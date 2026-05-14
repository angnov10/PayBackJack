import ea.edu.*;
import ea.Taste;

/**
 * PayBackJack - Das Hauptspiel.
 * Verbindet alle Szenen, die Sidebar und den Spielstand.
 * 
 * 1920x1080 Auflösung.
 * Links: Sidebar (350px) | Rechts: Spielszene
 * 
 * Szenen:   [1] Blackjack | [2] Bar
 * Spielen:  [H] Hit | [S] Stand | [D] Double | [L] Lupe
 * Einsatz:  [Pfeiltasten] | [LEERTASTE] Starten
 * Bar:      [3] Suppe | [4] Wasser | [5] Bier | [6] Lupe | [7] Zigarette
 */
public class PayBackJack extends SPIEL {
    
    // Szenen
    private String aktuelleSzene; // "cutscene", "blackjack", "bar"
    
    // Spielstand
    private Spielstand spielstand;
    
    // UI
    private Sidebar sidebar;
    
    // Szenen-Objekte
    private BarSzene barSzene;
    private CutsceneSzene cutscene;
    
    // Blackjack-Tisch Elemente
    private BildE tischHintergrund;
    private TextE bjStatus;
    private TextE bjSpielerLabel;
    private TextE bjDealerLabel;
    private TextE bjSpielerPunkte;
    private TextE bjDealerPunkte;
    
    // Blackjack-Logik
    private Kartenstapel deck;
    private Hand spielerHand;
    private Hand dealerHand;
    private BotSpieler bot1, bot2, bot3;
    private TextE bot1Label, bot2Label, bot3Label;
    
    private boolean rundeLaeuft;
    private boolean setzPhase;
    private boolean dealerKarteSichtbar; // Lupe-Effekt
    
    private static final int BJ_X = 380; // Start rechts von Sidebar
    
    // Setzt die Skalierung des Fensters fuer MacBooks herab, ohne die internen 1920x1080 Koordinaten zu aendern
    static {
        System.setProperty("sun.java2d.uiScale", "0.85");
    }
    
    public PayBackJack() {
        super(1920, 1080, false, false, true);
        
        spielstand = new Spielstand();
        barSzene = new BarSzene();
        cutscene = new CutsceneSzene();
        
        // === BLACKJACK TISCH AUFBAUEN ===
        tischHintergrund = new BildE(0, 0, "/Users/an/PayBackJack/assets/Sprites/Tisch/Background_640x360.png");
        tischHintergrund.sichtbarSetzen(false);
        
        bjStatus = new TextE("Druecke LEERTASTE zum Starten");
        bjStatus.positionSetzen(700, 30);
        bjStatus.farbeSetzen("Gelb");
        bjStatus.groesseSetzen(22);
        bjStatus.sichtbarSetzen(false);
        
        bjDealerLabel = new TextE("DEALER");
        bjDealerLabel.positionSetzen(714, 20);
        bjDealerLabel.farbeSetzen("Weiß");
        bjDealerLabel.groesseSetzen(18);
        bjDealerLabel.sichtbarSetzen(false);
        
        bjDealerPunkte = new TextE("");
        bjDealerPunkte.positionSetzen(714, 50);
        bjDealerPunkte.farbeSetzen("Weiß");
        bjDealerPunkte.sichtbarSetzen(false);
        
        bjSpielerLabel = new TextE("DEINE HAND");
        bjSpielerLabel.positionSetzen(714, 730);
        bjSpielerLabel.farbeSetzen("Weiß");
        bjSpielerLabel.groesseSetzen(18);
        bjSpielerLabel.sichtbarSetzen(false);
        
        bjSpielerPunkte = new TextE("");
        bjSpielerPunkte.positionSetzen(960, 710); // Mittig ueber Karten
        bjSpielerPunkte.farbeSetzen("Weiß");
        bjSpielerPunkte.sichtbarSetzen(false);
        
        // Bot-Spieler (Nur einer laut User)
        bot1 = new BotSpieler("Max");
        bot2 = null; // Deaktiviert
        bot3 = null; // Deaktiviert
        
        bot1Label = new TextE("Max");
        bot1Label.positionSetzen(1428, 500);
        bot1Label.farbeSetzen("Weiß");
        bot1Label.sichtbarSetzen(false);
        
        bot2Label = null;
        bot3Label = null;
        
        rundeLaeuft = false;
        setzPhase = true;
        dealerKarteSichtbar = false;
        
        // Deck EINMALIG initialisieren, Karten werden immer zurueckgegeben
        deck = new Kartenstapel();
        spielerHand = new Hand();
        dealerHand = new Hand();
        
        // Spiel startet mit Intro-Cutscene
        sidebar = new Sidebar();
        aktuelleSzene = "cutscene";
        cutscene.ladeIntro();
        cutscene.anzeigen();
        sidebar.aktualisieren(spielstand);
    }
    
