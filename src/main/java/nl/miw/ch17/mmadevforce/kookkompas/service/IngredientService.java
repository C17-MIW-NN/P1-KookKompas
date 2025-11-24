package nl.miw.ch17.mmadevforce.kookkompas.service;

import nl.miw.ch17.mmadevforce.kookkompas.model.Ingredient;
import nl.miw.ch17.mmadevforce.kookkompas.model.Recipe;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.IngredientRepository;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.RecipeRepository;
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

    public IngredientService(IngredientRepository ingredientRepository, RecipeRepository recipeRepository) {
        this.ingredientRepository = ingredientRepository;
        this.recipeRepository = recipeRepository;
    }

    public List<Ingredient> findAllIngredients() {
        return ingredientRepository.findAll();
    }

    public Ingredient saveIngredient(Ingredient ingredient) {
        return ingredientRepository.save(ingredient);
    }

    public Optional<Ingredient> findByIngredientName(String name) {
        return ingredientRepository.findByIngredientName(name);
    }
    public void deleteIngredient(Long ingredientId) {
        Ingredient ingredient = ingredientRepository.findById(ingredientId).orElse(null);

        if (ingredient != null) {
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
