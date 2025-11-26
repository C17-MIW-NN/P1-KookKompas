package nl.miw.ch17.mmadevforce.kookkompas.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author MMA Dev Force
 * Information regarding the Shoppinglist of KookKompasUsers
 */
@Entity
public class ShoppingList {

    @Id
    @GeneratedValue
    private Long shoppingListId;

    @OneToMany(mappedBy = "shoppingList", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShoppingListItem> shoppingItems = new ArrayList<>();

    @OneToOne
    private KookKompasUser kookKompasUser;

    public ShoppingList(Long shoppingListId) {
        this.shoppingListId = shoppingListId;
    }

    public ShoppingList(){
    }

    //METHOD

    public void addItem(ShoppingListItem item) {
        item.setShoppingList(this);
        this.shoppingItems.add(item);
    }

    public List<ShoppingListItem> getShoppingItems() {
        return shoppingItems;
    }

    public void setShoppingItems(List<ShoppingListItem> shoppingItems) {
        this.shoppingItems = shoppingItems;
    }

    public Long getShoppingListId() {
        return shoppingListId;
    }

    public void setShoppingListId(Long shoppingListId) {
        this.shoppingListId = shoppingListId;
    }

    public void setKookKompasUser(KookKompasUser kookKompasUser) {
        this.kookKompasUser = kookKompasUser;
    }
}
