package nl.miw.ch17.mmadevforce.kookkompas.service;

import nl.miw.ch17.mmadevforce.kookkompas.model.KookKompasUser;
import nl.miw.ch17.mmadevforce.kookkompas.model.Recipe;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author MMA Dev Force
 * Managing functionalities from the homepage
 */
@Service
public class HomepageService {

    private final RecipeRepository recipeRepository;

    public HomepageService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public List<Recipe> findRandomPublicRecipes(int count) {
        List<Recipe> publicRecipes = recipeRepository.findAll().stream()
                .filter(Recipe::isPublicVisible)
                .collect(Collectors.toList());
        Collections.shuffle(publicRecipes);
        return publicRecipes.stream().limit(count).collect(Collectors.toList());
    }

    public List<Recipe> findRandomRecipesForUser(KookKompasUser user, int count) {
        List<Recipe> recipes = recipeRepository.findAll().stream()
                .filter(recipe -> recipe.isPublicVisible() ||
                        (recipe.getOwner() != null && recipe.getOwner().getUserId().equals(user.getUserId())))
                .collect(Collectors.toList());
        Collections.shuffle(recipes);
        return recipes.stream().limit(count).collect(Collectors.toList());
    }
}
