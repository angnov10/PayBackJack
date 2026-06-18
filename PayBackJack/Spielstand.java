/**
 * Globaler Spielstand - speichert alles was zwischen Szenen geteilt wird.
 */
public class Spielstand {
    private int geld;
    private int einsatz;
    private int hunger;      // 0-100, bei 0 -> Kollaps
    private int durst;       // 0-100, bei 0 -> Kollaps
    private int alkohol;     // 0-100, bei 100 -> Kollaps
    private int verdacht;    // 0-100
    private int zeitVerbleibend; // in Sekunden, max 900
    private int rundenGespielt;
    
    // Items & Effekte
    private int lupeAbklingzeit;  // Runden bis nächster Einsatz
    private boolean nauseaEffekt;
    private int nauseaRunden;
    private boolean strafeErhalten;
    private boolean letzteRundeGeschummelt; // für Durchsuchungs-Chance
    
    // Inventar (6 Slots)
    private String[] inventar = new String[6];
    
    // Spielziel
    private static final int ZIEL_BETRAG = 10000; // Angepasst auf 10k
    private static final int KOLLAPS_STRAFE = 5000;
    
    public Spielstand() {
        geld = 1000;
        einsatz = 50;
        hunger = 90;
        durst = 90;
        alkohol = 0;
        verdacht = 0;
        zeitVerbleibend = 900; // 15 Minuten
        rundenGespielt = 0;
        lupeAbklingzeit = 0;
        nauseaEffekt = false;
        nauseaRunden = 0;
        strafeErhalten = false;
        letzteRundeGeschummelt = false;
    }
    
    // === GELD ===
    public int getGeld() { return geld; }
    public void geldAendern(int betrag) { geld += betrag; }
    public void setGeld(int betrag) { geld = betrag; }
    public int getEinsatz() { return einsatz; }
    public void setEinsatz(int e) { einsatz = e; }
    
    public boolean istPleite() { return geld < 0 || zeitVerbleibend <= 0; }
    public boolean hatGewonnen() { return geld >= ZIEL_BETRAG; }
    public int getZielBetrag() { return ZIEL_BETRAG; }
    
    // === STATS ===
    public int getTischLevel() {
        if (geld >= 5000) return 2;
        if (geld >= 2000) return 1;
        return 0;
    }
    
    public int getHunger() { return hunger; }
    public int getDurst() { return durst; }
    public int getAlkohol() { return alkohol; }
    public int getVerdacht() { return verdacht; }
    public int getZeitVerbleibend() { return zeitVerbleibend; }
    public boolean hatNausea() { return nauseaEffekt; }
    
    public void hungerAendern(int wert) { 
        hunger = Math.max(0, Math.min(100, hunger + wert)); 
    }
    public void durstAendern(int wert) { 
        durst = Math.max(0, Math.min(100, durst + wert)); 
    }
    public void alkoholAendern(int wert) { 
        alkohol = Math.max(0, Math.min(100, alkohol + wert)); 
    }
    public void verdachtAendern(int wert) {
        verdacht = Math.max(0, Math.min(100, verdacht + wert));
    }
    public void setVerdacht(int wert) {
        verdacht = Math.max(0, Math.min(100, wert));
    }
    
    public void zeitAbziehen(int sekunden) {
        zeitVerbleibend = Math.max(0, zeitVerbleibend - sekunden);
    }
    
    public void setGeschummelt(boolean val) {
        letzteRundeGeschummelt = val;
    }
    
    public boolean hatGeschummelt() {
        return letzteRundeGeschummelt;
    }
    
    /**
     * Wird nach jeder BlackJack-Runde aufgerufen.
     */
    public void rundeVorbei() {
        rundenGespielt++;
        
        // Schwellenwerte (Hunger/Durst < 20 entspricht im Konzept >80% Verwahrlosung)
        if (hunger < 20) geld -= 50;
        if (durst < 20) verdachtAendern(5);
        if (hunger < 10 && durst < 10 && !strafeErhalten) {
            geld -= 5000;
            strafeErhalten = true;
        }
        
        hunger -= 6;  // War 8 – jetzt mehr Spielraum
        durst -= 8;   // War 10 – jetzt mehr Spielraum
        if (alkohol > 0) alkohol -= 2;
        if (lupeAbklingzeit > 0) lupeAbklingzeit--;
        
        if (nauseaRunden > 0) {
            nauseaRunden--;
            if (nauseaRunden <= 0) nauseaEffekt = false;
        }
        
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
    
    // === KAUFEN ===
    public boolean kaufen(String item, int preis) {
        if (geld >= preis && itemHinzufuegen(item)) {
            geld -= preis;
            return true;
        }
        return false;
    }
    
    public void inventarLeeren() {
        for (int i = 0; i < inventar.length; i++) {
            inventar[i] = null;
        }
    }
    
    public String itemNutzen(int slot) {
        if (slot < 0 || slot >= inventar.length || inventar[slot] == null) return null;
        
        String item = inventar[slot];
        boolean verbraucht = true; // Fast alle Items werden verbraucht
        
        if (item.equals("Suppe")) {
            hungerAendern(40);
        } else if (item.equals("VerdorbeneSuppe")) {
            hungerAendern(40);
            if (Math.random() < 0.3) {
                nauseaEffekt = true;
                nauseaRunden = 2;
            }
        } else if (item.equals("Wasser")) {
            durstAendern(30);
        } else if (item.equals("SchmutzigesWasser")) {
            durstAendern(30);
            verdachtAendern(10);
        } else if (item.equals("Bier")) {
            alkoholAendern(15);
            durstAendern(15);
            verdachtAendern(-15);
        } else if (item.equals("Zigarette")) {
            einsatz += (einsatz / 2);
            verdachtAendern(20);
        } else if (item.equals("Lupe")) {
            verbraucht = false; 
        } else if (item.equals("Ass")) {
            verbraucht = true;
        }
        
        if (verbraucht) {
            inventar[slot] = null;
        }
        return item; // Gebe zurück was genutzt wurde
    }
    
    public int getRundenGespielt() { return rundenGespielt; }
    public int getKollapStrafe() { return KOLLAPS_STRAFE; }
}
