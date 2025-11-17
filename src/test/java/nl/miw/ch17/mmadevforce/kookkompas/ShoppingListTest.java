package nl.miw.ch17.mmadevforce.kookkompas;


import nl.miw.ch17.mmadevforce.kookkompas.controller.ShoppingListController;
import nl.miw.ch17.mmadevforce.kookkompas.model.Ingredient;
import nl.miw.ch17.mmadevforce.kookkompas.model.Recipe;
import nl.miw.ch17.mmadevforce.kookkompas.model.RecipeIngredient;
import nl.miw.ch17.mmadevforce.kookkompas.model.ShoppingListItem;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.IngredientRepository;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.RecipeIngredientRepository;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.RecipeRepository;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.ShoppingListItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Manon Kuipers
 * Test methods from the ShoppingList class
 */
@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
@ActiveProfiles("test")
public class ShoppingListTest {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private ShoppingListItemRepository shoppingListItemRepository;

    @Autowired
    private ShoppingListController shoppingListController;
    @Autowired
    private IngredientRepository ingredientRepository;
    @Autowired
    private RecipeIngredientRepository recipeIngredientRepository;

    @Test
    @DisplayName("Adding a new ingredient with a new unit to an empty shopping list")
    void testAddingWhenNewIngredientNameAndUnit() {

        Ingredient ingredient = ingredientRepository.findByName("Tomaat")
                .orElseGet(() -> ingredientRepository.save(new Ingredient("Tomaat")));

        RecipeIngredient recipeIngredient = new RecipeIngredient();
        recipeIngredient.setIngredient(ingredient);
        recipeIngredient.setUnit("stuk");
        recipeIngredient.setIngredientAmount(2.0);

        Recipe recipe = new Recipe();
        recipe.setTitle("Salade");
        recipe.setRecipeingredients(List.of(recipeIngredient));

        recipe = recipeRepository.save(recipe);

        String result = shoppingListController.addRecipeToShoppingList(recipe.getRecipeId());

        List<ShoppingListItem> items = shoppingListItemRepository.findAll();
        assertThat(items).hasSize(1);
        assertThat(items.get(0).getIngredientName()).isEqualTo("Tomaat");
        assertThat(items.get(0).getUnit()).isEqualTo("stuk");
        assertThat(items.get(0).getAmount()).isEqualTo(2.0);
        assertThat(result).isEqualTo("redirect:/shoppinglist/all");
    }
}
