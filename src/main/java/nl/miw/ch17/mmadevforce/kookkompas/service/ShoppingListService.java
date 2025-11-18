package nl.miw.ch17.mmadevforce.kookkompas.service;

import nl.miw.ch17.mmadevforce.kookkompas.model.Recipe;
import nl.miw.ch17.mmadevforce.kookkompas.model.RecipeIngredient;
import nl.miw.ch17.mmadevforce.kookkompas.model.ShoppingListItem;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.RecipeRepository;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.ShoppingListItemRepository;
import java.util.List;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author MMA Dev Force
 * Managing functionalities from the shopping list
 */
@Service
public class ShoppingListService {

    private final RecipeRepository recipeRepository;
    private final ShoppingListItemRepository shoppingListItemRepository;

    public ShoppingListService(RecipeRepository recipeRepository, ShoppingListItemRepository shoppingListItemRepository) {
        this.recipeRepository = recipeRepository;
        this.shoppingListItemRepository = shoppingListItemRepository;
    }

    public List<ShoppingListItem> findAllShoppingListItems() {
        return shoppingListItemRepository.findAll();
    }

    public void addRecipeToShoppingList(Long recipeId) {
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
    }

    public void clearShoppingList() {
        shoppingListItemRepository.deleteAll();
    }

}
