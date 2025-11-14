package nl.miw.ch17.mmadevforce.kookkompas.controller;

import nl.miw.ch17.mmadevforce.kookkompas.model.*;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.*;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;

import java.util.*;

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
    private final CategoryRepository categoryRepository;


    public InitializeController(IngredientRepository ingredientRepository,
                                RecipeIngredientRepository recipeIngredientRepository,
                                RecipeRepository recipeRepository,
                                RecipeStepRepository recipeStepRepository,
                                CategoryRepository categoryRepository) {
        this.ingredientRepository = ingredientRepository;
        this.recipeIngredientRepository = recipeIngredientRepository;
        this.recipeRepository = recipeRepository;
        this.recipeStepRepository = recipeStepRepository;
        this.categoryRepository = categoryRepository;
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

        ClassPathResource categoryFile = new ClassPathResource("static/RecipeCategory.csv");
        try (Scanner input = new Scanner(categoryFile.getInputStream())) {
            if (input.hasNextLine()) input.nextLine();

            while (input.hasNextLine()) {
                String line = input.nextLine().trim();
                if (!line.isEmpty()) {
                    String[] parts = line.split(",", 3);
                    String recipeTitle = parts[0].trim();
                    String categoriesString = parts[1].replaceAll("^\"|\"$", "").trim();
                    String description = parts[2].replaceAll("^\"|\"$", "").trim();

                    Recipe recipe = recipeRepository.findByTitle(recipeTitle)
                            .orElseGet(() -> recipeRepository.save(new Recipe(recipeTitle)));

                    recipe.setDescription(description);

                    Set<Category> categorySet = new HashSet<>();

                    String[] categoryNames = categoriesString.split(";");
                    for (String categoryName : categoryNames) {
                        String cleanName = categoryName.trim();
                        Category category = categoryRepository.findByCategoryName(cleanName)
                                .orElseGet(() -> categoryRepository.save(new Category(categoryName)));
                    categorySet.add(category);
                    }

                    recipe.setCategories(categorySet);
                    recipeRepository.save(recipe);
                }
            }

        } catch (Exception exception) {
            System.err.println("Categorie bestand kon niet geopend worden.");
            System.err.println(exception.getMessage());
        }

    }
}