    @Override
    public void tasteReagieren(int taste) {
        
        // === CUTSCENE ===
        if (aktuelleSzene.equals("cutscene")) {
            if (taste == Taste.LEERTASTE) {
                boolean fertig = cutscene.weiter();
                if (fertig) {
                    // Nach Intro -> Blackjack Tisch
                    if (!spielstand.istPleite() && !spielstand.hatGewonnen()) {
                        wechselZuBlackjack();
                    }
                }
            }
            return;
        }
        
        // === SZENEN-WECHSEL (immer verfügbar) ===
        if (taste == Taste._1 && !rundeLaeuft) {
            wechselZuBlackjack();
            return;
        }
        if (taste == Taste._2 && !rundeLaeuft) {
            wechselZuBar();
            return;
        }
        
        // === BLACKJACK SZENE ===
        if (aktuelleSzene.equals("blackjack")) {
            blackjackTaste(taste);
        }
        
        // === BAR SZENE ===
        if (aktuelleSzene.equals("bar")) {
            barTaste(taste);
        }
        
        // === INVENTAR NUTZEN (3-8) - Nur am Tisch möglich ===
        if (aktuelleSzene.equals("blackjack") && taste >= 3 && taste <= 8) {
            spielstand.itemNutzen(taste - 3);
            sidebar.aktualisieren(spielstand);
            bjSpielerPunkte.inhaltSetzen("Punkte: " + spielerHand.punkteBerechnen());
        }
    }
    
    // ==========================================
    //            SZENEN-WECHSEL
    // ==========================================
    
    private void wechselZuBlackjack() {
        barSzene.verstecken();
        cutscene.verstecken();
        blackjackAnzeigen();
        aktuelleSzene = "blackjack";
        sidebar.szeneMarkieren("blackjack");
        setzPhase = true;
        bjStatus.inhaltSetzen("Pfeiltasten = Einsatz | LEERTASTE = Start");
    }
    
    private void wechselZuBar() {
        blackjackVerstecken();
        cutscene.verstecken();
        barSzene.anzeigen();
        barSzene.aktualisieren(spielstand);
        aktuelleSzene = "bar";
        sidebar.szeneMarkieren("bar");
    }
    
    // ==========================================
    //            BLACKJACK LOGIK
    // ==========================================
    
