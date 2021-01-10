package utils;

import java.util.Locale;

/**
 * Contains methods for translating text for the Login screen.
 * @author Christian Dewey
 */
public class Language {

    /**
     * If system language is set to 'fr', the Login Display labels are changed to French,
     *  otherwise the text is changed to English.
     * @return outputText the text to be displayed
     */
    public static String languageCheck(String s) {
        String outputText = null;

        if (Locale.getDefault().getLanguage().equals("fr")) {
            if (s.equals("username")) {outputText = "Nom d'utilisateur:";}
            if (s.equals("password")) {outputText = "Mot de passe:";}
            if (s.equals("login")) {outputText = "Connexion";}
            if (s.equals("successful")) {outputText = "Connexion Réussie";}
            if (s.equals("failed")) {outputText = "Échec de la Connexion";}

        } else {
            if (s.equals("username")) {outputText = "Username:";}
            if (s.equals("password")) {outputText = "Password:";}
            if (s.equals("login")) {outputText = "Login";}
            if (s.equals("successful")) {outputText = "Login Successful";}
            if (s.equals("failed")) {outputText = "Login Failed";}
        }

        return outputText;
    }

    /**
     * If system language is set to 'fr', the Login Display Console text is changed to French,
     *  otherwise the text is changed to English.
     * @return outputText the text to be displayed
     */
    public static String consoleLanguage(String s) {
        String outputText = null;

        String lang = Locale.getDefault().getLanguage();
        String location = Locale.getDefault().getDisplayCountry();

        if (Locale.getDefault().getLanguage().equals("fr")) {
            if (s.equals("appLanguage")) {outputText = "Langue de l'application définie sur " + "'" + lang + "'" + " en fonction de la Langue du Système.\n";}
            if (s.equals("appLocation")) {outputText = "Votre emplacement est " + location + ".\n";}

        } else {
            if (s.equals("appLanguage")) {outputText = "Application language set to " + "'" + lang + "'" + " based on System language.\n";}
            if (s.equals("appLocation")) {outputText = "Your location is " + location + ".\n";}
        }

        return outputText;
    }
}