package nl.miw.ch17.mmadevforce.kookkompas.repositories;

import nl.miw.ch17.mmadevforce.kookkompas.model.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author MMA Dev Force
 */
public interface RecipeIngredientRepository extends JpaRepository <RecipeIngredient, Long> {
}
