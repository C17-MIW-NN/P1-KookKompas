package nl.miw.ch17.mmadevforce.kookkompas.controller;

import jakarta.validation.Valid;
import nl.miw.ch17.mmadevforce.kookkompas.model.Category;
import nl.miw.ch17.mmadevforce.kookkompas.model.Recipe;
import nl.miw.ch17.mmadevforce.kookkompas.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author MMA Dev Force
 * Handles requests regarding recipe categories
 */
@Controller
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/category/all")
    public String showRecipeCategories(Model viewmodel) {
        viewmodel.addAttribute("categories", categoryService.findAllCategories());
        viewmodel.addAttribute("formRecipeCategories", new Category());
        return "categoryOverview";
    }

    @GetMapping("/category/add")
    public String showCategoryForm(Model viewmodel) {
        viewmodel.addAttribute("formCategory", new Category());
        return "formRecipeCategories";
    }

    @PostMapping("/category/save")
    public String saveOrUpdateCategory(
            @Valid @ModelAttribute("formCategory") Category categoryToBeSaved, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("formCategory", categoryToBeSaved);
            return "formRecipeCategories";
        }

        if (!categoryService.isCategoryNameUnique(categoryToBeSaved.getCategoryName())) {
            bindingResult.rejectValue("categoryName", "error.categoryName", "Deze categorie bestaat al.");
            model.addAttribute("formCategory", categoryToBeSaved);
            return "formRecipeCategories";
        }
        categoryService.saveCategory(categoryToBeSaved);
        return getRedirectCategoryAll();
    }

    @GetMapping("/category/delete/{categoryId}")
    public String deleteCategory(@PathVariable("categoryId") Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return getRedirectCategoryAll();
    }

    @GetMapping("/category/edit/{categoryName}")
    public String showEditCategoryForm(@PathVariable("categoryName") String categoryName, Model viewmodel) {
        Optional<Category> optionalCategory = categoryService.findByCategoryName(categoryName);

        if (optionalCategory.isPresent()) {
            viewmodel.addAttribute("formCategory", optionalCategory.get());
            return "formRecipeCategories";
        }
        return getRedirectCategoryAll();
    }

    @GetMapping("/category/search")
    public String searchRecipesByCategory(@RequestParam("query") String query, Model datamodel) {
        List<Recipe> recipes = categoryService.findRecipesByCategoryName(query);
        datamodel.addAttribute("recipes", recipes);
        datamodel.addAttribute("query", query);
        return "recipeSearchResults";
    }

    private static String getRedirectCategoryAll() {
        return "redirect:/category/all";
    }

}
