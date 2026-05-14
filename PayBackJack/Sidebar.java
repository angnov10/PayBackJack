import ea.edu.*;

/**
 * Die permanente Sidebar links im Bild.
 * Zeigt: Kontostand, Hunger, Durst, Alkohol, Items, Szenen-Wechsel.
 * Breite: ca. 1/5 von 1920 = 384px
 */
public class Sidebar {
    
    // Hintergrund
    private BildE hintergrund;
    
    // Inventar Anzeigen
    private TextE[] inventarSlots = new TextE[6];
    private BildE[][] itemIcons = new BildE[6][5]; // 6 Slots, 5 Item-Typen
    
    // Text-Anzeigen
    private TextE titelText;
    private TextE geldText;
    private TextE einsatzText;
    private TextE rundenText;
    
    // Balken-Hintergründe (grau)
    private RechteckE hungerBg, durstBg, alkoholBg;
    // Balken-Füllungen (farbig)
    private RechteckE hungerBar, durstBar, alkoholBar;
    // Balken-Labels
    private TextE hungerLabel, durstLabel, alkoholLabel;
    
    // Items
    private TextE itemsTitle;
    private TextE lupeText;
    private TextE zigarettenText;
    
    // Szenen-Wechsel Buttons (Text)
    private TextE btnBlackjack;
    private TextE btnBar;
    private TextE aktuellerOrt;
    
    // Ziel-Anzeige
    private TextE zielText;
    
    private static final int SIDEBAR_X = 120;
    private static final int SIDEBAR_BREITE = 480;
    private static final int SIDEBAR_ZENTRUM_X = SIDEBAR_X + (SIDEBAR_BREITE / 2);
    private static final int BALKEN_BREITE = 350;
    private static final int BALKEN_HOEHE = 30;
    
    // Inventar Anzeigen
    // (Bereits oben deklariert)
    
