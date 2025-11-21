package nl.miw.ch17.mmadevforce.kookkompas.controller;

import nl.miw.ch17.mmadevforce.kookkompas.service.ShoppingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author MMA Dev Force
 * Handles requests regarding the shopping list
 */
@Controller
@RequestMapping("/shoppinglist")
public class ShoppingListController {

    @Autowired
    private final ShoppingListService shoppingListService;

    public ShoppingListController(ShoppingListService shoppingListService) {
        this.shoppingListService = shoppingListService;
    }

    @GetMapping("/all")
    public String showShoppingList(Model datamodel) {
        datamodel.addAttribute("shoppingList", shoppingListService.getItemsForLoggedInUser());
        return "shoppingListOverview";
    }

    @GetMapping("/add/{recipeId}")
    public String addRecipeToShoppingList(@PathVariable Long recipeId) {
        shoppingListService.addRecipeToShoppingList(recipeId);
        return getShoppinglistAll();
    }

    @GetMapping("/clear")
    public String clearShoppingList() {
        shoppingListService.clearShoppingList();
        return getShoppinglistAll();
    }

    private static String getShoppinglistAll() {
        return "redirect:/shoppinglist/all";
    }

}
