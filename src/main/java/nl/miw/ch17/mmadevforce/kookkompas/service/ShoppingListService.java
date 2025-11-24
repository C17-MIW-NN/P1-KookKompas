package nl.miw.ch17.mmadevforce.kookkompas.service;

import nl.miw.ch17.mmadevforce.kookkompas.model.*;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.KookKompasUserRepository;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.RecipeRepository;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.ShoppingListItemRepository;
import java.util.List;

import nl.miw.ch17.mmadevforce.kookkompas.repositories.ShoppingListRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author MMA Dev Force
 * Managing functionalities from the shopping list
 */
@Service
public class ShoppingListService {

    private final RecipeRepository recipeRepository;
    private final ShoppingListItemRepository shoppingListItemRepository;
    private final ShoppingListRepository shoppingListRepository;
    private final KookKompasUserRepository kookKompasUserRepository;

   // @Autowired
    private KookKompasUserService kookKompasUserService;

    public ShoppingListService(RecipeRepository recipeRepository, ShoppingListItemRepository shoppingListItemRepository, ShoppingListRepository shoppingListRepository, KookKompasUserRepository kookKompasUserRepository, KookKompasUserService kookKompasUserService) {
        this.recipeRepository = recipeRepository;
        this.shoppingListItemRepository = shoppingListItemRepository;
        this.shoppingListRepository = shoppingListRepository;
        this.kookKompasUserRepository = kookKompasUserRepository;
        this.kookKompasUserService = kookKompasUserService;
    }

    public List<ShoppingListItem> findAllShoppingListItems() {
        return shoppingListItemRepository.findAll();
    }

    public void addRecipeToShoppingList(Long recipeId) {

        KookKompasUser kookKompasUser = kookKompasUserService.getLoggedInUser();

        ShoppingList shoppingList = kookKompasUser.getShoppingList();

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new IllegalArgumentException("Recept niet gevonden"));

        for (RecipeIngredient recipeIngredient : recipe.getRecipeingredients()) {
            Ingredient ingredient = recipeIngredient.getIngredient();
            Double amount = recipeIngredient.getIngredientAmount();
            String unit = recipeIngredient.getUnit();

            Optional<ShoppingListItem> existing = shoppingListItemRepository.findByShoppingListAndIngredientAndUnit(shoppingList, ingredient, unit);

            if (existing.isPresent()) {
                ShoppingListItem item = existing.get();
                item.setAmount(item.getAmount() + recipeIngredient.getIngredientAmount());
                shoppingListItemRepository.save(item);
            } else {
                ShoppingListItem newItem = new ShoppingListItem();
                newItem.setShoppingList(shoppingList);
                newItem.setIngredient(ingredient);
                // newItem.setIngredientName(ingredient.getIngredientName());
                newItem.setAmount(amount);
                newItem.setUnit(unit);

                shoppingList.getShoppingItems().add(newItem);
            }
        }
        kookKompasUserService.save(kookKompasUser);
    }

    public void clearShoppingList() {
        KookKompasUser kookKompasUser = kookKompasUserService.getLoggedInUser();
        ShoppingList shoppingList = kookKompasUser.getShoppingList();
        shoppingList.getShoppingItems().clear();
        shoppingListRepository.save(shoppingList);
    }

    public List<ShoppingListItem> getItemsForLoggedInUser() {
        KookKompasUser kookKompasUser = kookKompasUserService.getLoggedInUser();
        return kookKompasUser.getShoppingList().getShoppingItems();
    }

}
