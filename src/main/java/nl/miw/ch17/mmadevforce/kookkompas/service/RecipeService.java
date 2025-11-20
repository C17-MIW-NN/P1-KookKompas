package nl.miw.ch17.mmadevforce.kookkompas.service;

import nl.miw.ch17.mmadevforce.kookkompas.model.*;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.CategoryRepository;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.IngredientRepository;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.RecipeRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.*;

/**
 * @author MAA Dev Force
 * Managing functionalities from the recipes
 */

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final CategoryRepository categoryRepository;
    private final IngredientRepository ingredientRepository;

    public RecipeService(RecipeRepository recipeRepository, CategoryRepository categoryRepository, IngredientRepository ingredientRepository) {
        this.recipeRepository = recipeRepository;
        this.categoryRepository = categoryRepository;
        this.ingredientRepository = ingredientRepository;
    }

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    public List<Recipe> searchByTitle(String query) {
        return recipeRepository.findByTitleContainingIgnoreCase(query);
    }

    public List<Ingredient> getAllIngredients() {
        return ingredientRepository.findAll();
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public void deleteRecipe(Long id) {
        recipeRepository.deleteById(id);
    }

    public Optional<Recipe> getRecipeWithIngredientsByTitle(String title) {
        Optional<Recipe> optionalRecipe = recipeRepository.findByTitle(title);

        if (optionalRecipe.isPresent()) {
            Recipe recipe = optionalRecipe.get();
            recipe.getRecipeingredients().size();
            recipe.getSteps().size();

            List<Ingredient> allIngredients = ingredientRepository.findAll();
            List<RecipeIngredient> recipeIngredients = new ArrayList<>();

            for (Ingredient ing : allIngredients) {
                // kijken of dit ingrediënt al in het recept zit
                RecipeIngredient existing = recipe.getRecipeingredients().stream()
                        .filter(ri -> ri.getIngredient() != null &&
                                ri.getIngredient().getIngredientId().equals(ing.getIngredientId()))
                        .findFirst()
                        .orElse(null);

                if (existing != null) {
                    existing.setIngredient(ing);
                    recipeIngredients.add(existing); // bestaande hoeveelheid + eenheid
                } else {
                    RecipeIngredient ri = new RecipeIngredient();
                    ri.setIngredient(null); // niet gekozen
                    ri.setIngredientAmount(null);
                    ri.setUnit(null);
                    recipeIngredients.add(ri);
                }
            }
            recipe.setRecipeingredients(recipeIngredients);
        }
        return optionalRecipe;
    }

    public Recipe saveOrUpdateRecipe(Recipe recipeFromForm) {

        Recipe recipeToBeSaved;
        if (recipeFromForm.getRecipeId() != null) {
            recipeToBeSaved = recipeRepository.findById(recipeFromForm.getRecipeId()).orElseThrow();
            recipeToBeSaved.setTitle(recipeFromForm.getTitle());
            recipeToBeSaved.setDescription(recipeFromForm.getDescription());
            recipeToBeSaved.setCoverImageUrl(recipeFromForm.getCoverImageUrl());
        } else {
            recipeToBeSaved = new Recipe();
            recipeToBeSaved.setTitle(recipeFromForm.getTitle());
            recipeToBeSaved.setDescription(recipeFromForm.getDescription());
            recipeToBeSaved.setCoverImageUrl(recipeFromForm.getCoverImageUrl());
        }

        Set<Category> categories = new HashSet<>();
        if (recipeFromForm.getCategories() != null) {
            for (Category c : recipeFromForm.getCategories()) {
                Category category = categoryRepository.findById(c.getCategoryId())
                        .orElseThrow(() -> new RuntimeException("Category not found: " + c.getCategoryId()));
                categories.add(category);
            }
        }

        recipeToBeSaved.setCategories(categories);

        if (recipeToBeSaved.getRecipeingredients() == null) {
            recipeToBeSaved.setRecipeingredients(new ArrayList<>());
        } else {
            recipeToBeSaved.getRecipeingredients().clear();
        }

        recipeToBeSaved.getRecipeingredients().clear();
        if (recipeFromForm.getRecipeingredients() != null) {
            for (RecipeIngredient recipeIngredient : recipeFromForm.getRecipeingredients()) {
                if (recipeIngredient.getIngredient() == null) continue;
                recipeIngredient.setRecipe(recipeToBeSaved);
                recipeToBeSaved.getRecipeingredients().add(recipeIngredient);
            }
        }

        // Huidige stappen wissen
        recipeToBeSaved.getSteps().clear();

        // Nieuwe stappen uit formulier toevoegen
        if (recipeFromForm.getSteps() != null) {
            int stepNum = 1;
            for (RecipeStep s : recipeFromForm.getSteps()) {
                // lege stappen overslaan
                if (s.getStepDescription() == null || s.getStepDescription().isBlank()) continue;

                RecipeStep newStep = new RecipeStep();
                newStep.setStepDescription(s.getStepDescription());
                newStep.setStepNumber(stepNum++);
                newStep.setRecipe(recipeToBeSaved);

                recipeToBeSaved.getSteps().add(newStep);
            }
        }

        // Recipe opslaan
        return recipeRepository.save(recipeToBeSaved);
    }

    public Recipe getRecipeByTitle(String title) {
        return recipeRepository.findByTitle(title)
                .orElseThrow(() -> new RuntimeException("Recipe not found: " + title));
    }

    // scaledIngredients berekenen
    public List<RecipeIngredient> getScaledIngredients(Recipe recipe, int servings) {
        return recipe.getRecipeingredients().stream()
                .map(ri -> {
                    Double amount = ri.getIngredientAmount();
                    double scaled = (amount != null ? amount : 0.0) * servings / recipe.getServings();

                    RecipeIngredient copy = new RecipeIngredient();
                    copy.setIngredient(ri.getIngredient());
                    copy.setUnit(ri.getUnit());
                    copy.setIngredientAmount(scaled);
                    return copy;
                })
                .toList();
    }

    public Set<Recipe> searchRecipes(String query) {
        List<Recipe> titleResults = recipeRepository.findByTitleContainingIgnoreCase(query);
        List<Recipe> ingredientResults =
                recipeRepository.findDistinctByRecipeingredients_Ingredient_NameIgnoreCase(query);

        Set<Recipe> results = new HashSet<>();
        results.addAll(titleResults);
        results.addAll(ingredientResults);

        return results;
    }

    public int increaseServings(int currentServings) {
        return currentServings + 1;
    }

    public int decreaseServings(int currentServings) {
        if (currentServings > 1) {
            return currentServings - 1;
        }
        return currentServings;
    }

}