package nl.miw.ch17.mmadevforce.kookkompas;

import nl.miw.ch17.mmadevforce.kookkompas.controller.IngredientController;
import nl.miw.ch17.mmadevforce.kookkompas.model.Ingredient;
import nl.miw.ch17.mmadevforce.kookkompas.model.Recipe;
import nl.miw.ch17.mmadevforce.kookkompas.model.RecipeIngredient;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.IngredientRepository;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.RecipeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author = MMA Dev Force
 *
 */

@ExtendWith(MockitoExtension.class)
public class IngredientDeleteTest {

    @InjectMocks
    private IngredientController ingredientController;

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private RecipeRepository recipeRepository;

    @Test
    @DisplayName("test deletion of ingredient when ingredient exists and has recipe")
    void testDeletionOfIngredientWhenIngredientExistsAndHasRecipe() {

        //Arrange
        Long id = 1L;
        Ingredient ingredient = new Ingredient();
        ingredient.setIngredientId(id);

        Recipe recipe = new Recipe();
        RecipeIngredient ri = new RecipeIngredient();
        ri.setIngredient(ingredient);
        recipe.setRecipeingredients(new ArrayList<>(List.of(ri)));

        when(ingredientRepository.findById(id)).thenReturn(Optional.of(ingredient));
        when(recipeRepository.findByRecipeingredientsIngredient(ingredient))
                .thenReturn(List.of(recipe));

        // Act
        String result = ingredientController.deleteIngredient(id);

        // Assert
        assertEquals("redirect:/ingredient/all", result);
        assertTrue(recipe.getRecipeingredients().isEmpty());
        Mockito.verify(ingredientRepository).findById(id);
        Mockito.verify(recipeRepository).findByRecipeingredientsIngredient(ingredient);
        Mockito.verify(ingredientRepository).delete(ingredient);
    }

    @Test
    @DisplayName("test deletion of ingredient when ingredient exists and has no recipe")
    void testDeletionOfIngredientWhenIngredientAndHasNoRecipe() {

        //Arrange
        Long id = 2L;
        Ingredient ingredient = new Ingredient();
        ingredient.setIngredientId(id);

        Recipe recipe = new Recipe();
        RecipeIngredient ri = new RecipeIngredient();
        ri.setIngredient(ingredient);
        recipe.setRecipeingredients(new ArrayList<>(List.of(ri)));

        when(ingredientRepository.findById(id)).thenReturn(Optional.of(ingredient));
        when(recipeRepository.findByRecipeingredientsIngredient(ingredient))
                .thenReturn(Collections.emptyList());

        // Act
        String result = ingredientController.deleteIngredient(id);

        // Assert
        assertEquals("redirect:/ingredient/all", result);
        Mockito.verify(ingredientRepository).findById(id);
        Mockito.verify(recipeRepository).findByRecipeingredientsIngredient(ingredient);
        Mockito.verify(ingredientRepository).delete(ingredient);
    }

    @Test
    @DisplayName("test deletion of ingredient when ingredient does not exist")
    void testDeletionOfIngredientWhenIngredientDoesNotExist() {

        // Arrange
        Long id = 3L;
        when(ingredientRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        String result = ingredientController.deleteIngredient(id);

        // Assert
        assertEquals("redirect:/ingredient/all", result);
        Mockito.verify(ingredientRepository).findById(id);
        Mockito.verify(recipeRepository, Mockito.never()).findByRecipeingredientsIngredient(any());
        Mockito.verify(ingredientRepository, Mockito.never()).delete(any());
    }
}
