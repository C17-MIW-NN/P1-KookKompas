package nl.miw.ch17.mmadevforce.kookkompas.model;

import jakarta.persistence.*;

/**
 * @author Melanie van der Vlies
 * The concept to add ingredients to recipe.
 */
@Entity
public class RecipeIngredient {

    @Id @GeneratedValue
    private Long recipeIngredientId;

    private Double ingredientAmount;
    private String unit;

    @ManyToOne
    private Ingredient ingredient;

    @ManyToOne
    private Recipe recipe;


    public RecipeIngredient() {

    }

    //METHOD ADD AMOUNT
    public double getScaledAmount(int currentServings, int defaultServings) {
        return ingredientAmount * currentServings / defaultServings;
    }

    public int decreasePerson(int servings) {
        if (servings > 1) {
            return servings - 1;
        }
        return servings;
    }


    public Long getRecipeIngredientId() {
        return recipeIngredientId;
    }

    public void setRecipeIngredientId(Long recipeIngredientId) {
        this.recipeIngredientId = recipeIngredientId;
    }

    public Double getIngredientAmount() {
        return ingredientAmount;
    }

    public void setIngredientAmount(Double ingredientAmount) {
        this.ingredientAmount = ingredientAmount;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }


}
