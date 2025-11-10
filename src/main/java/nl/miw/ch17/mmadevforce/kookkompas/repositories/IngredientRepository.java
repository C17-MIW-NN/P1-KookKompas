package nl.miw.ch17.mmadevforce.kookkompas.repositories;

import nl.miw.ch17.mmadevforce.kookkompas.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author MMA Dev Force
 * Doel methode
 */
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
}
