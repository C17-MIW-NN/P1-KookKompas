package nl.miw.ch17.mmadevforce.kookkompas.service;

import nl.miw.ch17.mmadevforce.kookkompas.model.Ingredient;
import nl.miw.ch17.mmadevforce.kookkompas.model.Recipe;
import nl.miw.ch17.mmadevforce.kookkompas.model.ShoppingListItem;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.IngredientRepository;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.RecipeRepository;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.ShoppingListItemRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author MMA Dev Force
 * Managing functionalities from ingredients
 */
@Service
public class IngredientService {
    private final IngredientRepository ingredientRepository;
    private final RecipeRepository recipeRepository;
    private final ShoppingListItemRepository shoppingListItemRepository;

    public IngredientService(IngredientRepository ingredientRepository, RecipeRepository recipeRepository, ShoppingListItemRepository shoppingListItemRepository) {
        this.ingredientRepository = ingredientRepository;
        this.recipeRepository = recipeRepository;
        this.shoppingListItemRepository = shoppingListItemRepository;
    }

    public List<Ingredient> findAllIngredients() {
        return ingredientRepository.findAll();
    }

    public Ingredient saveIngredient(Ingredient ingredient) {
        Optional<Ingredient> existing = ingredientRepository.findByIngredientName(ingredient.getIngredientName());

        if (existing.isPresent() &&
                (ingredient.getIngredientId() == null ||
                        !existing.get().getIngredientId().equals(ingredient.getIngredientId()))) {
            throw new DataIntegrityViolationException("Ingredient name must be unique");
        }
        return ingredientRepository.save(ingredient);
    }

    public Optional<Ingredient> findByIngredientId(Long ingredientId) {
        return ingredientRepository.findById(ingredientId);
    }

    public void deleteIngredient(Long ingredientId) {
        Ingredient ingredient = ingredientRepository.findById(ingredientId).orElse(null);

        if (ingredient != null) {
            List<ShoppingListItem> items = shoppingListItemRepository.findByIngredient(ingredient);
            shoppingListItemRepository.deleteAll(items);

            List<Recipe> recipesWithIngredient = recipeRepository.findByRecipeingredientsIngredient(ingredient);
            recipesWithIngredient.forEach(recipe -> {
                recipe.getRecipeingredients()
                        .removeIf(recipeIngredient ->
                                recipeIngredient.getIngredient().getIngredientId()
                                        .equals(ingredient.getIngredientId()));
            });
            ingredientRepository.delete(ingredient);
        }
    }
}
