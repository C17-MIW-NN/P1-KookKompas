package nl.miw.ch17.mmadevforce.kookkompas.controller;

import nl.miw.ch17.mmadevforce.kookkompas.model.Ingredient;
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
        if (ingredientRepository.count() == 0) {
            initializeDB();
        }
    }

    private void initializeDB() {
        ArrayList<Ingredient> ingredients = new ArrayList<>();

        ClassPathResource ingredientBestand = new ClassPathResource("static/IngredientList.txt");

        try (Scanner invoer = new Scanner(ingredientBestand.getInputStream())) {
            while (invoer.hasNextLine()) {
                String naam = invoer.nextLine().trim();
                if (!naam.isEmpty()) {
                    ingredients.add(new Ingredient(naam));
                }
            }
        } catch (Exception e) {
            System.err.println("Bestand kon niet geopend worden.");
            System.err.println(e.getMessage());
        }

        ingredientRepository.saveAll(ingredients);

    }


}
