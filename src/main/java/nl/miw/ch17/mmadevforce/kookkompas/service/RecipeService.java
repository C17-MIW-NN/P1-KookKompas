package nl.miw.ch17.mmadevforce.kookkompas.service;

import nl.miw.ch17.mmadevforce.kookkompas.model.*;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.CategoryRepository;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.IngredientRepository;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.KookKompasUserRepository;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.RecipeRepository;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author MAA Dev Force
 * Managing functionalities from the recipes
 */

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final CategoryRepository categoryRepository;
    private final IngredientRepository ingredientRepository;

    public RecipeService(RecipeRepository recipeRepository
            , CategoryRepository categoryRepository
            , IngredientRepository ingredientRepository) {
        this.recipeRepository = recipeRepository;
        this.categoryRepository = categoryRepository;
        this.ingredientRepository = ingredientRepository;
    }

    public List<Recipe> getAllRecipesForUser(KookKompasUser user) {
        Long userId = user.getUserId();
        return recipeRepository.findAll().stream()
                .filter(recipe -> recipe.isPublicVisible() ||
                        (recipe.getOwner() != null && recipe.getOwner().getUserId() != null &&
                                recipe.getOwner().getUserId().equals(userId)))
                .collect(Collectors.toList());
    }

    public List<Recipe> getAllPublicRecipes() {
        return recipeRepository.findAll().stream()
                .filter(Recipe::isPublicVisible)
                .collect(Collectors.toList());
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

    public Optional<Recipe> getRecipeWithIngredientsAndCategoriesByTitle(String title) {
        Optional<Recipe> optionalRecipe = loadRecipeWithRelations(title);
        if (optionalRecipe.isEmpty()) return optionalRecipe;

        Recipe recipe = optionalRecipe.get();
        List<RecipeIngredient> formIngredients = buildRecipeIngredientFormList(recipe);
        applyFormIngredientsToRecipe(recipe, formIngredients);

        return optionalRecipe;
    }

    public Recipe saveOrUpdateRecipe(Recipe recipeFromForm) {
        Recipe recipeToBeSaved = loadOrCreateRecipe(recipeFromForm);
        assignOwner(recipeToBeSaved, recipeFromForm);

        syncCategories(recipeToBeSaved, recipeFromForm.getCategories());
        syncIngredients(recipeToBeSaved, recipeFromForm.getRecipeingredients());
        syncSteps(recipeToBeSaved, recipeFromForm.getSteps());

        return recipeRepository.save(recipeToBeSaved);
    }


    public Recipe getRecipeByTitle(String title) {
        return recipeRepository.findByTitle(title)
                .orElseThrow(() -> new RuntimeException("Recipe not found: " + title));
    }

    // scaledIngredients berekenen
    public List<RecipeIngredient> getScaledIngredients(Recipe recipe, int servings) {
        double originalServings = recipe.getServings();
        return recipe.getRecipeingredients().stream()
                .map(ri -> {
                    RecipeIngredient copy = new RecipeIngredient();
                    copy.setIngredient(ri.getIngredient());
                    copy.setUnit(ri.getUnit());
                    Double baseAmount = ri.getIngredientAmount() != null ? ri.getIngredientAmount() : 0.0;
                    //double scaled = (amount != null ? amount : 0.0) * servings / recipe.getServings();

                    copy.setIngredientAmount(baseAmount * servings / originalServings);
                    return copy;
                })
                .toList();
    }

    public Set<Recipe> searchRecipes(String query) {
        List<Recipe> titleResults = recipeRepository.findByTitleContainingIgnoreCase(query);
        List<Recipe> ingredientResults =
                recipeRepository.findDistinctByRecipeingredients_Ingredient_IngredientNameIgnoreCase(query);

        Set<Recipe> results = new HashSet<>();
        results.addAll(titleResults);
        results.addAll(ingredientResults);

        return results;
    }

    private Optional<Recipe> loadRecipeWithRelations(String title) {
        Optional<Recipe> optionalRecipe = recipeRepository.findByTitle(title);

        if (optionalRecipe.isEmpty()) return Optional.empty();

        Recipe recipe = optionalRecipe.get();
        recipe.getRecipeingredients().size();
        recipe.getSteps().size();

        return optionalRecipe;
    }

    private List<RecipeIngredient> buildRecipeIngredientFormList(Recipe recipe) {
        List<Ingredient> allIngredients = ingredientRepository.findAll();
        List<RecipeIngredient> result = new ArrayList<>();

        for (Ingredient ing : allIngredients) {
            RecipeIngredient existing = recipe.getRecipeingredients().stream()
                    .filter(ri -> ri.getIngredient() != null &&
                            ri.getIngredient().getIngredientId().equals(ing.getIngredientId()))
                    .findFirst()
                    .orElse(null);

            if (existing != null) {
                result.add(existing);
            } else {
                RecipeIngredient empty = new RecipeIngredient();
                empty.setIngredient(null);
                empty.setIngredientAmount(null);
                empty.setUnit(null);
                result.add(empty);
            }
        }

        return result;
    }

    public int increaseServings(int currentServings) {
        return currentServings + 1;
    }

    public int decreaseServings(int currentServings) {
        if (currentServings > 1) {
            return currentServings - 1;
        }
        if (currentServings <= 1) {
            throw new IllegalArgumentException("Aantal kan niet lager dan 1");
        }
        return currentServings - 1;
    }

    private void applyFormIngredientsToRecipe(Recipe recipe, List<RecipeIngredient> formList) {
        recipe.setRecipeingredients(formList);
    }

    private Recipe loadOrCreateRecipe(Recipe form) {
        if (form.getRecipeId() != null) {
            Recipe existing = recipeRepository.findById(form.getRecipeId())
                    .orElseThrow(() -> new RuntimeException("Recipe not found: " + form.getRecipeId()));

            existing.setTitle(form.getTitle());
            existing.setDescription(form.getDescription());
            existing.setCoverImageUrl(form.getCoverImageUrl());
            existing.setPublicVisible(form.isPublicVisible());
            return existing;
        }

        Recipe newRecipe = new Recipe();
        newRecipe.setTitle(form.getTitle());
        newRecipe.setDescription(form.getDescription());
        newRecipe.setCoverImageUrl(form.getCoverImageUrl());
        newRecipe.setPublicVisible(form.isPublicVisible());
        return newRecipe;
    }

    private void assignOwner(Recipe target, Recipe form) {
        if (form.getOwner() != null) {
            target.setOwner(form.getOwner());
        } else if (target.getOwner() == null) {
            throw new RuntimeException("Owner must be set before saving a recipe");
        }
    }

    private void syncCategories(Recipe target, Set<Category> categoriesFromForm) {
        Set<Category> categories = new HashSet<>();
        if (categoriesFromForm != null) {
            for (Category c : categoriesFromForm) {
                Category cat = categoryRepository.findById(c.getCategoryId())
                        .orElseThrow(() -> new RuntimeException("Category not found: " + c.getCategoryId()));
                categories.add(cat);
            }
        }
        target.setCategories(categories);
    }

    private void syncIngredients(Recipe target, List<RecipeIngredient> ingredientsFromForm) {
        if (target.getRecipeingredients() == null) {
            target.setRecipeingredients(new ArrayList<>());
        } else {
            target.getRecipeingredients().clear();
        }

        if (ingredientsFromForm == null) return;

        for (RecipeIngredient ri : ingredientsFromForm) {
            if (ri.getIngredient() == null) continue;
            ri.setRecipe(target);
            target.getRecipeingredients().add(ri);
        }
    }

    private void syncSteps(Recipe target, List<RecipeStep> stepsFromForm) {
        target.getSteps().clear();
        if (stepsFromForm == null) return;

        int stepNum = 1;
        for (RecipeStep s : stepsFromForm) {
            if (s.getStepDescription() == null || s.getStepDescription().isBlank()) continue;

            RecipeStep newStep = new RecipeStep();
            newStep.setStepDescription(s.getStepDescription());
            newStep.setStepNumber(stepNum++);
            newStep.setCookingTimePerStep(s.getCookingTimePerStep());
            newStep.setRecipe(target);

            target.getSteps().add(newStep);
        }
    }
}