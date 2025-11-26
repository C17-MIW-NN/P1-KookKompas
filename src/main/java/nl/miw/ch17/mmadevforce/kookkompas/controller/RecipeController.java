package nl.miw.ch17.mmadevforce.kookkompas.controller;

import nl.miw.ch17.mmadevforce.kookkompas.model.*;
import nl.miw.ch17.mmadevforce.kookkompas.service.CategoryService;
import nl.miw.ch17.mmadevforce.kookkompas.service.ImageService;
import nl.miw.ch17.mmadevforce.kookkompas.service.KookKompasUserService;
import nl.miw.ch17.mmadevforce.kookkompas.service.RecipeService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
@RequestMapping("/recipe")
public class RecipeController {
    private final RecipeService recipeService;
    private final CategoryService categoryService;
    private final ImageService imageService;
    private final KookKompasUserService kookKompasUserService;

    public RecipeController(RecipeService recipeService, CategoryService categoryService, ImageService imageService, KookKompasUserService kookKompasUserService) {
        this.recipeService = recipeService;
        this.categoryService = categoryService;
        this.imageService = imageService;
        this.kookKompasUserService = kookKompasUserService;
    }

    @GetMapping({"/all"})
    private String showRecipeOverview(Model datamodel) {
        KookKompasUser user = getCurrentUser();

        addRecipesToModel(datamodel, user);

        datamodel.addAttribute("categories", categoryService.findAllCategories());

        if (user != null) {
            datamodel.addAttribute("categories", categoryService.getAllCategoriesForUser(user));
        } else {
            datamodel.addAttribute("categories", categoryService.getAllPublicCategories());
        }

        return "recipeOverview";
    }

    @GetMapping("/add")
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

    @GetMapping("/edit/{title}")
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

    @PostMapping("/save")
    public String saveOrUpdateRecipe(@ModelAttribute("formRecipe") Recipe recipeFromForm,
                                     BindingResult result,
                                     @RequestParam MultipartFile coverImageFile) {

        processCoverImage(recipeFromForm, coverImageFile, result);
        if (result.hasErrors()) {
            return "recipeForm";
        }

        KookKompasUser currentUser = kookKompasUserService.getLoggedInUser();
        if (recipeFromForm.getOwner() == null) {
            recipeFromForm.setOwner(currentUser);
        }

        recipeService.saveOrUpdateRecipe(recipeFromForm);
        return "redirect:/recipe/all";
    }

    @GetMapping("/delete/{recipeId}")
    public String deleteRecipe(@PathVariable("recipeId") Long recipeId) {
        recipeService.deleteRecipe(recipeId);
        return "redirect:/recipe/all";
    }

    @GetMapping("/detail/{title}")
    public String showRecipeDetailpage(@PathVariable("title") String title,
                                       @RequestParam(required = false) Integer servings,
                                       Model model) {

        Recipe recipe = recipeService.getRecipeByTitle(title);
        KookKompasUser user = getCurrentUser();

        if (!userCanAccess(recipe, user)) {
            return "redirect:/recipe/all";
        }

        int currentServings = determineServings(recipe, servings);
        List<RecipeIngredient> scaled = recipeService.getScaledIngredients(recipe, currentServings);

        addRecipeDetailModelAttributes(model, recipe, currentServings, scaled);

        return "recipeDetails";
    }

    @GetMapping("/search")
    public String searchRecipes(@RequestParam("query") String query, Model datamodel) {

        Set<Recipe> recipeResults = recipeService.searchRecipes(query);
        List<Recipe> categoryResults = categoryService.findRecipesByCategoryName(query);
        Set<Recipe> combined = new HashSet<>(recipeResults);

        if (categoryResults != null) {
            combined.addAll(categoryResults);
        }

        datamodel.addAttribute("recipes", combined);
        datamodel.addAttribute("query", query);
        return "recipeSearchResults";
    }

    @PostMapping("/detail/{title}/increase")
    public String increase(@PathVariable("title") String title,
                           @RequestParam int currentServings) {
        int updatedServings = recipeService.increaseServings(currentServings);
        return "redirect:/recipe/detail/" + title + "?servings=" + updatedServings;
    }

    @PostMapping("/detail/{title}/decrease")
    public String decrease(@PathVariable("title") String title,
                           @RequestParam int currentServings, Model model) {

        int updatedServings;

        try {
            updatedServings = recipeService.decreaseServings(currentServings);
        } catch (IllegalArgumentException exception) {
            model.addAttribute("errorMessage", exception.getMessage());
            return showRecipeDetailpage(title, currentServings, model);
        }
        return "redirect:/recipe/detail/" + title + "?servings=" + updatedServings;
    }

    private void addRecipesToModel(Model model, KookKompasUser user) {
        if (user != null) {
            model.addAttribute("recipes", recipeService.getAllRecipesForUser(user));
        } else {
            model.addAttribute("recipes", recipeService.getAllPublicRecipes());
        }
    }

    private void processCoverImage(Recipe recipe, MultipartFile file, BindingResult result) {
        try {
            if (file != null && !file.isEmpty()) {
                imageService.saveImage(file);
                recipe.setCoverImageUrl("/image/" + file.getOriginalFilename());
            } else if (recipe.getCoverImageUrl() != null && !recipe.getCoverImageUrl().isBlank()) {

            } else {
                recipe.setCoverImageUrl("/images/default.png");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private KookKompasUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getName())) {
            return null;
        }

        return kookKompasUserService.getLoggedInUser();
    }

    private boolean userCanAccess(Recipe recipe, KookKompasUser user) {
        if (recipe.isPublicVisible()) return true;
        if (user == null) return false;
        return recipe.getOwner().equals(user);
    }

    private int determineServings(Recipe recipe, Integer requestedServings) {
        return (requestedServings != null) ? requestedServings : recipe.getServings();
    }

    private void addRecipeDetailModelAttributes(
            Model model,
            Recipe recipe,
            int servings,
            List<RecipeIngredient> scaledIngredients) {

        model.addAttribute("recipe", recipe);
        model.addAttribute("currentServings", servings);
        model.addAttribute("scaledIngredients", scaledIngredients);
    }
}