package nl.miw.ch17.mmadevforce.kookkompas.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

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
    private int servings = DEFAULT_MINIMUM_SERVINGS;

    @OneToMany(mappedBy = "recipe")
    private List<RecipeIngredient> recipeingredients;

    @OneToMany(mappedBy = "recipe")
    private List<RecipeStep> steps;

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

//    public int addPerson(int servingsCurrent) {
//        return servingsCurrent++;
//    }

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
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
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

    public int getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(int preparationTime) {
        this.preparationTime = preparationTime;
    }

    public List<RecipeStep> getSteps() {
        return steps;
    }

    public void setSteps(List<RecipeStep> steps) {
        this.steps = steps;
    }

}
