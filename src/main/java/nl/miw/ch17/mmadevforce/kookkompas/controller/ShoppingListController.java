package nl.miw.ch17.mmadevforce.kookkompas.controller;

import nl.miw.ch17.mmadevforce.kookkompas.model.Ingredient;
import nl.miw.ch17.mmadevforce.kookkompas.model.Recipe;
import nl.miw.ch17.mmadevforce.kookkompas.model.RecipeIngredient;
import nl.miw.ch17.mmadevforce.kookkompas.model.ShoppingListItem;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.RecipeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Manon Kuipers
 * Doel methode
 */
@Controller
public class ShoppingListController {

    private final RecipeRepository recipeRepository;

    public ShoppingListController(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @GetMapping("/shoppinglist")
    public String getShoppingList(@RequestParam List<Long> recipeIds, Model model) {
        List<Recipe> recipes = recipeRepository.findAllById(recipeIds);

        List<ShoppingListItem> shoppingList = new ArrayList<>();
        for (Recipe recipe : recipes) {
            for (RecipeIngredient recipeIngredient : recipe.getRecipeingredients()) {
                shoppingList.add(new ShoppingListItem(
                        recipeIngredient.getIngredient().getName(),
                        recipeIngredient.getIngredientAmount(),
                        recipeIngredient.getUnit()
                ));
            }
        }

        model.addAttribute("shoppingList", shoppingList);
        return "shoppingList";
    }

    @GetMapping("/shoppinglist/add/{recipeId}")
    public String addRecipeToShoppingList(@PathVariable Long recipeId, Model model) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new IllegalArgumentException("Recipe not found"));

        List<ShoppingListItem> shoppingList = new ArrayList<>();
        for (RecipeIngredient recipeIngredient : recipe.getRecipeingredients()) {
            shoppingList.add(new ShoppingListItem(
                    recipeIngredient.getIngredient().getName(),
                    recipeIngredient.getIngredientAmount(),
                    recipeIngredient.getUnit()
            ));
        }

        model.addAttribute("shoppingList", shoppingList);
        return "shoppingList";
    }


}
