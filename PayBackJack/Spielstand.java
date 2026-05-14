/**
 * Globaler Spielstand - speichert alles was zwischen Szenen geteilt wird.
 */
public class Spielstand {
    private int geld;
    private int einsatz;
    private int hunger;      // 0-100, bei 0 -> Kollaps
    private int durst;       // 0-100, bei 0 -> Kollaps
    private int alkohol;     // 0-100, bei 100 -> Kollaps
    private int rundenGespielt;
    
    // Items
    private int lupeAbklingzeit;  // Runden bis nächster Einsatz
    private int zigaretten;      // Anzahl geraucht (stackable)
    
    // Inventar (6 Slots)
    private String[] inventar = new String[6];
    
    // Spielziel
    private static final int ZIEL_BETRAG = 100000;
    private static final int KOLLAPS_STRAFE = 5000;
    
    public Spielstand() {
        geld = 1000;
        einsatz = 50;
        hunger = 80;
        durst = 80;
        alkohol = 0;
        rundenGespielt = 0;
        lupeAbklingzeit = 0;
        zigaretten = 0;
    }
    
    // === GELD ===
    public int getGeld() { return geld; }
    public void geldAendern(int betrag) { geld += betrag; }
    public int getEinsatz() { return einsatz; }
    public void setEinsatz(int e) { einsatz = e; }
    
    public boolean istPleite() { return geld < 0; }
    public boolean hatGewonnen() { return geld >= ZIEL_BETRAG; }
    public int getZielBetrag() { return ZIEL_BETRAG; }
    
    // === STATS ===
    public int getHunger() { return hunger; }
    public int getDurst() { return durst; }
    public int getAlkohol() { return alkohol; }
    
    public void hungerAendern(int wert) { 
        hunger = Math.max(0, Math.min(100, hunger + wert)); 
    }
    public void durstAendern(int wert) { 
        durst = Math.max(0, Math.min(100, durst + wert)); 
    }
    public void alkoholAendern(int wert) { 
        alkohol = Math.max(0, Math.min(100, alkohol + wert)); 
    }
    
    /**
     * Wird nach jeder BlackJack-Runde aufgerufen.
     * Hunger und Durst sinken, Alkohol baut sich leicht ab.
     */
    public void rundeVorbei() {
        rundenGespielt++;
        hunger -= 3;
        durst -= 4;
        if (alkohol > 0) alkohol -= 1;
        if (lupeAbklingzeit > 0) lupeAbklingzeit--;
        
        // Grenzen einhalten
        hunger = Math.max(0, hunger);
        durst = Math.max(0, durst);
        alkohol = Math.max(0, alkohol);
    }
    
    /**
     * Prüft ob der Spieler kollabiert (Hunger oder Durst bei 0, oder Alkohol bei 100).
     */
    public boolean istKollabiert() {
        return hunger <= 0 || durst <= 0 || alkohol >= 100;
    }
    
    /**
     * Führt Kollaps-Strafe durch und setzt Stats zurück.
     */
    public void kollapsDurchfuehren() {
        geld -= KOLLAPS_STRAFE;
        hunger = 50;
        durst = 50;
        alkohol = 30;
    }
    
    // === ITEMS ===
    public boolean hatLupe() { 
        for (String s : inventar) {
            if ("Lupe".equals(s)) return true;
        }
        return false;
    }
    public boolean kannLupeNutzen() { return hatLupe() && lupeAbklingzeit <= 0; }
    public void lupeBenutzen() { lupeAbklingzeit = 10; }
    
    public int getZigaretten() { return zigaretten; }
    public void zigaretteKaufen() {
        if (geld >= 500) {
            if (itemHinzufuegen("Zigarette")) {
                geld -= 500;
            }
        }
    }
    
    // === INVENTAR LOGIK ===
    public String[] getInventar() { return inventar; }
    
    public boolean itemHinzufuegen(String item) {
        for (int i = 0; i < inventar.length; i++) {
            if (inventar[i] == null) {
                inventar[i] = item;
                return true;
            }
        }
        return false;
    }
    
    public void itemNutzen(int slot) {
        if (slot < 0 || slot >= inventar.length || inventar[slot] == null) return;
        
        String item = inventar[slot];
        if (item.equals("Suppe")) {
            hungerAendern(30);
        } else if (item.equals("Wasser")) {
            durstAendern(20);
        } else if (item.equals("Bier")) {
            alkoholAendern(15);
            durstAendern(10);
        } else if (item.equals("Zigarette")) {
            zigaretten++;
            inventar[slot] = null;
        } else if (item.equals("Lupe")) {
            // Lupe wird nicht verbraucht durch "Nutzen" Taste 3-8
            // Sie wird über L am Tisch benutzt
        }
    }
    
    /**
     * Glücks-Bonus durch Zigaretten (5% pro Stück).
     * Gibt einen Wert zwischen 0.0 und 1.0 zurück.
     */
    public double getGluecksBonus() {
        return zigaretten * 0.05;
    }
    
    public int getRundenGespielt() { return rundenGespielt; }
    public int getKollapStrafe() { return KOLLAPS_STRAFE; }
    
    // === BAR ITEMS KAUFEN ===
    public boolean lupeKaufen() {
        if (geld >= 10000 && itemHinzufuegen("Lupe")) { 
            geld -= 10000; 
            return true; 
        }
        return false;
    }
    public boolean suppeKaufen() {
        if (geld >= 10 && itemHinzufuegen("Suppe")) { 
            geld -= 10; 
            return true; 
        }
        return false;
    }
    public boolean wasserKaufen() {
        if (geld >= 6 && itemHinzufuegen("Wasser")) { 
            geld -= 6; 
            return true; 
        }
        return false;
    }
    public boolean bierKaufen() {
        if (geld >= 3 && itemHinzufuegen("Bier")) { 
            geld -= 3; 
            return true; 
        }
        return false;
    }
}
