package nl.miw.ch17.mmadevforce.kookkompas.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

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

    public ShoppingList(){
    }

    public Long getShoppingListId() {
        return shoppingListId;
    }

    public void setShoppingListId(Long shoppingListId) {
        this.shoppingListId = shoppingListId;
    }

    public List<ShoppingListItem> getShoppingItems() {
        return shoppingItems;
    }

    public void setKookKompasUser(KookKompasUser kookKompasUser) {
        this.kookKompasUser = kookKompasUser;
    }
}