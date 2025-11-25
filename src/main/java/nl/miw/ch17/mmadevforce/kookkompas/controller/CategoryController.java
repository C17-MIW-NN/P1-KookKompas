package nl.miw.ch17.mmadevforce.kookkompas.controller;

import jakarta.validation.Valid;
import nl.miw.ch17.mmadevforce.kookkompas.model.Category;
import nl.miw.ch17.mmadevforce.kookkompas.model.KookKompasUser;
import nl.miw.ch17.mmadevforce.kookkompas.model.Recipe;
import nl.miw.ch17.mmadevforce.kookkompas.service.CategoryService;
import nl.miw.ch17.mmadevforce.kookkompas.service.KookKompasUserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private final KookKompasUserService kookKompasUserService;

    public CategoryController(CategoryService categoryService, KookKompasUserService kookKompasUserService) {
        this.categoryService = categoryService;
        this.kookKompasUserService = kookKompasUserService;
    }

    @GetMapping("/category/all")
    public String showRecipeCategories(Model viewmodel,
                                       @AuthenticationPrincipal KookKompasUser user) {
        viewmodel.addAttribute("categories", categoryService.getAllCategoriesForUser(user));
        return "categoryOverview";
    }

    @GetMapping("/category/add")
    public String showCategoryForm(Model viewmodel) {
        viewmodel.addAttribute("formCategory", new Category());
        return "formRecipeCategories";
    }

    @PostMapping("/category/save")
    public String saveOrUpdateCategory(
            @Valid @ModelAttribute("formCategory") Category categoryToBeSaved,
            BindingResult bindingResult,
            Model model,
            @AuthenticationPrincipal KookKompasUser user)  {

        if (bindingResult.hasErrors()) {
            model.addAttribute("formCategory", categoryToBeSaved);
            return "formRecipeCategories";
        }

        if (categoryService.isCategoryNameTaken(categoryToBeSaved.getCategoryName(), user)) {
            bindingResult.rejectValue("categoryName", "error.categoryName", "Deze categorie bestaat al.");
            model.addAttribute("formCategory", categoryToBeSaved);
            return "formRecipeCategories";
        }

        categoryService.saveCategoryFromUser(categoryToBeSaved, user);
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
