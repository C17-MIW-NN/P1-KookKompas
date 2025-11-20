package nl.miw.ch17.mmadevforce.kookkompas.controller;

import nl.miw.ch17.mmadevforce.kookkompas.dto.NewKookKompasUserDTO;
import nl.miw.ch17.mmadevforce.kookkompas.service.KookKompasUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author MMA Dev Force
 * Doel van de class:
 */
@Controller
@RequestMapping("/user")
public class KookKompasUserController {
    private final KookKompasUserService kookKompasUserService;

    public KookKompasUserController(KookKompasUserService kookKompasUserService) {
        this.kookKompasUserService = kookKompasUserService;
    }

    @GetMapping("/all")
    private String showUserOverview(Model datamodel) {
        datamodel.addAttribute("allUsers", kookKompasUserService.getAllUsers());
        datamodel.addAttribute("formUser", new NewKookKompasUserDTO());
        datamodel.addAttribute("formModalHidden", true);

        return "userOverview";
    }

    @PostMapping("/save")
    private String saveOrUpdateUser(@ModelAttribute("formUser") NewKookKompasUserDTO userDtoToBeSaved, BindingResult result,
                                    Model datamodel) {
        if (kookKompasUserService.usernameInUse(userDtoToBeSaved.getUsername())) {
            result.rejectValue("username", "duplicate", "Deze gebruikersnaam is niet beschikbaar");
        }

        if (!userDtoToBeSaved.getPassword().equals(userDtoToBeSaved.getConfirmPassword())) {
            result.rejectValue("password", "no.match", "De wachtwoorden komen niet overeen");
        }

        if (result.hasErrors()) {
            datamodel.addAttribute("allUsers", kookKompasUserService.getAllUsers());
            datamodel.addAttribute("formModalHidden", false);
            return "userOverview";
        }

        kookKompasUserService.save(userDtoToBeSaved);
        return "redirect:/user/all";
    }
}