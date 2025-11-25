package nl.miw.ch17.mmadevforce.kookkompas.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

/**
 * @author MMA Dev Force
 * Information regarding recipesteps
 */
@Entity
public class RecipeStep {
    @Id @GeneratedValue
    private Long recipeStepId;
    private int stepNumber;
    private String stepDescription;
    private int cookingTimePerStep;

    @ManyToOne
    private Recipe recipe;

    public Long getRecipeStepId() {
        return recipeStepId;
    }

    public void setRecipeStepId(Long recipeStepId) {
        this.recipeStepId = recipeStepId;
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
    }

    public String getStepDescription() {
        return stepDescription;
    }

    public void setStepDescription(String stepDescription) {
        this.stepDescription = stepDescription;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public int getCookingTimePerStep() {
        return cookingTimePerStep;
    }

    public void setCookingTimePerStep(int cookingTimePerStep) {
        this.cookingTimePerStep = cookingTimePerStep;
    }
}
