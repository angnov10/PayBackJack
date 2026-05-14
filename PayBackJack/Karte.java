import ea.edu.BildE;

/**
 * Diese Klasse speichert die Daten einer Karte und verwaltet ihr Pixelart-Bild.
 */
public class Karte {
    private String farbe;
    private String name;
    private int wert;
    
    private BildE bild;
    private BildE rueckseite;

    public Karte(String pFarbe, String pName, int pWert) {
        farbe = pFarbe;
        name = pName;
        wert = pWert;
        
        // Dateinamen-Anpassung fuer die Assets (J, Q, K, A)
        String fileName = pName;
        if (pName.equals("Bube")) fileName = "J";
        else if (pName.equals("Dame")) fileName = "Q";
        else if (pName.equals("Koenig")) fileName = "K";
        else if (pName.equals("Ass")) fileName = "A";
        
        // Absolute Pfade, da BlueJ aus dem Unterordner gestartet wird
        String pfad = "/Users/an/PayBackJack/assets/Karten/" + farbe + "/" + farbe + fileName + ".png";
        bild = new BildE(0, 0, pfad);
        bild.sichtbarSetzen(false);
        
        String rueckPfad = "/Users/an/PayBackJack/assets/Karten/Rückseite.png";
        rueckseite = new BildE(0, 0, rueckPfad);
        rueckseite.sichtbarSetzen(false);
    }

    public int getWert() { return wert; }
    public String getName() { return name; }
    public String getFarbe() { return farbe; }
    
    public String getAnzeigeText() {
        return farbe + " " + name;
    }
    
    /**
     * Zeigt die Karte an den gegebenen Koordinaten an.
     * @param verdeckt true, wenn die Rueckseite gezeigt werden soll (z.B. 2. Karte vom Dealer)
     */
    public void anzeigen(int x, int y, boolean verdeckt) {
        if (verdeckt) {
            rueckseite.positionSetzen(x, y);
            rueckseite.sichtbarSetzen(true);
            bild.sichtbarSetzen(false);
        } else {
            bild.positionSetzen(x, y);
            bild.sichtbarSetzen(true);
            rueckseite.sichtbarSetzen(false);
        }
    }
    
    /**
     * Versteckt die Karte vom Bildschirm.
     */
    public void verstecken() {
        bild.sichtbarSetzen(false);
        rueckseite.sichtbarSetzen(false);
    }
    
    public void drehen(double grad) {
        bild.drehenAbsolut(grad);
        rueckseite.drehenAbsolut(grad);
    }
}
