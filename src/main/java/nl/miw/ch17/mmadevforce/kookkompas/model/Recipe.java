package nl.miw.ch17.mmadevforce.kookkompas.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author MMA Dev Force
 * Information regarding recipes of KookKompas
 */

@Entity
public class Recipe {
    private final int DEFAULT_MINIMUM_SERVINGS = 4;

    @Id @GeneratedValue
    private Long recipeId;
    private String title;
    private String description;
    private int servings = DEFAULT_MINIMUM_SERVINGS;
    private String coverImageUrl;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeIngredient> recipeingredients;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeStep> steps = new ArrayList<>();

    @ManyToMany
    private Set<Category> categories;

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public Recipe(String title, int servings, List<RecipeIngredient> recipeingredients) {
        this.title = title;
        this.servings = servings;
        this.recipeingredients = recipeingredients;
    }

    public Recipe(String title) {
        this.title = title;
    }

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

    public List<RecipeIngredient> getRecipeingredients() {
        return recipeingredients;
    }

    public void setRecipeingredients(List<RecipeIngredient> recipeingredients) {
        this.recipeingredients = recipeingredients;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<RecipeStep> getSteps() {
        return steps;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }
}