    private void blackjackTaste(int taste) {
        if (setzPhase) {
            if (taste == Taste.OBEN) {
                int neuerEinsatz = spielstand.getEinsatz() + 50;
                if (neuerEinsatz <= spielstand.getGeld()) {
                    spielstand.setEinsatz(neuerEinsatz);
                    sidebar.aktualisieren(spielstand);
                    bjStatus.inhaltSetzen("Einsatz: " + neuerEinsatz + "€ | LEERTASTE = Start");
                }
            }
            if (taste == Taste.UNTEN) {
                int neuerEinsatz = spielstand.getEinsatz() - 50;
                if (neuerEinsatz >= 50) {
                    spielstand.setEinsatz(neuerEinsatz);
                    sidebar.aktualisieren(spielstand);
                    bjStatus.inhaltSetzen("Einsatz: " + neuerEinsatz + "€ | LEERTASTE = Start");
                }
            }
            if (taste == Taste.LEERTASTE) {
                starteRunde();
            }
            return;
        }
        
        if (rundeLaeuft) {
            if (taste == Taste.H) hit();
            if (taste == Taste.S) stand();
            if (taste == Taste.D && spielerHand.anzahlKarten() == 2) doubleDown();
            if (taste == Taste.L) lupeBenuetzen();
        } else {
            // Nach Runde -> LEERTASTE für neue Setz-Phase
            if (taste == Taste.LEERTASTE) {
                pruefeSpielstatus();
            }
        }
    }
    
    private void starteRunde() {
        setzPhase = false;
        rundeLaeuft = true;
        dealerKarteSichtbar = false;
        
        int einsatz = spielstand.getEinsatz();
        spielstand.geldAendern(-einsatz);
        
        // Alte Karten verstecken und in den Stapel zurueckgeben
        spielerHand.kartenZurueckgeben(deck);
        dealerHand.kartenZurueckgeben(deck);
        if (bot1 != null) bot1.zuruecksetzen(deck);
        
        // Stapel mischen
        deck.mischen();
        
        // Austeilen: Spieler, Bots und Dealer
        spielerHand.karteHinzufuegen(deck.karteZiehen());
        if (bot1 != null) bot1.getHand().karteHinzufuegen(deck.karteZiehen());
        dealerHand.karteHinzufuegen(deck.karteZiehen());
        
        spielerHand.karteHinzufuegen(deck.karteZiehen());
        if (bot1 != null) bot1.getHand().karteHinzufuegen(deck.karteZiehen());
        dealerHand.karteHinzufuegen(deck.karteZiehen()); // Dealer bekommt 2. Karte
        
        // Bots spielen sofort
        botsSpielenLassen();
        
        // Anzeigen (Dealer Karte an Index 1 ist verdeckt)
        // Linke Kanten sind aligned bei 714
        spielerHand.alleZentriertAnzeigen(960, 768, false); // Center 960, Y 768
        dealerHand.positionAnzeigen(714, 78, true); // Dealer nicht zentriert, Linke Kante 714
        
        if (bot1 != null) {
            bot1.kartenAnzeigen(1428, 720); // Nach deinen Eckwerten
            bot1.getHand().alleDrehen(30);
        }
        
        bjSpielerPunkte.inhaltSetzen("Punkte: " + spielerHand.punkteBerechnen());
        bjDealerPunkte.inhaltSetzen("Dealer: ?");
        bjStatus.inhaltSetzen("[H] Hit | [S] Stand | [D] Double | [L] Lupe");
        
        sidebar.aktualisieren(spielstand);
        
        // Sofort BlackJack prüfen
        if (spielerHand.punkteBerechnen() == 21) {
            gewinn(2.5);
            bjStatus.inhaltSetzen("BLACKJACK! Gewinn: " + (int)(einsatz * 2.5) + "€");
        }
    }
    
    private void botsSpielenLassen() {
        if (bot1 != null) {
            while (bot1.willZiehen()) bot1.getHand().karteHinzufuegen(deck.karteZiehen());
        }
    }
    
    private void hit() {
        spielerHand.karteHinzufuegen(deck.karteZiehen());
        spielerHand.alleZentriertAnzeigen(960, 768, false);
        int p = spielerHand.punkteBerechnen();
        bjSpielerPunkte.inhaltSetzen("Punkte: " + p);
        bjStatus.inhaltSetzen("[H] Hit | [S] Stand");
        
        if (p > 21) {
            bjStatus.inhaltSetzen("BUST! " + spielstand.getEinsatz() + "€ verloren.");
            rundeBeenden();
        }
    }
    
