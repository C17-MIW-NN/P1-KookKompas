package nl.miw.ch17.mmadevforce.kookkompas.model;

/**
 * @author Melanie van der Vlies
 * Doel van de class:
 */
public class Homepage {
    private String welcomeMessage;

    public Homepage() {
        this.welcomeMessage = "Welkom bij KookKompas!";
    }

    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    public void setWelcomeMessage(String welcomeMessage) {
        this.welcomeMessage = welcomeMessage;
    }
}
