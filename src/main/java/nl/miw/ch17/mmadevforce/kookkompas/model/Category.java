package nl.miw.ch17.mmadevforce.kookkompas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

/**
 * @author MMA Dev Force
 * Information regarding recipe categories
 */
@Entity
public class Category {
    private static final int MIN_CHARACTERS_CATEGORY = 2;
    private static final int MAX_CHARACTERS_CATEGORY = 20;

    @Id @GeneratedValue
    private Long categoryId;

    @NotBlank(message = "Categorienaam mag niet leeg zijn.")
    @Size(min = MIN_CHARACTERS_CATEGORY, max = MAX_CHARACTERS_CATEGORY,
            message = "Categorienaam moet tussen de 2 en 20 tekens lang zijn.")
    private String categoryName;

    private String categoryColor;
    private boolean publicVisible;

    @ManyToOne
    private KookKompasUser owner;

    @ManyToMany(mappedBy = "categories")
    private Set<Recipe> recipes = new HashSet<>();

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    public Category() {
    }

    @Override
    public String toString() {
        return categoryName;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryColor() {
        return categoryColor;
    }

    public void setCategoryColor(String categoryColor) {
        this.categoryColor = categoryColor;
    }
    public Set<Recipe> getRecipes() {
        return recipes;
    }

    public boolean isPublicVisible() {
        return publicVisible;
    }

    public void setPublicVisible(boolean publicVisible) {
        this.publicVisible = publicVisible;
    }

    public KookKompasUser getOwner() {
        return owner;
    }

    public void setOwner(KookKompasUser owner) {
        this.owner = owner;
    }
}