    private void doubleDown() {
        int einsatz = spielstand.getEinsatz();
        if (spielstand.getGeld() >= einsatz) {
            spielstand.geldAendern(-einsatz);
            spielstand.setEinsatz(einsatz * 2);
            
            spielerHand.karteHinzufuegen(deck.karteZiehen());
            spielerHand.alleZentriertAnzeigen(960, 768, false);
            bjSpielerPunkte.inhaltSetzen("Punkte: " + spielerHand.punkteBerechnen());
            
            if (spielerHand.punkteBerechnen() > 21) {
                bjStatus.inhaltSetzen("BUST nach Double! Verloren.");
                rundeBeenden();
            } else {
                stand();
            }
        } else {
            bjStatus.inhaltSetzen("Nicht genug Geld zum Verdoppeln!");
        }
    }
    
    private void stand() {
        // Dealer deckt auf
        dealerHand.positionAnzeigen(714, 78, false);
        
        // Dealer zieht
        while (dealerHand.punkteBerechnen() < 17) {
            dealerHand.karteHinzufuegen(deck.karteZiehen());
        }
        dealerHand.positionAnzeigen(714, 78, false);
        
        int sP = spielerHand.punkteBerechnen();
        int dP = dealerHand.punkteBerechnen();
        int einsatz = spielstand.getEinsatz();
        bjDealerPunkte.inhaltSetzen("Dealer: " + dP);
        
        if (dP > 21 || sP > dP) {
            gewinn(2.0);
            bjStatus.inhaltSetzen("Gewonnen! +" + einsatz + "€");
        } else if (sP < dP) {
            bjStatus.inhaltSetzen("Dealer gewinnt. -" + einsatz + "€");
            rundeBeenden();
        } else {
            gewinn(1.0); // Push
            bjStatus.inhaltSetzen("Push - Einsatz zurueck.");
        }
    }
    
    private void lupeBenuetzen() {
        if (spielstand.kannLupeNutzen() && rundeLaeuft && !dealerKarteSichtbar) {
            spielstand.lupeBenutzen();
            bjDealerPunkte.inhaltSetzen("Dealer: " + dealerHand.punkteBerechnen());
            dealerKarteSichtbar = true;
            dealerHand.positionAnzeigen(714, 78, false);
        }
    }
    
    private void gewinn(double multiplikator) {
        int gewinn = (int)(spielstand.getEinsatz() * multiplikator);
        spielstand.geldAendern(gewinn);
        rundeBeenden();
    }
    
    private void rundeBeenden() {
        rundeLaeuft = false;
        spielstand.rundeVorbei();
        
        // Kollaps prüfen
        if (spielstand.istKollabiert()) {
            spielstand.kollapsDurchfuehren();
            bjStatus.inhaltSetzen("KOLLABIERT! -5000€ Strafe. [LEERTASTE]");
        }
        
        sidebar.aktualisieren(spielstand);
        
        // Einsatz zurücksetzen nach Double
        if (spielstand.getEinsatz() > spielstand.getGeld() && spielstand.getGeld() > 0) {
            spielstand.setEinsatz(50);
        }
    }
    
    private void pruefeSpielstatus() {
        if (spielstand.hatGewonnen()) {
            blackjackVerstecken();
            cutscene.ladeGewinn();
            cutscene.anzeigen();
            aktuelleSzene = "cutscene";
        } else if (spielstand.istPleite()) {
            blackjackVerstecken();
            cutscene.ladeVerlust();
            cutscene.anzeigen();
            aktuelleSzene = "cutscene";
        } else {
            setzPhase = true;
            bjStatus.inhaltSetzen("Einsatz: [Hoch/Runter] | Start: [LEER]");
        }
    }
    
    // ==========================================
    //            BAR LOGIK
    // ==========================================
    
