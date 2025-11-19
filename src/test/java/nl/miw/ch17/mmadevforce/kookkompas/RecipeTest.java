package nl.miw.ch17.mmadevforce.kookkompas;

import nl.miw.ch17.mmadevforce.kookkompas.controller.RecipeController;
import nl.miw.ch17.mmadevforce.kookkompas.model.Recipe;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.CategoryRepository;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.IngredientRepository;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.RecipeRepository;
import nl.miw.ch17.mmadevforce.kookkompas.service.CategoryService;
import nl.miw.ch17.mmadevforce.kookkompas.service.RecipeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author MMA Dev Force
 * Test methods from Recipe class
 */
public class RecipeTest {

    @Test
    @DisplayName("test search terms with capitalized letter")
    void testSearchTermsWithCapitalizedLetter() {
        // Arrange
        RecipeService recipeService = Mockito.mock(RecipeService.class);
        CategoryService categoryService = Mockito.mock(CategoryService.class);
        Model model = Mockito.mock(Model.class);

        RecipeController controller = new RecipeController(recipeService, categoryService);

        Recipe recipe = new Recipe();
        recipe.setTitle("Pasta");

        Mockito.when(recipeService.searchRecipes("Pasta"))
                .thenReturn(Set.of(recipe));

        // Act
        String viewName = controller.searchRecipes("Pasta", model);

        // Assert
        assertEquals("recipeSearchResults", viewName);
        Mockito.verify(model).addAttribute("recipes", Set.of(recipe));
        Mockito.verify(model).addAttribute("query", "Pasta");
    }

    @Test
    @DisplayName("test search terms with low case letters")
    void testSearchTermsWithLowCaseLetters() {
        //Arrange
        RecipeService recipeService = Mockito.mock(RecipeService.class);
        CategoryService categoryService = Mockito.mock(CategoryService.class);

        Model model = Mockito.mock(Model.class);

        RecipeController controller = new RecipeController(recipeService, categoryService);
        Recipe recipe = new Recipe();
        recipe.setTitle("pasta");

        Mockito.when(recipeService.searchRecipes("pasta"))
                .thenReturn(Set.of(recipe));

        //Act
        String viewName = controller.searchRecipes("pasta", model);

        //Assert
        assertEquals("recipeSearchResults", viewName);

        Mockito.verify(model).addAttribute("recipes", Set.of(recipe));
        Mockito.verify(model).addAttribute("query", "pasta");
    }

    @Test
    @DisplayName("test search terms with spelling mistake")
    void testSearchTermsWithSpellingMistake() {
        //Arrange
        RecipeService recipeService = Mockito.mock(RecipeService.class);
        CategoryService categoryService = Mockito.mock(CategoryService.class);

        Model model = Mockito.mock(Model.class);

        RecipeController controller = new RecipeController(recipeService, categoryService);

        Mockito.when(recipeService.searchRecipes("Patsa"))
                .thenReturn(Set.of());

        //Act
        String viewName = controller.searchRecipes("Patsa", model);

        //Assert
        assertEquals("recipeSearchResults", viewName);

        Mockito.verify(model).addAttribute("recipes", Set.of());
        Mockito.verify(model).addAttribute("query", "Patsa");
    }

    @Test
    @DisplayName("test search terms with one letter")
    void testSearchTermsWithOneLetter() {
        // Arrange
        RecipeService recipeService = Mockito.mock(RecipeService.class);
        CategoryService categoryService = Mockito.mock(CategoryService.class);

        Model model = Mockito.mock(Model.class);

        RecipeController controller = new RecipeController(recipeService, categoryService);

        Recipe recipe = new Recipe();
        recipe.setTitle("pasta");

        Mockito.when(recipeService.searchRecipes("p"))
                .thenReturn(Set.of(recipe));

        // Act
        String viewName = controller.searchRecipes("p", model);

        // Assert
        assertEquals("recipeSearchResults", viewName);
        Mockito.verify(model).addAttribute("recipes", Set.of(recipe));
        Mockito.verify(model).addAttribute("query", "p");
    }

    @Test
    @DisplayName("test search terms with a symbol")
    void testSearchTermsWithSymbol() {
        // Arrange
        RecipeService recipeService = Mockito.mock(RecipeService.class);
        CategoryService categoryService = Mockito.mock(CategoryService.class);

        Model model = Mockito.mock(Model.class);

        RecipeController controller = new RecipeController(recipeService, categoryService);

        Mockito.when(recipeService.searchRecipes("@"))
                .thenReturn(Set.of());

        // Act
        String viewName = controller.searchRecipes("@", model);

        // Assert
        assertEquals("recipeSearchResults", viewName);
        Mockito.verify(model).addAttribute("recipes", Set.of());
        Mockito.verify(model).addAttribute("query", "@");
    }
}

