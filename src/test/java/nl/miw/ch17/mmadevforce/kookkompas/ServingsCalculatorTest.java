package nl.miw.ch17.mmadevforce.kookkompas;

import nl.miw.ch17.mmadevforce.kookkompas.service.RecipeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author MMA Dev Force
 * Test of the increase and decrease servings function
 */
public class ServingsCalculatorTest {

    @Test
    @DisplayName("test if the currentServings amount increases by 1")
    void testIncreaseServings() {
        //Arrange
        RecipeService recipeService = new RecipeService(null, null
                , null,null);
        int currentServings = 3;

        //Act
        int result = recipeService.increaseServings(currentServings);

        //Assert
        assertEquals(4, result);
    }

    @Test
    @DisplayName("test of the currentServings amount decreases by 1")
    void testDecreaseServingsMoreThanOne() {
        //Arrange
        RecipeService recipeService = new RecipeService(null, null
                , null, null);
        int currentServings = 3;

        //Act
        int result = recipeService.decreaseServings(currentServings);

        //Assert
        assertEquals(2, result);
    }

    @Test
    @DisplayName("test if the servings amount does not get lower than 1 with a working return path")
    void testDecreaseServingsThrowsExceptionAtMinimum() {
        //Arrange
        RecipeService recipeService = new RecipeService(null, null
                , null, null);
        int currentServings = 1;

        //Act
        assertThrows(IllegalArgumentException.class, () -> {
            recipeService.decreaseServings(currentServings);
        });
    }

}
