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

    @GetMapping({"/recipe/all"})
    private String showRecipeOverview(Model datamodel) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        KookKompasUser currentUser = null;

        if (authentication != null && authentication.isAuthenticated() &&
                !"anonymousUser".equals(authentication.getName())) {
            currentUser = kookKompasUserService.getLoggedInUser();
            datamodel.addAttribute("recipes", recipeService.getAllRecipesForUser(currentUser));
        } else {
            datamodel.addAttribute("recipes", recipeService.getAllPublicRecipes());
        }

<<<<<<< HEAD
        datamodel.addAttribute("categories", categoryService.findAllCategories());
=======
        if (currentUser != null) {
            datamodel.addAttribute("categories", categoryService.getAllCategoriesForUser(currentUser));
        } else {
            datamodel.addAttribute("categories", categoryService.getAllPublicCategories());
        }

>>>>>>> e4b0e1d32849e59efe38e32304b4a1eaebe09974
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

        KookKompasUser currentUser = kookKompasUserService.getLoggedInUser();
        if (recipeFromForm.getOwner() == null) {
            recipeFromForm.setOwner(currentUser);
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

        //Recipe recipe = recipeService.getRecipeByTitle(title);
        Optional<Recipe> optionalRecipe = recipeService.getRecipeWithIngredientsAndCategoriesByTitle(title);
        optionalRecipe.ifPresent(r -> r.getSteps().size());
        Recipe recipe = optionalRecipe.orElseThrow(() -> new RuntimeException("Recipe not found: " + title));
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        KookKompasUser currentUser = null;
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            currentUser = kookKompasUserService.getLoggedInUser();
        }

        if (!recipe.isPublicVisible()) {
            if (currentUser == null || !recipe.getOwner().equals(currentUser)) {
                return "redirect:/recipe/all";
            }
        }

        int currentServings = (servings != null) ? servings : recipe.getServings();

        List<RecipeIngredient> scaledIngredients = recipeService.getScaledIngredients(recipe, currentServings);

        model.addAttribute("recipe", recipe);
        model.addAttribute("currentServings", currentServings);
        model.addAttribute("scaledIngredients", scaledIngredients);

        return "recipeDetails";
    }

    @GetMapping("/recipe/search")
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

    @PostMapping("/recipe/detail/{title}/increase")
    public String increase(@PathVariable("title") String title, @RequestParam int currentServings) {
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