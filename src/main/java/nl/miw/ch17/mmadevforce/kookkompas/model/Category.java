package nl.miw.ch17.mmadevforce.kookkompas.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

/**
 * @author MMA Dev Force
 * Information regarding recipe categories
 */
@Entity
public class Category {

    @Id @GeneratedValue
    private Long categoryId;

    @Column(unique = true)
    private String categoryName;

    private String categoryColor;

    @ManyToMany(mappedBy = "categories")
    private Set<Recipe> recipes = new HashSet<>();

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    public Category() {
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

}
