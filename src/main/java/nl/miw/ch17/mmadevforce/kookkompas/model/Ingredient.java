package nl.miw.ch17.mmadevforce.kookkompas.model;

import jakarta.persistence.*;

import java.util.List;

/**
 * @author MMA Dev Force
 * The ingredients that will be used in the recipes.
 */
@Entity
public class Ingredient {

    @Id @GeneratedValue
    private Long ingredientId;

    @Column(unique = true)
    String name;

    @OneToMany(mappedBy = "ingredient")
    private List<RecipeIngredient> recipeingredients;

    public Ingredient(String name) {
        this.name = name;
    }

    public Ingredient() {
    }

    public Long getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(Long ingredientId) {
        this.ingredientId = ingredientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
