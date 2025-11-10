package nl.miw.ch17.mmadevforce.kookkompas.repositories;

import nl.miw.ch17.mmadevforce.kookkompas.model.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RecipeIngredientRepository extends JpaRepository <RecipeIngredient, Long> {
}
