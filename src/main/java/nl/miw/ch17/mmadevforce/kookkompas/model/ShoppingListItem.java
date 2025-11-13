package nl.miw.ch17.mmadevforce.kookkompas.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

/**
 * @author Manon Kuipers
 * Doel methode
 */
@Entity
public class ShoppingListItem {

    @Id @GeneratedValue
    private long shoppingListId;

    private String ingredientName;
    private double amount;
    private String unit;

    public ShoppingListItem(String ingredientName, double amount, String unit) {
        this.ingredientName = ingredientName;
        this.amount = amount;
        this.unit = unit;
    }

    public ShoppingListItem() {
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
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

    public long getShoppingListId() {
        return shoppingListId;
    }

    public void setShoppingListId(long shoppingListId) {
        this.shoppingListId = shoppingListId;
    }
}
