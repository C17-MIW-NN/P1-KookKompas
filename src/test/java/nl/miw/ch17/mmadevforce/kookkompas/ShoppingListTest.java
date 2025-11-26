package nl.miw.ch17.mmadevforce.kookkompas;


import nl.miw.ch17.mmadevforce.kookkompas.controller.KookKompasUserController;
import nl.miw.ch17.mmadevforce.kookkompas.controller.ShoppingListController;
import nl.miw.ch17.mmadevforce.kookkompas.model.*;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.*;
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
 * @author MMA Dev Force
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
    private KookKompasUserRepository kookKompasUserRepository;

    @Autowired
    private KookKompasUserController kookKompasUserController;

    @Autowired
    private ShoppingListController shoppingListController;
    @Autowired
    private IngredientRepository ingredientRepository;
    @Autowired
    private RecipeIngredientRepository recipeIngredientRepository;

    @Test
    @DisplayName("Adding a new ingredient with a new unit to an empty shopping list")
    void testAddingWhenNewIngredientNameAndUnit() {

        Ingredient ingredient = ingredientRepository.findByIngredientName("Tomaat")
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
        assertThat(items.get(0).getIngredient().getIngredientName()).isEqualTo("Tomaat");
        assertThat(items.get(0).getUnit()).isEqualTo("stuk");
        assertThat(items.get(0).getAmount()).isEqualTo(2.0);
        assertThat(result).isEqualTo("redirect:/shoppinglist/all");
    }

    @Test
    @DisplayName("Adding existing ingredient with unit combination to shopping list")
    void testAddingWhenIngredientNameAndUnitExists() {
        ShoppingListItem existingItem = new ShoppingListItem();
        existingItem.getIngredient().setIngredientName("Tomaat");
        existingItem.setUnit("stuk");
        existingItem.setAmount(1.0);
        shoppingListItemRepository.save(existingItem);

        Ingredient ingredient = ingredientRepository.findByIngredientName("Tomaat")
                .orElseGet(() -> ingredientRepository.save(new Ingredient("Tomaat")));

        RecipeIngredient recipeIngredient = new RecipeIngredient();
        recipeIngredient.setIngredient(ingredient);
        recipeIngredient.setUnit("stuk");
        recipeIngredient.setIngredientAmount(2.0);

        Recipe recipe = new Recipe();
        recipe.setTitle("Salade");
        recipe.setRecipeingredients(List.of(recipeIngredient));

        recipe = recipeRepository.save(recipe);
        shoppingListController.addRecipeToShoppingList(recipe.getRecipeId());

        List<ShoppingListItem> items = shoppingListItemRepository.findAll();
        assertThat(items).hasSize(1);
        assertThat(items.get(0).getAmount()).isEqualTo(3.0);
    }

    @Test
    @DisplayName("Adding ingredient with same name but different unit to shopping list")
    void testAddingWhenIngredientExistsButUnitIsNew() {
        ShoppingListItem existingItem = new ShoppingListItem();
        //existingItem.setIngredientName("Tomaat");
        existingItem.setUnit("gram");
        existingItem.setAmount(100.0);
        shoppingListItemRepository.save(existingItem);

        Ingredient ingredient = ingredientRepository.findByIngredientName("Tomaat")
                .orElseGet(() -> ingredientRepository.save(new Ingredient("Tomaat")));

        RecipeIngredient recipeIngredient = new RecipeIngredient();
        recipeIngredient.setIngredient(ingredient);
        recipeIngredient.setUnit("stuk");
        recipeIngredient.setIngredientAmount(2.0);

        Recipe recipe = new Recipe();
        recipe.setTitle("Salade");
        recipe.setRecipeingredients(List.of(recipeIngredient));

        recipe = recipeRepository.save(recipe);
        shoppingListController.addRecipeToShoppingList(recipe.getRecipeId());

        List<ShoppingListItem> items = shoppingListItemRepository.findAll();
        assertThat(items).hasSize(2);
        assertThat(items).anyMatch(item ->
                item.getIngredient().getIngredientName().equals("Tomaat") && item.getUnit().equals("gram"));
        assertThat(items).anyMatch(item ->
                item.getIngredient().getIngredientName().equals("Tomaat") && item.getUnit().equals("stuk"));
    }

    @Test
    @DisplayName("Adding ingredient with same unit but different name to shopping list")
    void testAddingWhenUnitExistsButIngredientIsNew() {
        ShoppingListItem existingItem = new ShoppingListItem();
//        existingItem.setIngredientName("Komkommer");
        existingItem.setUnit("stuk");
        existingItem.setAmount(1.0);
        shoppingListItemRepository.save(existingItem);

        Ingredient ingredient = ingredientRepository.findByIngredientName("Tomaat")
                .orElseGet(() -> ingredientRepository.save(new Ingredient("Tomaat")));

        RecipeIngredient recipeIngredient = new RecipeIngredient();
        recipeIngredient.setIngredient(ingredient);
        recipeIngredient.setUnit("stuk");
        recipeIngredient.setIngredientAmount(2.0);

        Recipe recipe = new Recipe();
        recipe.setTitle("Salade");
        recipe.setRecipeingredients(List.of(recipeIngredient));

        recipe = recipeRepository.save(recipe);
        shoppingListController.addRecipeToShoppingList(recipe.getRecipeId());

        List<ShoppingListItem> items = shoppingListItemRepository.findAll();
        assertThat(items).hasSize(2);
        assertThat(items).anyMatch(item ->
                item.getIngredient().getIngredientName().equals("Komkommer") && item.getUnit().equals("stuk"));
        assertThat(items).anyMatch(item ->
                item.getIngredient().getIngredientName().equals("Tomaat") && item.getUnit().equals("stuk"));
    }
}
