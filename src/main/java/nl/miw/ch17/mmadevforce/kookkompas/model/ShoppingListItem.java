package nl.miw.ch17.mmadevforce.kookkompas.model;

import jakarta.persistence.Entity;

/**
 * @author Manon Kuipers
 * Doel methode
 */
public class ShoppingListItem {
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
}
