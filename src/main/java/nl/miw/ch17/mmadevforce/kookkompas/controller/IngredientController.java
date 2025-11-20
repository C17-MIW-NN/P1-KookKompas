package nl.miw.ch17.mmadevforce.kookkompas.controller;

import jakarta.validation.Valid;
import nl.miw.ch17.mmadevforce.kookkompas.model.Ingredient;
import nl.miw.ch17.mmadevforce.kookkompas.service.IngredientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * @author MMA Dev Force
 * Handles requests regarding ingredients
 */
@Controller
@RequestMapping("/ingredient")
public class IngredientController {
    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping("/all")
    public String showIngredientOverview(Model datamodel) {
        datamodel.addAttribute("ingredients", ingredientService.findAllIngredients());
        return "ingredientOverview";
    }

    @GetMapping("/add")
    public String showIngredientForm(Model datamodel){
        datamodel.addAttribute("formIngredient", new Ingredient());
        return "ingredientForm";
    }

    @PostMapping("/save")
    public String saveIngredient(
            @Valid @ModelAttribute("formIngredient") Ingredient ingredientToBeSaved,
            BindingResult bindingResult,
            Model model ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("formIngredient", ingredientToBeSaved );
            return "ingredientForm";
        }
        ingredientService.saveIngredient(ingredientToBeSaved);
        return getRedirectIngredientAll();
    }
    
    @GetMapping("/edit/{name}")
    public String showEditIngredientForm(@PathVariable("name") String name, Model datamodel) {
        Optional<Ingredient> optionalIngredient = ingredientService.findByIngredientName(name);

        if (optionalIngredient.isPresent()) {
            datamodel.addAttribute("formIngredient", optionalIngredient.get());
            return "ingredientForm";
        }

        return getRedirectIngredientAll();
    }

    @GetMapping("/delete/{ingredientId}")
    public String deleteIngredient(@PathVariable("ingredientId") Long ingredientId) {
        ingredientService.deleteIngredient(ingredientId);
        return getRedirectIngredientAll();
    }

    private static String getRedirectIngredientAll() {
        return "redirect:/ingredient/all";
    }

}
