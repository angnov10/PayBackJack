 

import ea.edu.TextE;

/**
 * Hilfsklasse zum zentralen Zuweisen der Pixelify Sans Schriftart auf Texte.
 */
public class FontHelper {
    public static final String FONT_NAME = "Tiny5 Regular";

    /**
     * Wendet die Schriftart auf alle uebergebenen TextE-Objekte an.
     */
    public static void anwenden(TextE... texte) {
        for (TextE t : texte) {
            if (t != null) {
                t.fontSetzen(FONT_NAME);
            }
        }
    }
}
