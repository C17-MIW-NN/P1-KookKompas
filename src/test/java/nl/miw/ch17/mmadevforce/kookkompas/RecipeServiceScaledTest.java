package nl.miw.ch17.mmadevforce.kookkompas;

import nl.miw.ch17.mmadevforce.kookkompas.model.Ingredient;
import nl.miw.ch17.mmadevforce.kookkompas.model.Recipe;
import nl.miw.ch17.mmadevforce.kookkompas.model.RecipeIngredient;
import nl.miw.ch17.mmadevforce.kookkompas.service.RecipeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author MMA Dev Force
 * Test methods from Recipe Service
 */
public class RecipeServiceScaledTest {

    @Test
    @DisplayName("test scaled ingredients when servings is doubled")
    void testScaledIngredientsServingsDoubled() {
        // Arrange
        Recipe recipe = new Recipe();
        recipe.setServings(2);

        Ingredient flour = new Ingredient();
        flour.setName("Flour");

        RecipeIngredient recipeIngredient = new RecipeIngredient();
        recipeIngredient.setIngredient(flour);
        recipeIngredient.setIngredientAmount(100.0);
        recipeIngredient.setUnit("g");

        recipe.setRecipeingredients(List.of(recipeIngredient));

        RecipeService recipeService = new RecipeService(
                null,
                null,
                null);

        // Act
        List<RecipeIngredient> scaled = recipeService.getScaledIngredients(recipe, 4);

        // Assert
        assertEquals(200.0, scaled.get(0).getIngredientAmount());
        assertEquals("Flour", scaled.get(0).getIngredient().getName());
        assertEquals("g", scaled.get(0).getUnit());
    }

    @Test
    @DisplayName("test scaled ingredients when amount is null")
    void testScaledIngredientsAmountNull() {
        // Arrange
        Recipe recipe = new Recipe();
        recipe.setServings(2);

        Ingredient flour = new Ingredient();
        flour.setName("Flour");

        RecipeIngredient recipeIngredient = new RecipeIngredient();
        recipeIngredient.setIngredient(flour);
        recipeIngredient.setIngredientAmount(null);
        recipeIngredient.setUnit("g");

        recipe.setRecipeingredients(List.of(recipeIngredient));

        RecipeService recipeService = new RecipeService(
                null,
                null,
                null);

        // Act
        List<RecipeIngredient> scaled = recipeService.getScaledIngredients(recipe, 4);

        // Assert
        assertEquals(0.0, scaled.get(0).getIngredientAmount());
        assertEquals("Flour", scaled.get(0).getIngredient().getName());
        assertEquals("g", scaled.get(0).getUnit());
    }

    @Test
    @DisplayName("test scaled ingredients when servings is halved")
    void testScaledIngredientsServingsHalved() {
        // Arrange
        Recipe recipe = new Recipe();
        recipe.setServings(4);

        Ingredient flour = new Ingredient();
        flour.setName("Flour");

        RecipeIngredient recipeIngredient = new RecipeIngredient();
        recipeIngredient.setIngredient(flour);
        recipeIngredient.setIngredientAmount(200.0);
        recipeIngredient.setUnit("g");

        recipe.setRecipeingredients(List.of(recipeIngredient));

        RecipeService recipeService = new RecipeService(
                null,
                null,
                null);

        // Act
        List<RecipeIngredient> scaled = recipeService.getScaledIngredients(recipe, 2);

        // Assert
        assertEquals(100.0, scaled.get(0).getIngredientAmount());
        assertEquals("Flour", scaled.get(0).getIngredient().getName());
        assertEquals("g", scaled.get(0).getUnit());
    }

}
