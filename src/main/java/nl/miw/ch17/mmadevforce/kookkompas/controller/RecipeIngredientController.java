package nl.miw.ch17.mmadevforce.kookkompas.controller;

import nl.miw.ch17.mmadevforce.kookkompas.model.RecipeIngredient;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.IngredientRepository;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.RecipeIngredientRepository;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.RecipeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * @author Melanie van der Vlies
 * Handle requests reguarding recipes and their ingredients
 */
@Controller
public class RecipeIngredientController {
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;

    public RecipeIngredientController(RecipeIngredientRepository recipeIngredientRepository,
                                      RecipeRepository recipeRepository, IngredientRepository ingredientRepository) {
        this.recipeIngredientRepository = recipeIngredientRepository;
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
    }

    @GetMapping({"/recipeIngredient/all"})
    public String showRecipeIngredientOverview(Model viewmodel) {
        viewmodel.addAttribute("recipeIngredient", new RecipeIngredient());
        viewmodel.addAttribute("ingredients", ingredientRepository.findAll());
        viewmodel.addAttribute("recipes", recipeRepository.findAll());

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
            recipeIngredientRepository.save(recipeIngredientToBeSaved);
        }
        return "redirect:/recipeIngredient/all";
    }

    @GetMapping("/recipeIngredient/delete/{recipeIngredientId}")
    public String deleteRecipeIngredient(@PathVariable("recipeIngredientId") Long recipeIngredientId) {
        recipeIngredientRepository.deleteById(recipeIngredientId);
        return "redirect:/recipeIngredient/all";
    }

    @GetMapping("/recipeIngredient/edit/{recipeIngredientId}")
    public String showEditRecipeIngredientForm(@PathVariable("complaintId") Long recipeIngredientId, Model viewmodel) {
        Optional<RecipeIngredient> optionalRecipeIngredient = recipeIngredientRepository.findById(recipeIngredientId);

        if (optionalRecipeIngredient.isPresent()) {
            viewmodel.addAttribute("formComplaint", optionalRecipeIngredient);
            return "recipeIngredientForm";
        }
        return "redirect:/recipeIngredient/all";
    }
}
