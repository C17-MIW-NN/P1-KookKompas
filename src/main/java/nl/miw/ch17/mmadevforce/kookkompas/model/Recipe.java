package nl.miw.ch17.mmadevforce.kookkompas.model;

import jakarta.persistence.*;

import java.util.List;

/**
 * @author MMA Dev Force
 *
 */

@Entity
public class Recipe {

    @Id @GeneratedValue
    long recipeId;
    String title;

    @OneToMany(mappedBy = "recipe")
    private List<RecipeIngredient> recipeingredients;

    public String getTitle() {
        return title;
    }

    public void setTitle(String Title) {
        this.title = Title;
    }

    public long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(long recipeId) {
        this.recipeId = recipeId;
    }
}
