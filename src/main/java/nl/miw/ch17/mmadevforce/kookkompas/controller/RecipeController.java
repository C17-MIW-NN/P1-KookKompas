package nl.miw.ch17.mmadevforce.kookkompas.controller;

import nl.miw.ch17.mmadevforce.kookkompas.model.*;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.CategoryRepository;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.IngredientRepository;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.RecipeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Arjen Zijlstra
 *
 */

@Controller
public class RecipeController {

    private final RecipeRepository recipeRepository;
    private final CategoryRepository categoryRepository;
    private final IngredientRepository ingredientRepository;

    public RecipeController(RecipeRepository recipeRepository, CategoryRepository categoryRepository, IngredientRepository ingredientRepository) {
        this.recipeRepository = recipeRepository;
        this.categoryRepository = categoryRepository;
        this.ingredientRepository = ingredientRepository;
    }

    @GetMapping({"/recipe/all", "/"})
    private String showRecipeOverview(Model datamodel) {

        datamodel.addAttribute("recipes", recipeRepository.findAll());

        return "recipeOverview";
    }

    @GetMapping("/recipe/add")
    public String showRecipeForm(Model datamodel) {

        Recipe recipe = new Recipe();

        // Voor elk ingrediënt een lege RecipeIngredient toevoegen
        List<Ingredient> allIngredients = ingredientRepository.findAll();
        List<RecipeIngredient> recipeIngredients = new ArrayList<>();
        for (Ingredient ing : allIngredients) {
            RecipeIngredient ri = new RecipeIngredient();
            ri.setIngredient(null); // nog niet gekozen
            ri.setIngredientAmount(null);
            ri.setUnit(null);
            recipeIngredients.add(ri);
        }
        recipe.setRecipeingredients(recipeIngredients);


        return showRecipeForm(datamodel, recipe);
    }

    @GetMapping("/recipe/edit/{title}")
    public String showEditRecipeForm(@PathVariable("title") String title, Model datamodel) {

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

            return showRecipeForm(datamodel, recipe);
        }

        return "redirect:/recipe/all";
    }

    private String showRecipeForm(Model datamodel, Recipe recipe) {
        datamodel.addAttribute("formRecipe", recipe);
        datamodel.addAttribute("allCategories", categoryRepository.findAll());
        datamodel.addAttribute("ingredients", ingredientRepository.findAll());
        return "recipeForm";
    }

    @PostMapping("/recipe/save")
    public String saveOrUpdateRecipe(
            @ModelAttribute("formRecipe") Recipe recipeFromForm) {

        // Bestaand recept ophalen of nieuw recept aanmaken
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
        recipeRepository.save(recipeToBeSaved);

        return "redirect:/recipe/all";
    }

    @GetMapping("/recipe/delete/{recipeId}")
    public String deleteRecipe(@PathVariable("recipeId") Long recipeId) {
        recipeRepository.deleteById(recipeId);

        return "redirect:/recipe/all";
    }


    @GetMapping("/recipe/detail/{title}")
    public String showRecipeDetailpage(@PathVariable("title") String title,
                                       @RequestParam(required = false) Integer servings,
                                       Model model) {
        Recipe recipe = recipeRepository.findByTitle(title)
                .orElseThrow(() -> new RuntimeException("Recipe not found: " + title));

        // Default servings uit recept, of queryparameter
        int currentServings = (servings != null) ? servings : recipe.getServings();

        model.addAttribute("recipe", recipe);
        model.addAttribute("currentServings", currentServings);

        // scaledIngredients berekenen
        List<RecipeIngredient> scaledIngredients = recipe.getRecipeingredients().stream()
                .map(ri -> {
                    Double amount = ri.getIngredientAmount();
                    double scaled = (amount != null ? amount : 0.0) * currentServings / recipe.getServings();

                    RecipeIngredient copy = new RecipeIngredient();
                    copy.setIngredient(ri.getIngredient());
                    copy.setUnit(ri.getUnit());
                    copy.setIngredientAmount(scaled);
                    return copy;
                }).toList();

        model.addAttribute("scaledIngredients", scaledIngredients);

        return "recipeDetails";
    }

    @GetMapping("/recipe/search")
    public String searchRecipes(@RequestParam("query") String query, Model datamodel) {
        List<Recipe> titleResults = recipeRepository.findByTitleContainingIgnoreCase(query);
        List<Recipe> ingredientResults =
                recipeRepository.findDistinctByRecipeingredients_Ingredient_NameIgnoreCase(query);
        Set<Recipe> results = new HashSet<>();
        results.addAll(titleResults);
        results.addAll(ingredientResults);
        datamodel.addAttribute("recipes", results);
        datamodel.addAttribute("query", query);
        return "recipeSearchResults";
    }

    @PostMapping("/recipe/detail/{title}/decrease")
    public String decrease(@PathVariable("title") String title, @RequestParam int currentServings) {
        if (currentServings > 1) {
            currentServings--;
        }
        return "redirect:/recipe/detail/" + title + "?servings=" + currentServings;
    }

    @PostMapping("/recipe/detail/{title}/increase")
    public String increase(@PathVariable("title") String title, @RequestParam int currentServings) {
        return "redirect:/recipe/detail/" + title + "?servings=" + (currentServings + 1);
    }

}
