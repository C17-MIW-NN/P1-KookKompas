package nl.miw.ch17.mmadevforce.kookkompas.repositories;

import nl.miw.ch17.mmadevforce.kookkompas.model.ShoppingList;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author MMA Dev Force
 * Doel van de class:
 */
public interface ShoppingListRepository extends JpaRepository<ShoppingList, Long> {

}
