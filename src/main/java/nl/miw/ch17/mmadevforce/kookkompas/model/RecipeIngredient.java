package nl.miw.ch17.mmadevforce.kookkompas.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

/**
 * @author Melanie van der Vlies
 * The concept to add ingredients to recipe.
 */
@Entity
public class RecipeIngredient {

    @Id @GeneratedValue
    private Long recipeIngredientId;
    private Long recipeId;
    private Long ingredientId;

    private double ingredientAmount;
    private String unit;

    @ManyToOne
    private Ingredient ingredient;

    @ManyToOne
    private Recipe recipe;


    public RecipeIngredient() {

    }

    //METHOD ADD AMOUNT
    public int addPerson(int servings) {
        return servings++;
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

    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    public Long getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(Long ingredientId) {
        this.ingredientId = ingredientId;
    }

    public double getIngredientAmount() {
        return ingredientAmount;
    }

    public void setIngredientAmount(double ingredientAmount) {
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
