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
}
