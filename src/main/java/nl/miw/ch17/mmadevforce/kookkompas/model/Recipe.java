package nl.miw.ch17.mmadevforce.kookkompas.model;

import jakarta.persistence.*;

/**
 * @author MMA Dev Force
 *
 */

@Entity
public class Recipe {

    @Id @GeneratedValue
    long recipeId;
    String title;

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
