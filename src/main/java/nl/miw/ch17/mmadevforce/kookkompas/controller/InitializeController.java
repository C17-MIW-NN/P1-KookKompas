package nl.miw.ch17.mmadevforce.kookkompas.controller;

import nl.miw.ch17.mmadevforce.kookkompas.model.Ingredient;
import nl.miw.ch17.mmadevforce.kookkompas.model.Recipe;
import nl.miw.ch17.mmadevforce.kookkompas.model.RecipeIngredient;
import nl.miw.ch17.mmadevforce.kookkompas.model.RecipeStep;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.IngredientRepository;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.RecipeIngredientRepository;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.RecipeRepository;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.RecipeStepRepository;
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
    private final RecipeStepRepository recipeStepRepository;


    public InitializeController(IngredientRepository ingredientRepository, RecipeIngredientRepository recipeIngredientRepository, RecipeRepository recipeRepository, RecipeStepRepository recipeStepRepository) {
        this.ingredientRepository = ingredientRepository;
        this.recipeIngredientRepository = recipeIngredientRepository;
        this.recipeRepository = recipeRepository;
        this.recipeStepRepository = recipeStepRepository;
    }

    @EventListener
    private void seed(ContextRefreshedEvent ignoredEvent) {
        if (ingredientRepository.count() == 0 && recipeRepository.count() == 0) {
            initializeDB();
        }
    }

    private void initializeDB() {

        ClassPathResource ingredientBestand = new ClassPathResource("static/IngredientList.csv");

        try (Scanner invoer = new Scanner(ingredientBestand.getInputStream())) {
            if (invoer.hasNextLine()) invoer.nextLine();

            while (invoer.hasNextLine()) {
                String regel = invoer.nextLine().trim();
                if (!regel.isEmpty()) {
                    String[] parts = regel.split(",");
                    String recipeTitle = parts[0];
                    String name = parts[1];
                    double ingredientAmount = Double.parseDouble(parts[2]);
                    String unit = parts[3];

                    Recipe recipe = recipeRepository.findByTitle(recipeTitle)
                            .orElseGet(() -> recipeRepository.save(new Recipe(recipeTitle)));

                    Ingredient ingredient = ingredientRepository.findByName(name)
                            .orElseGet(() -> ingredientRepository.save(new Ingredient(name)));

                    RecipeIngredient recipeIngredient = new RecipeIngredient();
                    recipeIngredient.setIngredient(ingredient);
                    recipeIngredient.setRecipe(recipe);
                    recipeIngredient.setIngredientAmount(ingredientAmount);
                    recipeIngredient.setUnit(unit);

                    recipeIngredientRepository.save(recipeIngredient);
                }
            }
        } catch (Exception e) {
            System.err.println("Bestand kon niet geopend worden.");
            System.err.println(e.getMessage());
        }

        ClassPathResource stepsBestand = new ClassPathResource("static/RecipeSteps.csv");

        try (Scanner invoer = new Scanner(stepsBestand.getInputStream())) {
            if (invoer.hasNextLine()) invoer.nextLine();

            while (invoer.hasNextLine()) {
                String line = invoer.nextLine().trim();
                if (!line.isEmpty()) {
                    String[] parts = line.split(",");
                    String recipeTitle = parts[0];
                    int stepNumber = Integer.parseInt(parts[1]);
                    String stepDescription = parts[2];

                    Recipe recipe = recipeRepository.findByTitle(recipeTitle)
                            .orElseThrow(() -> new IllegalArgumentException("Recipe not found: " + recipeTitle));

                    RecipeStep step = new RecipeStep();
                    step.setRecipe(recipe);
                    step.setStepNumber(stepNumber);
                    step.setStepDescription(stepDescription);

                    recipeStepRepository.save(step);
                }
            }

        } catch (Exception e) {
            System.err.println("Stappenbestand kon niet geopend worden.");
            System.err.println(e.getMessage());
        }

    }
}
