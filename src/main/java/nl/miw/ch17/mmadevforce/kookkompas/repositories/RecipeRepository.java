package nl.miw.ch17.mmadevforce.kookkompas.repositories;

import nl.miw.ch17.mmadevforce.kookkompas.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Arjen Zijlstra
 *
 */
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    Optional<Recipe> findByTitle(String title);

}
