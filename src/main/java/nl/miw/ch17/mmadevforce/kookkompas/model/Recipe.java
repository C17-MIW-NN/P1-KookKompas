package nl.miw.ch17.mmadevforce.kookkompas.model;

import jakarta.persistence.*;

import java.util.List;

/**
 * @author MMA Dev Force
 *
 */

@Entity
public class Recipe {
    private final int DEFAULT_MINIMUM_SERVINGS = 2;

    @Id @GeneratedValue
    private Long recipeId;
    private String title;
    private String description;
    private int preparationTime;
    private int currentServings = DEFAULT_MINIMUM_SERVINGS;

    @OneToMany(mappedBy = "recipe")
    private List<RecipeIngredient> recipeingredients;

    public Recipe() {

    }

    public int addPerson(int servingsCurrent) {
        return servingsCurrent++;
    }

    public int decreasePerson(int servingsCurrent) {
        if (servingsCurrent > 1) {
            return servingsCurrent - 1;
        }
        return servingsCurrent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String Title) {
        this.title = Title;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    public int getServings() {
        return currentServings;
    }

    public void setServings(int servings) {
        this.currentServings = currentServings;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(int preparationTime) {
        this.preparationTime = preparationTime;
    }
}
