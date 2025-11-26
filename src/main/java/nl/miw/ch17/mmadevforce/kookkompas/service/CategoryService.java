package nl.miw.ch17.mmadevforce.kookkompas.service;

import nl.miw.ch17.mmadevforce.kookkompas.model.Category;
import nl.miw.ch17.mmadevforce.kookkompas.model.KookKompasUser;
import nl.miw.ch17.mmadevforce.kookkompas.model.Recipe;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.CategoryRepository;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.KookKompasUserRepository;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.RecipeRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author MMA Dev Force
 * Managing functionalities from the recipe categories
 */
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final RecipeRepository recipeRepository;
    private final KookKompasUserRepository kookKompasUserRepository;

    public CategoryService(CategoryRepository categoryRepository, RecipeRepository recipeRepository, KookKompasUserRepository kookKompasUserRepository) {
        this.categoryRepository = categoryRepository;
        this.recipeRepository = recipeRepository;
        this.kookKompasUserRepository = kookKompasUserRepository;
    }

    public boolean isCategoryNameTaken(String name, KookKompasUser user) {

        if (!categoryRepository.findByCategoryNameAndPublicVisibleTrue(name).isEmpty()) {
            return true;
        }

        if (user != null && !categoryRepository.findByCategoryNameAndOwner(name, user).isEmpty()) {
            return true;
        }

        return false;
    }

    public List<Recipe> findRecipesByCategoryName(String query) {
        List<Category> categories = categoryRepository.findByCategoryNameContainingIgnoreCase(query);
        Set<Recipe> result = new HashSet<>();

        for (Category category : categories) {
            result.addAll(category.getRecipes());
        }
        return new ArrayList<>(result);
    }

    public List<Category> findAllCategories() {

        return categoryRepository.findAll();
    }

    public Category saveCategoryFromUser(Category category, KookKompasUser user) {
        if (!category.isPublicVisible()) {
            category.setOwner(user);
        } else {
            category.setOwner(null);
        }
        return categoryRepository.save(category);
    }

    public Category savePublicCategory(Category category) {
        category.setPublicVisible(true);
        category.setOwner(null);
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category != null) {
            recipeRepository.findAll().forEach(recipe -> recipe.getCategories().remove(category));
            categoryRepository.delete(category);
        }
    }

    public Optional<Category> findByCategoryName(String categoryName) {
        return categoryRepository.findByCategoryName(categoryName);
    }

//<<<<<<< HEAD
//=======
    public List<Category> getAllCategoriesForUser(KookKompasUser user) {
        return categoryRepository.findAll().stream()
                .filter(category -> category.isPublicVisible() ||
                        (category.getOwner() != null && category.getOwner().getUserId().equals(user.getUserId())))
                .collect(Collectors.toList());
    }

    public List<Category> getAllPublicCategories() {
        return categoryRepository.findAll().stream()
                .filter(Category::isPublicVisible)
                .collect(Collectors.toList());
    }

//    public KookKompasUser getLoggedInUser() {
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        return kookKompasUserRepository.findByUsername(username)
//                .orElseThrow(() -> new RuntimeException("User not found: " + username));
//    }

//    public List<Recipe> findRecipesByCategoryQuery(String query) {
//        List<Category> matchingCategories = categoryRepository.findByCategoryNameContainingIgnoreCase(query);
//        Set<Recipe> recipes = new HashSet<>();
//
//        for (Category category : matchingCategories) {
//            recipes.addAll(category.getRecipes());
//        }
//
//        return new ArrayList<>(recipes);
//    }

//>>>>>>> e4b0e1d32849e59efe38e32304b4a1eaebe09974
}
