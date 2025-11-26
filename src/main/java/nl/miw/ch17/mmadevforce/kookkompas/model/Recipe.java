package nl.miw.ch17.mmadevforce.kookkompas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author MMA Dev Force
 * Information regarding recipes of KookKompas
 */

@Entity
public class Recipe {
    private final int DEFAULT_MINIMUM_SERVINGS = 4;
    private static final int MIN_CHARACTERS_RECIPE = 2;
    private static final int MAX_CHARACTERS_RECIPE = 50;

    @Id @GeneratedValue
    private Long recipeId;

    @Column(unique = true)
    @NotBlank(message = "Titel mag niet leeg zijn")
    @Size(min = MIN_CHARACTERS_RECIPE, max = MAX_CHARACTERS_RECIPE,
            message = "Titel van het recept moet tussen de 2 en 50 tekens lang zijn.")
    private String title;

    private String description;
    private int servings = DEFAULT_MINIMUM_SERVINGS;
    private String coverImageUrl;
    private boolean publicVisible;

    @ManyToOne
    private KookKompasUser owner;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeIngredient> recipeingredients;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<RecipeStep> steps = new ArrayList<>();

    @ManyToMany
    private Set<Category> categories;

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

    public int getCookingTime() {
        return steps.stream()
                .mapToInt(RecipeStep::getCookingTimePerStep)
                .sum();
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
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

    public List<RecipeStep> getSteps() {
        return steps;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public KookKompasUser getOwner() {
        return owner;
    }

    public void setOwner(KookKompasUser owner) {
        this.owner = owner;
    }

    public boolean isPublicVisible() {
        return publicVisible;
    }

    public void setPublicVisible(boolean publicVisible) {
        this.publicVisible = publicVisible;
    }
}
