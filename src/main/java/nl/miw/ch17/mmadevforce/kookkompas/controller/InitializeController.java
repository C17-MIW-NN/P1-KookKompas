package nl.miw.ch17.mmadevforce.kookkompas.controller;

import nl.miw.ch17.mmadevforce.kookkompas.model.*;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.*;
import nl.miw.ch17.mmadevforce.kookkompas.service.InitializeService;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

import java.util.*;

/**
 * @author MMA Dev Force
 * Initialises the database with example data
 */
@Controller
public class InitializeController {
    private final InitializeService initializeService;

    public InitializeController(InitializeService initializeService) {
        this.initializeService = initializeService;
    }

    @EventListener
    private void seed(ContextRefreshedEvent ignoredEvent) {
        initializeService.seedDatabaseIfEmpty();
    }
}
