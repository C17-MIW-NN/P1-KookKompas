package nl.miw.ch17.mmadevforce.kookkompas.repositories;

import nl.miw.ch17.mmadevforce.kookkompas.model.Ingredient;
import nl.miw.ch17.mmadevforce.kookkompas.model.ShoppingList;
import nl.miw.ch17.mmadevforce.kookkompas.model.ShoppingListItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author MMA Dev Force
 */
public interface ShoppingListItemRepository extends JpaRepository<ShoppingListItem, Long> {
    Optional<ShoppingListItem> findByShoppingListAndIngredientAndUnit(
            ShoppingList shoppingList,
            Ingredient ingredient,
            String unit);
}
