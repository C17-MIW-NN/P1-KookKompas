package nl.miw.ch17.mmadevforce.kookkompas.repositories;

import nl.miw.ch17.mmadevforce.kookkompas.model.RecipeStep;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Manon Kuipers
 * Doel methode
 */
public interface RecipeStepRepository extends JpaRepository<RecipeStep, Long> {
}
