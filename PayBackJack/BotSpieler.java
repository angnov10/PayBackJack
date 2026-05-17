import java.util.ArrayList;

/**
 * Ein Bot-Spieler am BlackJack-Tisch.
 * Einfache KI: Zieht bis 14 (75% der Zeit), sonst bis 12.
 */
public class BotSpieler {
    private String name;
    private Hand hand;
    private boolean fertig;
    
    public BotSpieler(String pName) {
        name = pName;
        hand = new Hand();
        fertig = false;
    }
    
    public String getName() { return name; }
    public Hand getHand() { return hand; }
    public boolean istFertig() { return fertig; }
    
    /**
     * Bot entscheidet ob er zieht oder steht.
     * 75%: Zieht bis Punkte >= 14
     * 25%: Zieht bis Punkte >= 12
     */
    public boolean willZiehen() {
        if (fertig) return false;
        
        int punkte = hand.punkteBerechnen();
        if (punkte >= 21) {
            fertig = true;
            return false;
        }
        
        // 75% Chance: Zieht bis 14
        double zufall = Math.random();
        int grenze = (zufall < 0.75) ? 14 : 12;
        
        if (punkte >= grenze) {
            fertig = true;
            return false;
        }
        return true;
    }
    
    /**
     * Setzt den Bot für eine neue Runde zurück.
     */
    public void zuruecksetzen(Kartenstapel deck) {
        hand.kartenZurueckgeben(deck);
        fertig = false;
    }
    
    /**
     * Zeigt die Karten des Bots an einer Position an.
     */
    public void kartenAnzeigen(int x, int y) {
        hand.alleAnzeigenGedreht(x, y, -30);
    }
    
    /**
     * Zeigt die Karten des Bots zentriert an.
     */
    public void kartenZentriertAnzeigen(int centerX, int y) {
        hand.alleZentriertAnzeigen(centerX, y, false);
    }
    
    /**
     * Versteckt die Karten des Bots.
     */
    public void kartenZurueckgeben(Kartenstapel deck) {
        hand.kartenZurueckgeben(deck);
    }
}
