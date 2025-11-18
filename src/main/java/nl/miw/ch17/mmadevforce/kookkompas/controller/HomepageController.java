package nl.miw.ch17.mmadevforce.kookkompas.controller;

import nl.miw.ch17.mmadevforce.kookkompas.service.HomepageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author MMA DEV FORCE
 * Homepage
 */
@Controller
public class HomepageController {
    private final HomepageService homepageService;

    public HomepageController(HomepageService homepageService) {
        this.homepageService = homepageService;
    }

    @GetMapping("/homepage")
    public String home(Model model) {
        model.addAttribute("welcomeMessage", "Welkom bij KookKompas!");
        model.addAttribute("recipes", homepageService.findAllRecipes());
        return "homepage";
    }

}
