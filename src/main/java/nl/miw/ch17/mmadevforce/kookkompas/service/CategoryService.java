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

}
