import java.util.ArrayList;

/**
 * Verwaltet die Karten in einer Hand.
 */
public class Hand {
    private ArrayList<Karte> kartenInHand;

    public Hand() {
        kartenInHand = new ArrayList<Karte>();
    }

    public void karteHinzufuegen(Karte k) {
        if (kartenInHand.size() < 10) {
            kartenInHand.add(k);
        }
    }

    public int punkteBerechnen() {
        int summe = 0;
        int asse = 0;
        for (Karte k : kartenInHand) {
            if (k != null) {
                summe += k.getWert();
                if (k.getWert() == 11) asse++;
            }
        }
        while (summe > 21 && asse > 0) {
            summe -= 10;
            asse--;
        }
        return summe;
    }

    public void alleAnzeigen(int startX, int startY, boolean dealerVerdeckt) {
        int abstand = 88; // Abstand weiter erhoeht
        for (int i = 0; i < kartenInHand.size(); i++) {
            Karte k = kartenInHand.get(i);
            
            // Wenn Dealer, und es ist die zweite Karte (Index 1) und nicht umgedreht
            boolean verdeckt = (dealerVerdeckt && i == 1);
            k.anzeigen(startX + (i * abstand), startY, verdeckt);
        }
    }
    
    public void positionAnzeigen(int x, int y, boolean dealerVerdeckt) {
        alleAnzeigen(x, y, dealerVerdeckt);
    }
    
    public void alleAnzeigenGedreht(int startX, int startY, double grad) {
        int abstand = 88;
        double rad = Math.toRadians(grad);
        double stepX = abstand * Math.cos(rad);
        double stepY = abstand * Math.sin(rad);
        
        for (int i = 0; i < kartenInHand.size(); i++) {
            Karte k = kartenInHand.get(i);
            int curX = (int) (startX + i * stepX);
            int curY = (int) (startY + i * stepY);
            k.drehen(grad);
            k.anzeigen(curX, curY, false);
        }
    }
    
    public void alleDrehen(double grad) {
        for (Karte k : kartenInHand) {
            k.drehen(grad);
        }
    }
    
    public void alleZentriertAnzeigen(int centerX, int startY, boolean dealerVerdeckt) {
        int abstand = 88;
        int kartenBreite = 154;
        int gesamtBreite = (kartenInHand.size() - 1) * abstand + kartenBreite;
        int startX = centerX - (gesamtBreite / 2);
        
        alleAnzeigen(startX, startY, dealerVerdeckt);
    }
    
    /**
     * Versteckt alle Karten in der Hand und gibt sie zurueck an den Stapel.
     * Dies verhindert, dass sich Bilder im Fenster stauen.
     */
    public void kartenZurueckgeben(Kartenstapel deck) {
        for (Karte k : kartenInHand) {
            k.drehen(0); // Rotation fuer die Wiederverwendung zuruecksetzen!
            k.verstecken();
            if (deck != null) deck.zuruecknehmen(k);
        }
        kartenInHand.clear();
    }
    
    /**
     * Versteckt die Karten nur optisch (z.B. bei einem Szenenwechsel).
     */
    public void alleOptischVerstecken() {
        for (Karte k : kartenInHand) {
            k.verstecken();
        }
    }
    
    /**
     * Gibt zurück, wie viele Karten aktuell in der Hand sind.
     */
    public int anzahlKarten() {
        return kartenInHand.size();
    }
}