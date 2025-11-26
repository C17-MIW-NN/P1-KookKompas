package nl.miw.ch17.mmadevforce.kookkompas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * @author MMA Dev Force
 * The ingredients that will be used in the recipes.
 */
@Entity
public class Ingredient {
    private static final int MIN_CHARACTERS_INGREDIENT = 2;
    private static final int MAX_CHARACTERS_INGREDIENT = 50;

    @Id @GeneratedValue
    private Long ingredientId;

    @Column(unique = true)
    @NotBlank(message = "Naam mag niet leeg zijn")
    @Size(min = MIN_CHARACTERS_INGREDIENT,
            max = MAX_CHARACTERS_INGREDIENT,
            message = "Naam moet tussen 2 en 50 tekens zijn")
    private String ingredientName;

    public Ingredient(String name) {
        this.ingredientName = name;
    }

    public Ingredient() {
    }

    public Long getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(Long ingredientId) {
        this.ingredientId = ingredientId;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String name) {
        this.ingredientName = name;
    }
}