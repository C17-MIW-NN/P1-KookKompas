package nl.miw.ch17.mmadevforce.kookkompas.controller;

import nl.miw.ch17.mmadevforce.kookkompas.model.KookKompasUser;
import nl.miw.ch17.mmadevforce.kookkompas.service.HomepageService;
import nl.miw.ch17.mmadevforce.kookkompas.service.KookKompasUserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author MMA Dev Force
 * Handles requests regarding homepage
 */
@Controller
public class HomepageController {
    private static final int AMOUNT_RANDOM_RECIPES = 3;

    private final HomepageService homepageService;
    private final KookKompasUserService kookKompasUserService;

    public HomepageController(HomepageService homepageService, KookKompasUserService kookKompasUserService) {
        this.homepageService = homepageService;
        this.kookKompasUserService = kookKompasUserService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("welcomeMessage", "Welkom bij KookKompas!");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() &&
                !"anonymousUser".equals(authentication.getName())) {
            KookKompasUser currentUser = kookKompasUserService.getLoggedInUser();
            model.addAttribute("recipes", homepageService.findRandomRecipesForUser(currentUser,
                    AMOUNT_RANDOM_RECIPES));
        } else {
            model.addAttribute("recipes", homepageService.findRandomPublicRecipes(AMOUNT_RANDOM_RECIPES));
        }
        return "homepage";
    }
}
