package nl.miw.ch17.mmadevforce.kookkompas.controller;

import nl.miw.ch17.mmadevforce.kookkompas.model.Recipe;
import nl.miw.ch17.mmadevforce.kookkompas.model.RecipeIngredient;
import nl.miw.ch17.mmadevforce.kookkompas.model.ShoppingListItem;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.RecipeRepository;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.ShoppingListItemRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Manon Kuipers
 * Doel methode
 */
@Controller
@RequestMapping("/shoppinglist")
public class ShoppingListController {

    private final RecipeRepository recipeRepository;
    private final ShoppingListItemRepository shoppingListItemRepository;

    public ShoppingListController(RecipeRepository recipeRepository, ShoppingListItemRepository shoppingListItemRepository) {
        this.recipeRepository = recipeRepository;
        this.shoppingListItemRepository = shoppingListItemRepository;
    }

    @GetMapping("/all")
    public String showShoppingList(Model datamodel) {
        datamodel.addAttribute("shoppingList", shoppingListItemRepository.findAll());
        return "shoppingListOverview";
    }

    @GetMapping("/add/{recipeId}")
    public String addRecipeToShoppingList(@PathVariable Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new IllegalArgumentException("Recipe not found"));

        for (RecipeIngredient recipeIngredient : recipe.getRecipeingredients()) {
            Optional<ShoppingListItem> existing = shoppingListItemRepository.findAll().stream()
                    .filter(item ->
                            item.getIngredientName().equals(recipeIngredient.getIngredient().getName())
                            && item.getUnit().equals(recipeIngredient.getUnit()))
                    .findFirst();

            if (existing.isPresent()) {
                ShoppingListItem item = existing.get();
                item.setAmount(item.getAmount() + recipeIngredient.getIngredientAmount());
                shoppingListItemRepository.save(item);
            } else {
                shoppingListItemRepository.save(new ShoppingListItem(
                        recipeIngredient.getIngredient().getName(),
                        recipeIngredient.getIngredientAmount(),
                        recipeIngredient.getUnit()
                ));
            }
        }

        return "redirect:/shoppinglist/all";
    }

    @GetMapping("/clear")
    public String clearShoppingList() {
        shoppingListItemRepository.deleteAll();
        return "redirect:/shoppinglist/all";
    }


}