    private void barTaste(int taste) {
        if (taste == Taste._3) {
            if (spielstand.suppeKaufen()) {
                barSzene.setFeedback("Suppe gekauft! Slot [3]");
                barSzene.juanSagt("Gute Wahl. Staerkt die Nerven.");
            } else {
                barSzene.setFeedback("Inventar voll oder kein Geld!");
            }
        }
        if (taste == Taste._4) {
            if (spielstand.wasserKaufen()) {
                barSzene.setFeedback("Wasser gekauft! Slot [4]");
                barSzene.juanSagt("Klares Wasser. Selten hier unten.");
            } else {
                barSzene.setFeedback("Inventar voll oder kein Geld!");
            }
        }
        if (taste == Taste._5) {
            if (spielstand.bierKaufen()) {
                barSzene.setFeedback("Bier gekauft! Slot [5]");
                barSzene.juanSagt("Prost! Aber pass auf...");
            } else {
                barSzene.setFeedback("Inventar voll oder kein Geld!");
            }
        }
        if (taste == Taste._6) {
            if (spielstand.lupeKaufen()) {
                barSzene.setFeedback("Lupe gekauft! Druecke [L] am Tisch.");
                barSzene.juanSagt("Damit siehst du was der Dealer hat. Alle 10 Runden.");
            } else {
                barSzene.setFeedback("Zu teuer oder Inventar voll!");
            }
        }
        if (taste == Taste._7) {
            if (spielstand.getGeld() >= 500) {
                if (spielstand.itemHinzufuegen("Zigarette")) {
                    spielstand.geldAendern(-500);
                    barSzene.setFeedback("Zigarette gekauft!");
                    barSzene.juanSagt("Die bringen Glueck... sagt man.");
                } else {
                    barSzene.setFeedback("Inventar voll!");
                }
            } else {
                barSzene.setFeedback("Nicht genug Geld!");
            }
        }
        
        sidebar.aktualisieren(spielstand);
        barSzene.aktualisieren(spielstand);
    }
    
    // ==========================================
    //        BLACKJACK ANZEIGE
    // ==========================================
    
    private void blackjackAnzeigen() {
        tischHintergrund.sichtbarSetzen(true);
        bjStatus.sichtbarSetzen(true);
        bjSpielerLabel.sichtbarSetzen(true);
        bjDealerLabel.sichtbarSetzen(true);
        bjSpielerPunkte.sichtbarSetzen(true);
        bjDealerPunkte.sichtbarSetzen(true);
        if (bot1Label != null) bot1Label.sichtbarSetzen(true);
        
        // Wenn eine Runde aktiv ist, muessen die Karten wieder sichtbar gemacht werden
        if (rundeLaeuft || !setzPhase) {
            spielerHand.alleZentriertAnzeigen(960, 768, false);
            dealerHand.positionAnzeigen(714, 78, !dealerKarteSichtbar);
            bot1.kartenAnzeigen(1428, 720);
            bot1.getHand().alleDrehen(30);
        }
    }
    
    private void blackjackVerstecken() {
        tischHintergrund.sichtbarSetzen(false);
        bjStatus.sichtbarSetzen(false);
        bjSpielerLabel.sichtbarSetzen(false);
        bjDealerLabel.sichtbarSetzen(false);
        bjSpielerPunkte.sichtbarSetzen(false);
        bjDealerPunkte.sichtbarSetzen(false);
        bjDealerPunkte.sichtbarSetzen(false);
        if (bot1Label != null) bot1Label.sichtbarSetzen(false);
        
        // Wir verstecken sie hier nur optisch beim Wechsel in die Bar.
        if (spielerHand != null) spielerHand.alleOptischVerstecken();
        if (dealerHand != null) dealerHand.alleOptischVerstecken();
        if (bot1 != null && bot1.getHand() != null) bot1.getHand().alleOptischVerstecken();
    }
}