    public Sidebar() {
        // Hintergrund der Sidebar (Startet bei X=120, Breite 480)
        hintergrund = new BildE(SIDEBAR_X, 0, "/Users/an/PayBackJack/assets/Sprites/Sidebar/Background_160x360.png");
        
        // === TITEL ===
        titelText = new TextE("PAYBACK JACK");
        titelText.positionSetzen(SIDEBAR_ZENTRUM_X - 100, 10);
        titelText.farbeSetzen("Rot");
        titelText.groesseSetzen(28);
        
        // === GELDSTAND BOX (180, 36 to 540, 156) ===
        geldText = new TextE("Konto: 1000€");
        geldText.positionSetzen(SIDEBAR_X + 60, 50);
        geldText.farbeSetzen("Gelb");
        geldText.groesseSetzen(20);
        
        einsatzText = new TextE("Einsatz: 50€");
        einsatzText.positionSetzen(SIDEBAR_X + 60, 85);
        einsatzText.farbeSetzen("Weiß");
        
        zielText = new TextE("Ziel: 100.000€");
        zielText.positionSetzen(SIDEBAR_X + 60, 120);
        zielText.farbeSetzen("Grün");
        
        rundenText = new TextE("Runde: 0");
        rundenText.positionSetzen(SIDEBAR_X + 280, 50);
        rundenText.farbeSetzen("Weiß");
        
        // === LEISTEN BOX (186, 210 to 540, 438) ===
        int leistenStartX = SIDEBAR_X + 66;
        int leistenStartY = 210;
        
        hungerLabel = new TextE("Hunger");
        hungerLabel.positionSetzen(leistenStartX, leistenStartY + 10);
        hungerLabel.farbeSetzen("Weiß");
        
        hungerBg = new RechteckE();
        hungerBg.breiteSetzen(BALKEN_BREITE);
        hungerBg.hoeheSetzen(BALKEN_HOEHE);
        hungerBg.positionSetzen(leistenStartX, leistenStartY + 40);
        hungerBg.farbeSetzen("Grau");
        
        hungerBar = new RechteckE();
        hungerBar.breiteSetzen(BALKEN_BREITE);
        hungerBar.hoeheSetzen(BALKEN_HOEHE);
        hungerBar.positionSetzen(leistenStartX, leistenStartY + 40);
        hungerBar.farbeSetzen("Grün");
        
        durstLabel = new TextE("Durst");
        durstLabel.positionSetzen(leistenStartX, leistenStartY + 80);
        durstLabel.farbeSetzen("Weiß");
        
        durstBg = new RechteckE();
        durstBg.breiteSetzen(BALKEN_BREITE);
        durstBg.hoeheSetzen(BALKEN_HOEHE);
        durstBg.positionSetzen(leistenStartX, leistenStartY + 110);
        durstBg.farbeSetzen("Grau");
        
        durstBar = new RechteckE();
        durstBar.breiteSetzen(BALKEN_BREITE);
        durstBar.hoeheSetzen(BALKEN_HOEHE);
        durstBar.positionSetzen(leistenStartX, leistenStartY + 110);
        durstBar.farbeSetzen("Blau");
        
        alkoholLabel = new TextE("Alkohol");
        alkoholLabel.positionSetzen(leistenStartX, leistenStartY + 150);
        alkoholLabel.farbeSetzen("Weiß");
        
        alkoholBg = new RechteckE();
        alkoholBg.breiteSetzen(BALKEN_BREITE);
        alkoholBg.hoeheSetzen(BALKEN_HOEHE);
        alkoholBg.positionSetzen(leistenStartX, leistenStartY + 180);
        alkoholBg.farbeSetzen("Grau");
        
        alkoholBar = new RechteckE();
        alkoholBar.breiteSetzen(0);
        alkoholBar.hoeheSetzen(BALKEN_HOEHE);
        alkoholBar.positionSetzen(leistenStartX, leistenStartY + 180);
        alkoholBar.farbeSetzen("Rot");
        
        // === ITEMS BOX (180, 492 to 540, 900) ===
        int itemsStartX = SIDEBAR_X + 60;
        int itemsStartY = 492;
        
        itemsTitle = new TextE("-- Inventar --");
        itemsTitle.positionSetzen(itemsStartX, itemsStartY + 10);
        itemsTitle.farbeSetzen("Gelb");
        
        String[] itemNamen = {"Suppe", "Wasser", "Bier", "Zigarette", "Lupe"};
        
        // 6 Slots in 2 Spalten
        for (int i = 0; i < 6; i++) {
            int col = i % 2;
            int row = i / 2;
            int slotX = itemsStartX + (col * 180) + 10;
            int slotY = itemsStartY + 60 + (row * 100);
            
            inventarSlots[i] = new TextE("[" + (i+3) + "] Leer");
            inventarSlots[i].positionSetzen(slotX, slotY + 75);
            inventarSlots[i].farbeSetzen("Weiß");
            inventarSlots[i].groesseSetzen(12);
            
            // Alle 5 möglichen Icons für diesen Slot erstellen
            for (int j = 0; j < 5; j++) {
                String pfad = "/Users/an/PayBackJack/assets/Sprites/Items/" + itemNamen[j] + "_20x20.png";
                itemIcons[i][j] = new BildE(slotX + 50, slotY, pfad);
                itemIcons[i][j].sichtbarSetzen(false);
            }
        }
        
        lupeText = new TextE("Lupe: Nein");
        lupeText.positionSetzen(itemsStartX, itemsStartY + 360);
        lupeText.farbeSetzen("Weiß");
        
        // === BAR TISCH WECHSEL (180, 936 to 540, 1044) ===
        int switchStartY = 936;
        aktuellerOrt = new TextE("-- Ort --");
        aktuellerOrt.positionSetzen(itemsStartX, switchStartY + 10);
        aktuellerOrt.farbeSetzen("Gelb");
        
        btnBlackjack = new TextE("[1] Tisch");
        btnBlackjack.positionSetzen(itemsStartX, switchStartY + 45);
        btnBlackjack.farbeSetzen("Weiß");
        
        btnBar = new TextE("[2] Bar");
        btnBar.positionSetzen(itemsStartX + 180, switchStartY + 45);
        btnBar.farbeSetzen("Weiß");
    }
    
