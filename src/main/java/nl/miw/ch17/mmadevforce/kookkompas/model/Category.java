package nl.miw.ch17.mmadevforce.kookkompas.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

/**
 * @author MMA Dev Force
 *
 */
@Entity
public class Category {

    @Id @GeneratedValue
    private Long categoryId;

    private String categoryName;


    public Category(String category) {
        this.categoryName = category;
    }

    public Category() {

    }


    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategory() {
        return categoryName;
    }

    public void setCategory(String category) {
        this.categoryName = category;
    }
}
