package nl.miw.ch17.mmadevforce.kookkompas.repositories;

import nl.miw.ch17.mmadevforce.kookkompas.model.Ingredient;
import nl.miw.ch17.mmadevforce.kookkompas.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author Arjen Zijlstra
 *
 */
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Optional<Recipe> findByTitle(String title);

    List<Recipe> findByTitleContainingIgnoreCase(String query);

    List<Recipe> findByRecipeingredientsIngredient(Ingredient ingredient);

    List<Recipe> findByCategories_CategoryNameIgnoreCase(String categoryName);

    List<Recipe> findDistinctByRecipeingredients_Ingredient_IngredientNameIgnoreCase(String query);
}
