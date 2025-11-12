package nl.miw.ch17.mmadevforce.kookkompas.controller;

import nl.miw.ch17.mmadevforce.kookkompas.model.Ingredient;
import nl.miw.ch17.mmadevforce.kookkompas.model.Recipe;
import nl.miw.ch17.mmadevforce.kookkompas.model.RecipeIngredient;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.IngredientRepository;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.RecipeIngredientRepository;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.RecipeRepository;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author MMA Dev Force
 * Initialises the database with example data
 */
@Controller
public class InitializeController {
    private final IngredientRepository ingredientRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final RecipeRepository recipeRepository;


    public InitializeController(IngredientRepository ingredientRepository, RecipeIngredientRepository recipeIngredientRepository, RecipeRepository recipeRepository) {
        this.ingredientRepository = ingredientRepository;
        this.recipeIngredientRepository = recipeIngredientRepository;
        this.recipeRepository = recipeRepository;
    }

    @EventListener
    private void seed(ContextRefreshedEvent ignoredEvent) {
        if (ingredientRepository.count() == 0 && recipeRepository.count() == 0) {
            initializeDB();
        }
    }

    private void initializeDB() {
        Recipe pastaBolognese = new Recipe();
        pastaBolognese.setTitle("Pasta Bolognese");
        recipeRepository.save(pastaBolognese);

        ClassPathResource ingredientBestand = new ClassPathResource("static/IngredientList.csv");

        try (Scanner invoer = new Scanner(ingredientBestand.getInputStream())) {
            if (invoer.hasNextLine()) invoer.nextLine();

            while (invoer.hasNextLine()) {
                String regel = invoer.nextLine().trim();
                if (!regel.isEmpty()) {
                    String[] parts = regel.split(",");
                    String name = parts[0];
                    double ingredientAmount = Double.parseDouble(parts[1]);
                    String unit = parts[2];

                    Ingredient ingredient = ingredientRepository.findByName(name)
                            .orElseGet(() -> ingredientRepository.save(new Ingredient(name)));

                    RecipeIngredient recipeIngredient = new RecipeIngredient();
                    recipeIngredient.setIngredient(ingredient);
                    recipeIngredient.setRecipe(pastaBolognese);
                    recipeIngredient.setIngredientAmount(ingredientAmount);
                    recipeIngredient.setUnit(unit);

                    recipeIngredientRepository.save(recipeIngredient);
                }
            }
        } catch (Exception e) {
            System.err.println("Bestand kon niet geopend worden.");
            System.err.println(e.getMessage());
        }

    }
}
