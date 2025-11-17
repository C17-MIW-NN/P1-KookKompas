package nl.miw.ch17.mmadevforce.kookkompas;

import nl.miw.ch17.mmadevforce.kookkompas.controller.RecipeController;
import nl.miw.ch17.mmadevforce.kookkompas.model.Recipe;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.CategoryRepository;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.RecipeRepository;
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
        //Arrange
        RecipeRepository recipeRepository = Mockito.mock(RecipeRepository.class);
        CategoryRepository categoryRepository = Mockito.mock(CategoryRepository.class);
        Model model = Mockito.mock(Model.class);

        RecipeController controller = new RecipeController(recipeRepository, categoryRepository);

        Recipe recipe = new Recipe();
        recipe.setTitle("Pasta");

        Mockito.when(recipeRepository.findByTitleContainingIgnoreCase("Pasta"))
                .thenReturn(List.of(recipe));
        Mockito.when(recipeRepository.findDistinctByRecipeingredients_Ingredient_NameIgnoreCase("Pasta"))
                .thenReturn(List.of());

        //Act
        String viewName = controller.searchRecipes("Pasta", model);

        //Assert
        assertEquals("recipeSearchResults", viewName);

        Mockito.verify(model).addAttribute("recipes", Set.of(recipe));
        Mockito.verify(model).addAttribute("query", "Pasta");
    }

    @Test
    @DisplayName("test search terms with low case letters")
    void testSearchTermsWithLowCaseLetters() {
        //Arrange
        RecipeRepository recipeRepository = Mockito.mock(RecipeRepository.class);
        CategoryRepository categoryRepository = Mockito.mock(CategoryRepository.class);
        Model model = Mockito.mock(Model.class);

        RecipeController controller = new RecipeController(recipeRepository, categoryRepository);

        Recipe recipe = new Recipe();
        recipe.setTitle("pasta");

        Mockito.when(recipeRepository.findByTitleContainingIgnoreCase("pasta"))
                .thenReturn(List.of(recipe));
        Mockito.when(recipeRepository.findDistinctByRecipeingredients_Ingredient_NameIgnoreCase("pasta"))
                .thenReturn(List.of());

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
        RecipeRepository recipeRepository = Mockito.mock(RecipeRepository.class);
        CategoryRepository categoryRepository = Mockito.mock(CategoryRepository.class);
        Model model = Mockito.mock(Model.class);

        RecipeController controller = new RecipeController(recipeRepository, categoryRepository);

        Mockito.when(recipeRepository.findByTitleContainingIgnoreCase("Patsa"))
                .thenReturn(List.of());
        Mockito.when(recipeRepository.findDistinctByRecipeingredients_Ingredient_NameIgnoreCase("Patsa"))
                .thenReturn(List.of());

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
        //Arrange
        RecipeRepository recipeRepository = Mockito.mock(RecipeRepository.class);
        CategoryRepository categoryRepository = Mockito.mock(CategoryRepository.class);
        Model model = Mockito.mock(Model.class);

        RecipeController controller = new RecipeController(recipeRepository, categoryRepository);

        Recipe recipe = new Recipe();
        recipe.setTitle("pasta");

        Mockito.when(recipeRepository.findByTitleContainingIgnoreCase("p"))
                .thenReturn(List.of(recipe));
        Mockito.when(recipeRepository.findDistinctByRecipeingredients_Ingredient_NameIgnoreCase("p"))
                .thenReturn(List.of());

        //Act
        String viewName = controller.searchRecipes("p", model);

        //Assert
        assertEquals("recipeSearchResults", viewName);

        Mockito.verify(model).addAttribute("recipes", Set.of(recipe));
        Mockito.verify(model).addAttribute("query", "p");
    }

    @Test
    @DisplayName("test search terms with a symbol")
    void testSearchTermsWithSymbol() {
        //Arrange
        RecipeRepository recipeRepository = Mockito.mock(RecipeRepository.class);
        CategoryRepository categoryRepository = Mockito.mock(CategoryRepository.class);
        Model model = Mockito.mock(Model.class);

        RecipeController controller = new RecipeController(recipeRepository, categoryRepository);

        Mockito.when(recipeRepository.findByTitleContainingIgnoreCase("@"))
                .thenReturn(List.of());
        Mockito.when(recipeRepository.findDistinctByRecipeingredients_Ingredient_NameIgnoreCase("@"))
                .thenReturn(List.of());

        //Act
        String viewName = controller.searchRecipes("@", model);

        //Assert
        assertEquals("recipeSearchResults", viewName);

        Mockito.verify(model).addAttribute("recipes", Set.of());
        Mockito.verify(model).addAttribute("query", "@");
    }

}

