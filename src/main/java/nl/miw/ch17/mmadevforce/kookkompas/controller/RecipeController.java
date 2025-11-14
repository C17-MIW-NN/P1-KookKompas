package nl.miw.ch17.mmadevforce.kookkompas.controller;

import nl.miw.ch17.mmadevforce.kookkompas.model.Category;
import nl.miw.ch17.mmadevforce.kookkompas.model.Recipe;
import nl.miw.ch17.mmadevforce.kookkompas.model.RecipeIngredient;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.CategoryRepository;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.RecipeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Arjen Zijlstra
 *
 */

@Controller
public class RecipeController {

    private final RecipeRepository recipeRepository;
    private final CategoryRepository categoryRepository;

    public RecipeController(RecipeRepository recipeRepository, CategoryRepository categoryRepository) {
        this.recipeRepository = recipeRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping({"/recipe/all", "/"})
    private String showRecipeOverview(Model datamodel) {

        datamodel.addAttribute("recipes", recipeRepository.findAll());

        return "recipeOverview";
    }

    @GetMapping("/recipe/add")
    public String showRecipeForm(Model datamodel) {
        return showRecipeForm(datamodel, new Recipe());
    }

    @GetMapping("/recipe/edit/{title}")
    public String showEditRecipeForm(@PathVariable("title") String title, Model datamodel) {

        Optional<Recipe> optionalRecipe = recipeRepository.findByTitle(title);

        if (optionalRecipe.isPresent()) {
            return showRecipeForm(datamodel, optionalRecipe.get());
        }

        return "redirect:/recipe/all";
    }

    private String showRecipeForm(Model datamodel, Recipe recipe) {
        datamodel.addAttribute("formRecipe", recipe);
        datamodel.addAttribute("allCategories", categoryRepository.findAll());
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
        } else {
            recipeToBeSaved = new Recipe();
            recipeToBeSaved.setTitle(recipeFromForm.getTitle());
            recipeToBeSaved.setDescription(recipeFromForm.getDescription());
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
    public String showRecipeDetailpage(@PathVariable("title") String title, Model datamodel) {
        Optional<Recipe> recipeToShow = recipeRepository.findByTitle(title);

        if (recipeToShow.isEmpty()) {
            return "redirect:/recipe/all";
        }

        Recipe recipe = recipeToShow.get();
        datamodel.addAttribute("recipe", recipe);
        datamodel.addAttribute("ingredients", recipe.getRecipeingredients());

        return "recipeDetails";
    }

    @GetMapping("/recipe/search")
    public String searchRecipes(@RequestParam("query") String query, Model datamodel) {
        List<Recipe> results =recipeRepository.findByTitleContainingIgnoreCase(query);
        datamodel.addAttribute("recipes", results);
        datamodel.addAttribute("query", query);
        return "recipeSearchResults";
    }

    @GetMapping("/recipe/{recipeId}")
    public String showRecipe(@PathVariable Long recipeId, @RequestParam(required = false) Integer servings, Model model) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow();
        int currentServings = (servings != null) ? servings : recipe.getServings();

        model.addAttribute("recipe", recipe);
        model.addAttribute("currentServings", currentServings);

        List<RecipeIngredient> scaledIngredients = recipe.getRecipeingredients().stream().map(recipeingredient -> {
            double scaled = recipeingredient.getIngredientAmount() * currentServings / recipe.getServings();
            recipeingredient.setIngredientAmount(scaled);
            return recipeingredient;
        }).toList();

        model.addAttribute("scaledIngredients", scaledIngredients);

        return "recipeDetails";
    }

    @PostMapping("/recipe/{recipeId}/decrease")
    public String decrease(@PathVariable Long recipeId, @RequestParam int currentServings) {
        if (currentServings > 1) {
            currentServings--;
        }
        return "redirect:/recipe/" + recipeId + "?servings=" + currentServings;
    }

    @PostMapping("/recipe/{recipeId}/increase")
    public String increase(@PathVariable Long recipeId, @RequestParam int currentServings) {
        //int newServings = currentServings + 1;
        return "redirect:/recipe/" + recipeId + "?servings=" + (currentServings + 1);
    }

}
