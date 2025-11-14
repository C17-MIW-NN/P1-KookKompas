package nl.miw.ch17.mmadevforce.kookkompas.controller;

import nl.miw.ch17.mmadevforce.kookkompas.model.Ingredient;
import nl.miw.ch17.mmadevforce.kookkompas.model.Recipe;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.IngredientRepository;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.RecipeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @author MMA Dev Force
 * Doel methode
 */
@Controller
@RequestMapping("/ingredient")
public class IngredientController {

    private final IngredientRepository ingredientRepository;
    private final RecipeRepository recipeRepository;

    public IngredientController(IngredientRepository ingredientRepository, RecipeRepository recipeRepository) {
        this.ingredientRepository = ingredientRepository;
        this.recipeRepository = recipeRepository;
    }

    @GetMapping("/all")
    public String showIngredientOverview(Model datamodel) {
        datamodel.addAttribute("ingredients", ingredientRepository.findAll());
        return "ingredientOverview";
    }

    @GetMapping("/add")
    public String showIngredientForm(Model datamodel){
        datamodel.addAttribute("formIngredient", new Ingredient());

        return "ingredientForm";
    }

    @PostMapping("/save")
    public String saveIngredient(@ModelAttribute("formIngredient") Ingredient ingredientToBeSaved){
        ingredientRepository.save(ingredientToBeSaved);
        return "redirect:/ingredient/all";
    }

    @GetMapping("/edit/{name}")
    public String showEditIngredientForm(@PathVariable("name") String name, Model datamodel) {
        Optional<Ingredient> optionalIngredient = ingredientRepository.findByName(name);

        if (optionalIngredient.isPresent()) {
            datamodel.addAttribute("formIngredient", optionalIngredient.get());
            return "ingredientForm";
        }

        return "redirect:/ingredient/all";
    }

    @GetMapping("/delete/{ingredientId}")
    public String deleteIngredient(@PathVariable("ingredientId") Long ingredientId) {
        Ingredient ingredient = ingredientRepository.findById(ingredientId).orElse(null);
        if (ingredient != null) {
            List<Recipe> recipesWithIngredient = recipeRepository.findByRecipeingredientsIngredient(ingredient);
            recipesWithIngredient.forEach(recipe -> {
                recipe.getRecipeingredients()
                        .removeIf(recipeIngredient ->
                                recipeIngredient.getIngredient().getIngredientId()
                                        .equals(ingredient.getIngredientId()));
            });
            ingredientRepository.delete(ingredient);
        }
        return "redirect:/ingredient/all";
    }

}
