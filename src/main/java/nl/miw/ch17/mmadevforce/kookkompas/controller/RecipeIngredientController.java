package nl.miw.ch17.mmadevforce.kookkompas.controller;

import nl.miw.ch17.mmadevforce.kookkompas.model.Recipe;
import nl.miw.ch17.mmadevforce.kookkompas.model.RecipeIngredient;
import nl.miw.ch17.mmadevforce.kookkompas.service.RecipeIngredientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * @author MMA Dev Force
 * Handle requests regarding recipes and their ingredients
 */

@Controller
@RequestMapping("/recipe")
public class RecipeIngredientController {

    private final RecipeIngredientService recipeIngredientService;

    public RecipeIngredientController(RecipeIngredientService recipeIngredientService) {
        this.recipeIngredientService = recipeIngredientService;
    }

    @GetMapping({"/recipeIngredient/all"})
    public String showRecipeIngredientOverview(Model viewmodel) {
        viewmodel.addAttribute("recipeIngredient", new RecipeIngredient());
        viewmodel.addAttribute("ingredients", recipeIngredientService.getAllRecipeIngredients());
        viewmodel.addAttribute("recipes", recipeIngredientService.getAllRecipes());

        return "recipeIngredientOverview";
    }

    @GetMapping("/recipeIngredient/add")
    public String showRecipeIngredientForm(Model viewmodel) {
        viewmodel.addAttribute("formRecipeIngredient", new RecipeIngredient());
        return "recipeIngredientForm";
    }

    @PostMapping("/recipeIngredient/save")
    public String saveOrUpdateRecipeIngredient(@ModelAttribute("formRecipeIngredient")
                                                   RecipeIngredient recipeIngredientToBeSaved, BindingResult result) {
        if (!result.hasErrors()) {
            recipeIngredientService.saveRecipeIngredient(recipeIngredientToBeSaved);
        }
        return "redirect:/recipeIngredient/all";
    }

    @GetMapping("/recipeIngredient/delete/{recipeIngredientId}")
    public String deleteRecipeIngredient(@PathVariable("recipeIngredientId") Long recipeIngredientId) {
        recipeIngredientService.deleteRecipeIngredient(recipeIngredientId);
        return "redirect:/recipeIngredient/all";
    }

    @GetMapping("/recipeIngredient/edit/{recipeIngredientId}")
    public String showEditRecipeIngredientForm(@PathVariable("recipeIngredientId") Long recipeIngredientId, Model viewmodel) {
        Optional<RecipeIngredient> optionalRecipeIngredient = recipeIngredientService.getRecipeIngredientById(recipeIngredientId);

        if (optionalRecipeIngredient.isPresent()) {
            viewmodel.addAttribute("formIngredient", optionalRecipeIngredient);
            return "recipeIngredientForm";
        }
        return "redirect:/recipeIngredient/all";
    }

    @GetMapping("/{title}")
    public String showRecipe(@PathVariable String title, @RequestParam(required = false) Integer servings, Model model) {
        Recipe recipe = recipeIngredientService.getRecipeByTitle(title).orElseThrow();
        int currentServings = (servings != null) ? servings : recipe.getServings();
        model.addAttribute("recipe", recipe);
        model.addAttribute("currentServings", currentServings);

        return "redirect:/recipe/detail/{title}";
    }
}
