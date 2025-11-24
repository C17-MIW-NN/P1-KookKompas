package nl.miw.ch17.mmadevforce.kookkompas.service;

import nl.miw.ch17.mmadevforce.kookkompas.model.*;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

/**
 * @author MMA Dev Force
 * Managing functionalities from initialize
 */
@Service
public class InitializeService {
    private final IngredientRepository ingredientRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final RecipeRepository recipeRepository;
    private final RecipeStepRepository recipeStepRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;
    private final KookKompasUserService kookKompasUserService;

    public InitializeService(IngredientRepository ingredientRepository,
                             RecipeIngredientRepository recipeIngredientRepository,
                             RecipeRepository recipeRepository,
                             RecipeStepRepository recipeStepRepository,
                             CategoryRepository categoryRepository, CategoryService categoryService, KookKompasUserService kookKompasUserService) {
        this.ingredientRepository = ingredientRepository;
        this.recipeIngredientRepository = recipeIngredientRepository;
        this.recipeRepository = recipeRepository;
        this.recipeStepRepository = recipeStepRepository;
        this.categoryRepository = categoryRepository;
        this.categoryService = categoryService;
        this.kookKompasUserService = kookKompasUserService;
    }

    public void seedDatabaseIfEmpty() {
        if (ingredientRepository.count() == 0 && recipeRepository.count() == 0) {
            initializeDB();
        }
    }

    public void initializeDB() {
        makeUser("Admin", "AdminPW");

        loadCSVFileIngredientList();
        loadCSVFileRecipeSteps();
        loadCSVFileRecipeCategory();
        loadCSVFileCategoryList();
    }

    private KookKompasUser makeUser(String username, String password) {
        KookKompasUser user = new KookKompasUser();
        user.setUsername(username);
        user.setPassword(password);

        kookKompasUserService.saveUser(user);
        return user;
    }

    private void loadCSVFileCategoryList() {
        ClassPathResource categoryListFile = new ClassPathResource("static/CategoryList.csv");
        try (Scanner input = new Scanner(categoryListFile.getInputStream())) {
            if (input.hasNextLine()) input.nextLine();

            while (input.hasNextLine()) {
                String line = input.nextLine().trim();
                if (!line.isEmpty()) {
                    importCSVFileCategoryList(line);
                }
            }

        } catch (Exception exception) {
            System.err.println("Categorielijst bestand kon niet geopend worden.");
            System.err.println(exception.getMessage());
        }
    }


    public Category importCSVFileCategoryList(String line) {
        String[] parts = line.split(",", 2);
        String categoryName = parts[0].trim();
        String categoryColor = parts[1].trim();

        Optional<Category> optionalCategory = categoryService.findByCategoryName(categoryName);

        Category category;
        if (optionalCategory.isPresent()) {
            // Bestaande categorie updaten
            category = optionalCategory.get();
        } else {
            // Nieuwe categorie aanmaken
            category = new Category();
            category.setCategoryName(categoryName);
        }

        category.setCategoryColor(categoryColor);
        categoryService.saveCategory(category);
        return category;
    }

    private void loadCSVFileRecipeCategory() {
        ClassPathResource categoryFile = new ClassPathResource("static/RecipeCategory.csv");
        try (Scanner input = new Scanner(categoryFile.getInputStream())) {
            if (input.hasNextLine()) input.nextLine();

            while (input.hasNextLine()) {
                String line = input.nextLine().trim();
                if (!line.isEmpty()) {
                    importCSVFileRecipeCategory(line);
                }
            }

        } catch (Exception exception) {
            System.err.println("Categorie bestand kon niet geopend worden.");
            System.err.println(exception.getMessage());
        }
    }


    private void importCSVFileRecipeCategory(String line) {
        String[] parts = line.split(",", 4);
        String recipeTitle = parts[0].trim();
        String imgUrl = parts[1].replaceAll("^\"|\"$", "").trim();
        String categoriesString = parts[2].replaceAll("^\"|\"$", "").trim();
        String description = parts[3].replaceAll("^\"|\"$", "").trim();

        Recipe recipe = recipeRepository.findByTitle(recipeTitle)
                .orElseGet(() -> recipeRepository.save(new Recipe(recipeTitle)));

        recipe.setDescription(description);
        recipe.setCoverImageUrl(imgUrl);

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

    private void loadCSVFileRecipeSteps() {
        ClassPathResource stepsBestand = new ClassPathResource("static/RecipeSteps.csv");

        try (Scanner invoer = new Scanner(stepsBestand.getInputStream())) {
            if (invoer.hasNextLine()) invoer.nextLine();

            while (invoer.hasNextLine()) {
                String line = invoer.nextLine().trim();
                if (!line.isEmpty()) {
                    importCSVFileRecipeSteps(line);
                }
            }

        } catch (Exception e) {
            System.err.println("Stappenbestand kon niet geopend worden.");
            System.err.println(e.getMessage());
        }
    }

    private void importCSVFileRecipeSteps(String line) {
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

    private void loadCSVFileIngredientList() {
        ClassPathResource ingredientBestand = new ClassPathResource("static/IngredientList.csv");

        try (Scanner invoer = new Scanner(ingredientBestand.getInputStream())) {
            if (invoer.hasNextLine()) invoer.nextLine();

            while (invoer.hasNextLine()) {
                String regel = invoer.nextLine().trim();
                if (!regel.isEmpty()) {
                    importCSVFileIngredientList(regel);
                }
            }
        } catch (Exception e) {
            System.err.println("Bestand kon niet geopend worden.");
            System.err.println(e.getMessage());
        }
    }

    private void importCSVFileIngredientList(String regel) {
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
