package nl.miw.ch17.mmadevforce.kookkompas.model;

import jakarta.persistence.*;

/**
 * @author MMA Dev Force
 * Information regarding shoppinglistitems
 */
@Entity
public class ShoppingListItem {

    @Id @GeneratedValue
    private long shoppingListItemId;

    private double amount;
    private String unit;

    @ManyToOne
    private ShoppingList shoppingList;

    @ManyToOne
    private Ingredient ingredient;

    public ShoppingListItem(double amount, String unit) {
        this.amount = amount;
        this.unit = unit;
    }

    public ShoppingListItem() {
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public long getShoppingListItemId() {
        return shoppingListItemId;
    }

    public void setShoppingListItemId(long shoppingListId) {
        this.shoppingListItemId = shoppingListId;
    }

    public void setShoppingList(ShoppingList shoppingList) {
        this.shoppingList = shoppingList;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }
}
