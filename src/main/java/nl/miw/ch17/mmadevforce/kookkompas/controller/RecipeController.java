package nl.miw.ch17.mmadevforce.kookkompas.controller;

import nl.miw.ch17.mmadevforce.kookkompas.model.*;
import nl.miw.ch17.mmadevforce.kookkompas.service.CategoryService;
import nl.miw.ch17.mmadevforce.kookkompas.service.ImageService;
import nl.miw.ch17.mmadevforce.kookkompas.service.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

/**
 * @author MMA Dev Force
 * Handles requests regarding recipe categories
 */

@Controller
public class RecipeController {

    private final RecipeService recipeService;
    private final CategoryService categoryService;
    private final ImageService imageService;

    public RecipeController(RecipeService recipeService, CategoryService categoryService, ImageService imageService) {
        this.recipeService = recipeService;
        this.categoryService = categoryService;
        this.imageService = imageService;
    }

    @GetMapping({"/recipe/all"})
    private String showRecipeOverview(Model datamodel) {

        datamodel.addAttribute("recipes", recipeService.getAllRecipes());
        datamodel.addAttribute("categories", categoryService.findAllCategories());

        return "recipeOverview";
    }

    @GetMapping("/recipe/add")
    public String showRecipeForm(Model datamodel) {

        Recipe recipe = new Recipe();

        // Voor elk ingrediënt een lege RecipeIngredient toevoegen
        List<Ingredient> allIngredients = recipeService.getAllIngredients();
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
        Optional<Recipe> optionalRecipe = recipeService.getRecipeWithIngredientsAndCategoriesByTitle(title);

        if (optionalRecipe.isPresent()) {
            return showRecipeForm(datamodel, optionalRecipe.get());
        }

        return "redirect:/recipe/all";
    }

    private String showRecipeForm(Model datamodel, Recipe recipe) {
        datamodel.addAttribute("formRecipe", recipe);
        datamodel.addAttribute("allCategories", recipeService.getAllCategories());
        datamodel.addAttribute("ingredients", recipeService.getAllIngredients());
        return "recipeForm";
    }

    @PostMapping("/recipe/save")
    public String saveOrUpdateRecipe(@ModelAttribute("formRecipe") Recipe recipeFromForm,
                                     BindingResult result,
                                     @RequestParam MultipartFile coverImageFile) {

        try {
            if (coverImageFile != null && !coverImageFile.isEmpty()) {
                imageService.saveImage(coverImageFile);
                recipeFromForm.setCoverImageUrl("/image/" + coverImageFile.getOriginalFilename());
            } else if (recipeFromForm.getCoverImageUrl() != null && !recipeFromForm.getCoverImageUrl().isBlank()) {

            } else {
                recipeFromForm.setCoverImageUrl("/images/default.png");
            }
        } catch (IOException imageError) {
            result.rejectValue("coverImageFile", "imageNotSaved", "Afbeelding niet opgeslagen");
        }

        if (result.hasErrors()) {
            return "recipeForm";
        }

        recipeService.saveOrUpdateRecipe(recipeFromForm);
        return "redirect:/recipe/all";
    }


    @GetMapping("/recipe/delete/{recipeId}")
    public String deleteRecipe(@PathVariable("recipeId") Long recipeId) {
        recipeService.deleteRecipe(recipeId);
        return "redirect:/recipe/all";
    }

    @GetMapping("/recipe/detail/{title}")
    public String showRecipeDetailpage(@PathVariable("title") String title,
                                       @RequestParam(required = false) Integer servings,
                                       Model model) {

        Recipe recipe = recipeService.getRecipeByTitle(title);

        // Default servings uit recept, of queryparameter
        int currentServings = (servings != null) ? servings : recipe.getServings();

        List<RecipeIngredient> scaledIngredients = recipeService.getScaledIngredients(recipe, currentServings);

        model.addAttribute("recipe", recipe);
        model.addAttribute("currentServings", currentServings);
        model.addAttribute("scaledIngredients", scaledIngredients);

        return "recipeDetails";
    }

    @GetMapping("/recipe/search")
    public String searchRecipes(@RequestParam("query") String query, Model datamodel) {
        Set<Recipe> results = recipeService.searchRecipes(query);
        datamodel.addAttribute("recipes", results);
        datamodel.addAttribute("query", query);
        return "recipeSearchResults";
    }

    @PostMapping("/recipe/detail/{title}/increase")
    public String increase(@PathVariable("title") String title,
                           @RequestParam int currentServings) {

        int updatedServings = recipeService.increaseServings(currentServings);

        return "redirect:/recipe/detail/" + title + "?servings=" + updatedServings;
    }

    @PostMapping("/recipe/detail/{title}/decrease")
    public String decrease(@PathVariable("title") String title,
                           @RequestParam int currentServings, Model model) {

        //int updatedServings = recipeService.decreaseServings(currentServings);
        int updatedServings;

        try {
            updatedServings = recipeService.decreaseServings(currentServings);
        } catch (IllegalArgumentException exception) {
            model.addAttribute("errorMessage", exception.getMessage());
            return showRecipeDetailpage(title, currentServings, model);
        }
        return "redirect:/recipe/detail/" + title + "?servings=" + updatedServings;
    }
}