    /**
     * Aktualisiert alle Anzeigen basierend auf dem Spielstand.
     */
    public void aktualisieren(Spielstand s) {
        geldText.inhaltSetzen("Konto: " + s.getGeld() + "€");
        einsatzText.inhaltSetzen("Einsatz: " + s.getEinsatz() + "€");
        rundenText.inhaltSetzen("Runde: " + s.getRundenGespielt());
        
        // Hunger-Balken
        int hungerBreite = (int)(BALKEN_BREITE * (s.getHunger() / 100.0));
        hungerBar.breiteSetzen(Math.max(1, hungerBreite));
        if (s.getHunger() < 20) hungerBar.farbeSetzen("Rot");
        else hungerBar.farbeSetzen("Grün");
        
        // Durst-Balken
        int durstBreite = (int)(BALKEN_BREITE * (s.getDurst() / 100.0));
        durstBar.breiteSetzen(Math.max(1, durstBreite));
        if (s.getDurst() < 20) durstBar.farbeSetzen("Rot");
        else durstBar.farbeSetzen("Blau");
        
        // Alkohol-Balken
        int alkoholBreite = (int)(BALKEN_BREITE * (s.getAlkohol() / 100.0));
        alkoholBar.breiteSetzen(Math.max(1, alkoholBreite));
        if (s.getAlkohol() > 80) alkoholBar.farbeSetzen("Rot");
        else alkoholBar.farbeSetzen("Gelb");
        
        // Items
        if (s.hatLupe()) {
            if (s.kannLupeNutzen()) {
                lupeText.inhaltSetzen("Lupe: Bereit [L]");
                lupeText.farbeSetzen("Grün");
            } else {
                lupeText.inhaltSetzen("Lupe: Cool [L]");
                lupeText.farbeSetzen("Rot");
            }
        } else {
            lupeText.inhaltSetzen("Lupe: Nein");
            lupeText.farbeSetzen("Weiß");
        }
        
        // Inventar Slots
        String[] inv = s.getInventar();
        String[] itemNamen = {"Suppe", "Wasser", "Bier", "Zigarette", "Lupe"};
        
        for (int i = 0; i < 6; i++) {
            // Erstmal alle Icons für diesen Slot verstecken
            for (int j = 0; j < 5; j++) itemIcons[i][j].sichtbarSetzen(false);
            
            if (inv[i] != null) {
                inventarSlots[i].inhaltSetzen("[" + (i+3) + "] " + inv[i]);
                inventarSlots[i].farbeSetzen("Grün");
                
                // Passendes Icon finden und anzeigen
                for (int j = 0; j < 5; j++) {
                    if (inv[i].equals(itemNamen[j])) {
                        itemIcons[i][j].sichtbarSetzen(true);
                        break;
                    }
                }
            } else {
                inventarSlots[i].inhaltSetzen("[" + (i+3) + "] Leer");
                inventarSlots[i].farbeSetzen("Weiß");
            }
        }
        
        // Geld-Farbe
        if (s.getGeld() < 100) {
            geldText.farbeSetzen("Rot");
        } else {
            geldText.farbeSetzen("Gelb");
        }
    }
    
    /**
     * Markiert welche Szene aktiv ist.
     */
    public void szeneMarkieren(String szene) {
        if (szene.equals("blackjack")) {
            btnBlackjack.farbeSetzen("Gelb");
            btnBar.farbeSetzen("Weiß");
        } else {
            btnBlackjack.farbeSetzen("Weiß");
            btnBar.farbeSetzen("Gelb");
        }
    }
}
