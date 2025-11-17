package nl.miw.ch17.mmadevforce.kookkompas.model;

import jakarta.persistence.*;

import java.util.Set;

/**
 * @author MMA Dev Force
 *
 */
@Entity
public class Category {

    @Id @GeneratedValue
    private Long categoryId;

    @Column(unique = true)
    private String categoryName;

    private String categoryColor;

    public Category(String categoryName, String categoryColor) {
        this.categoryName = categoryName;
        this.categoryColor = categoryColor;
    }

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
}
