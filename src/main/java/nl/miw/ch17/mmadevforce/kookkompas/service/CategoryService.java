package nl.miw.ch17.mmadevforce.kookkompas.service;

import nl.miw.ch17.mmadevforce.kookkompas.model.Category;
import nl.miw.ch17.mmadevforce.kookkompas.model.Recipe;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.CategoryRepository;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author MMA Dev Force
 * Managing functionalities from the recipe categories
 */
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final RecipeRepository recipeRepository;

    public CategoryService(CategoryRepository categoryRepository, RecipeRepository recipeRepository) {
        this.categoryRepository = categoryRepository;
        this.recipeRepository = recipeRepository;
    }

    public List<Recipe> findRecipesByCategoryName(String query) {
//        List<Category> matchingCategories = categoryRepository.findByCategoryNameContainingIgnoreCase(query);
//        Set<Recipe> recipes = new HashSet<>();
//
//        for (Category category : matchingCategories) {
//            recipes.addAll(recipeRepository.findByCategories_CategoryNameIgnoreCase(category.getCategoryName()));
//        }
//
//        return new ArrayList<>(recipes);
        return recipeRepository.findByCategories_CategoryNameIgnoreCase(query);
    }

    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    public Category saveCategory(Category category) {
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

}
