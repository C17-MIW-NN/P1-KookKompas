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
    private int servings = DEFAULT_MINIMUM_SERVINGS;

    @OneToMany(mappedBy = "recipe")
    private List<RecipeIngredient> recipeingredients;

    public Recipe() {

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
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }
}
