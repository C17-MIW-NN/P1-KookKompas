package nl.miw.ch17.mmadevforce.kookkompas.controller;

import nl.miw.ch17.mmadevforce.kookkompas.model.Ingredient;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.IngredientRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @author MMA Dev Force
 * Doel methode
 */
@Controller
@RequestMapping("/ingredient")
public class IngredientController {

    private final IngredientRepository ingredientRepository;

    public IngredientController(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
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

    @GetMapping("/delete/{ingredientId}")
    public String deleteIngredient(@PathVariable("ingredientId") Long ingredientId) {
        ingredientRepository.deleteById(ingredientId);
        return "redirect:/ingredient/all";
    }

}
