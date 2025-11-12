package nl.miw.ch17.mmadevforce.kookkompas.controller;

import nl.miw.ch17.mmadevforce.kookkompas.model.Recipe;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.RecipeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

/**
 * @author Arjen Zijlstra
 *
 */

@Controller
public class RecipeController {

    private final RecipeRepository recipeRepository;

    public RecipeController(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @GetMapping({"/recipe/all", "/"})
    private String showRecipeOverview(Model datamodel) {

        datamodel.addAttribute("recipes", recipeRepository.findAll());

        return "recipeOverview";
    }

    @GetMapping("/recipe/add")
    public String showRecipeForm(Model datamodel) {
        datamodel.addAttribute("formRecipe", new Recipe());

        return "recipeForm";
    }

    @GetMapping("/recipe/edit/{title}")
    public String showEditRecipeForm(@PathVariable("title") String title, Model datamodel) {

        Optional<Recipe> optionalRecipe = recipeRepository.findByTitle(title);

        if (optionalRecipe.isPresent()) {
            datamodel.addAttribute("formRecipe", optionalRecipe.get());
            return "recipeForm";
        }

        return "redirect:/recipe/all";
    }

    @PostMapping("/recipe/save")
    public String saveOrUpdateRecipe(
            @ModelAttribute("formRecipe") Recipe recipeFromForm) {

        // Bestaand recept ophalen of nieuw recept aanmaken
        Recipe recipeToBeSaved;
        if (recipeFromForm.getRecipeId() != null) {
            recipeToBeSaved = recipeRepository.findById(recipeFromForm.getRecipeId()).orElseThrow();
            recipeToBeSaved.setTitle(recipeFromForm.getTitle());
        } else {
            recipeToBeSaved = new Recipe();
            recipeToBeSaved.setTitle(recipeFromForm.getTitle());
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
    public String showRecipeDetailpage(@PathVariable("title") String title, Model datamodel) {
        Optional<Recipe> recipeToShow = recipeRepository.findByTitle(title);

        if (recipeToShow.isEmpty()) {
            return "redirect:/recipe/all";
        }

        datamodel.addAttribute("recipe", recipeToShow.get());

        return "recipeDetails";
    }

}
