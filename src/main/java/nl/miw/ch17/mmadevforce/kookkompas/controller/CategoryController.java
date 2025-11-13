package nl.miw.ch17.mmadevforce.kookkompas.controller;

import nl.miw.ch17.mmadevforce.kookkompas.model.Category;
import nl.miw.ch17.mmadevforce.kookkompas.model.RecipeIngredient;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.CategoryRepository;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.RecipeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

/**
 * @author MMA Dev Force
 *
 */
@Controller
public class CategoryController {
    private final CategoryRepository categoryRepository;
    private final RecipeRepository recipeRepository;

    public CategoryController(CategoryRepository categoryRepository, RecipeRepository recipeRepository) {
        this.categoryRepository = categoryRepository;
        this.recipeRepository = recipeRepository;
    }

    @GetMapping("/category/all")
    public String showRecipeCategories(Model viewmodel) {
        viewmodel.addAttribute("categories", categoryRepository.findAll());
        viewmodel.addAttribute("formRecipeCategories", new Category());
        return "categoryOverview";
    }

    @GetMapping("/category/add")
    public String showCategoryForm(Model viewmodel) {
        viewmodel.addAttribute("formCategory", new Category());
        return "formRecipeCategories";
    }

    @PostMapping("/category/save")
    public String saveOrUpdateCategory(@ModelAttribute("formCategory") Category categoryToBeSaved) {
        categoryRepository.save(categoryToBeSaved);
        return "redirect:/category/all";
    }

    @GetMapping("/category/delete/{categoryId}")
    public String deleteCategory(@PathVariable("categoryId") Long categoryId) {
        categoryRepository.deleteById(categoryId);
        return "redirect:/category/all";
    }

    @GetMapping("/category/edit/{categoryName}")
    public String showEditCategoryForm(@PathVariable("categoryName") String categoryName, Model viewmodel) {
        Optional<Category> optionalCategory = categoryRepository.findByCategoryName(categoryName);

        if (optionalCategory.isPresent()) {
            viewmodel.addAttribute("formCategory", optionalCategory.get());
            return "formRecipeCategories";
        }
        return "redirect:/category/all";
    }

}
