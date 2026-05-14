import ea.edu.*;

/**
 * Zeigt Story-Texte (Cutscenes) an.
 * Wird für Intro, Zwischensequenzen und Enden genutzt.
 */
public class CutsceneSzene {
    
    private BildE hintergrund;
    private TextE zeile1, zeile2, zeile3, zeile4, zeile5;
    private TextE weiterText;
    
    private String[][] szenen;
    private int aktuelleSzene;
    private int aktuelleSeite;
    private boolean sichtbar;
    private boolean abgeschlossen;
    
    private static final int OFFSET_X = 600;
    
    public CutsceneSzene() {
        hintergrund = new BildE(0, 0, "/Users/an/PayBackJack/assets/Sprites/Bar/Background_640x360.png");
        hintergrund.sichtbarSetzen(false);
        
        zeile1 = new TextE("");
        zeile1.positionSetzen(OFFSET_X + 150, 250);
        zeile1.farbeSetzen("Weiß");
        zeile1.groesseSetzen(24);
        zeile1.sichtbarSetzen(false);
        
        zeile2 = new TextE("");
        zeile2.positionSetzen(OFFSET_X + 150, 330);
        zeile2.farbeSetzen("Weiß");
        zeile2.groesseSetzen(24);
        zeile2.sichtbarSetzen(false);
        
        zeile3 = new TextE("");
        zeile3.positionSetzen(OFFSET_X + 150, 410);
        zeile3.farbeSetzen("Weiß");
        zeile3.groesseSetzen(24);
        zeile3.sichtbarSetzen(false);
        
        zeile4 = new TextE("");
        zeile4.positionSetzen(OFFSET_X + 150, 490);
        zeile4.farbeSetzen("Weiß");
        zeile4.groesseSetzen(24);
        zeile4.sichtbarSetzen(false);
        
        zeile5 = new TextE("");
        zeile5.positionSetzen(OFFSET_X + 150, 570);
        zeile5.farbeSetzen("Weiß");
        zeile5.groesseSetzen(24);
        zeile5.sichtbarSetzen(false);
        
        weiterText = new TextE("[LEERTASTE] Weiter...");
        weiterText.positionSetzen(OFFSET_X + 150, 900);
        weiterText.farbeSetzen("Gelb");
        weiterText.groesseSetzen(18);
        weiterText.sichtbarSetzen(false);
        
        sichtbar = false;
        abgeschlossen = false;
    }
    
    /**
     * Lädt die Intro-Story.
     */
    public void ladeIntro() {
        szenen = new String[][] {
            {
                "Du schuldest Jack 100.000 Euro.",
                "Jack ist nicht irgendwer.",
                "Er ist der Boss des Untergrund-Kartells.",
                "Du konntest nicht rechtzeitig zahlen.",
                ""
            },
            {
                "Zwei Maenner holen dich ab.",
                "Eine Augenbinde, eine Autofahrt.",
                "Du wirst in einen schaebigen Keller gebracht.",
                "Eine kleine Bar. Dutzende Gesichter starren dich an.",
                "Sie alle schulden jemandem Geld."
            },
            {
                "Jack tritt vor dich hin.",
                "'Du spielst jetzt Blackjack. Mein Spiel.'",
                "'Erreichst du 100.000 Euro, bist du frei.'",
                "'Wenn nicht... bleibst du hier. Fuer immer.'",
                "'Viel Glueck. Du wirst es brauchen.'"
            },
            {
                "Die Regeln sind einfach:",
                "Komm so nah wie moeglich an 21 Punkte.",
                "Ueberschreitest du 21, verlierst du.",
                "Du startest mit 1.000 Euro.",
                "Iss und trink, sonst kollabierst du. Das kostet 5.000 Euro."
            },
            {
                "Steuerung:",
                "[1] Blackjack Tisch | [2] Bar",
                "[H] Hit | [S] Stand | [D] Double Down",
                "[PFEILTASTEN] Einsatz aendern",
                "[LEERTASTE] Runde starten"
            }
        };
        aktuelleSzene = 0;
        aktuelleSeite = 0;
        abgeschlossen = false;
        seitenAnzeigen();
    }
    
    /**
     * Lädt das Gewinn-Ende.
     */
    public void ladeGewinn() {
        szenen = new String[][] {
            {
                "Du hast es geschafft.",
                "100.000 Euro. Auf den Cent genau.",
                "Jack nickt langsam.",
                "'Ein Deal ist ein Deal.'",
                ""
            },
            {
                "Die schwere Kellertuer oeffnet sich.",
                "Tageslicht blendet dich.",
                "Du stolperst die Treppe hoch.",
                "Erschoepft. Aber frei.",
                "ENDE - Du hast gewonnen!"
            }
        };
        aktuelleSzene = 0;
        aktuelleSeite = 0;
        abgeschlossen = false;
        seitenAnzeigen();
    }
    
    /**
     * Lädt das Verlust-Ende.
     */
    public void ladeVerlust() {
        szenen = new String[][] {
            {
                "Dein Konto ist leer.",
                "Jack laechelt kalt.",
                "'Tja... sieht so aus als bleibst du.'",
                "'Juan, zeig ihm die Kueche.'",
                ""
            },
            {
                "GAME OVER",
                "Du verbringst den Rest deiner Tage",
                "in diesem Keller.",
                "",
                "[LEERTASTE] Neues Spiel"
            }
        };
        aktuelleSzene = 0;
        aktuelleSeite = 0;
        abgeschlossen = false;
        seitenAnzeigen();
    }
    
    private void seitenAnzeigen() {
        if (szenen == null || aktuelleSzene >= szenen.length) return;
        
        String[] seite = szenen[aktuelleSzene];
        zeile1.inhaltSetzen(seite[0]);
        zeile2.inhaltSetzen(seite[1]);
        zeile3.inhaltSetzen(seite[2]);
        zeile4.inhaltSetzen(seite[3]);
        zeile5.inhaltSetzen(seite[4]);
    }
    
    /**
     * Geht zur nächsten Seite. Gibt true zurück wenn die Cutscene vorbei ist.
     */
    public boolean weiter() {
        aktuelleSzene++;
        if (aktuelleSzene >= szenen.length) {
            abgeschlossen = true;
            verstecken();
            return true;
        }
        seitenAnzeigen();
        return false;
    }
    
    public void anzeigen() {
        sichtbar = true;
        hintergrund.sichtbarSetzen(true);
        zeile1.sichtbarSetzen(true);
        zeile2.sichtbarSetzen(true);
        zeile3.sichtbarSetzen(true);
        zeile4.sichtbarSetzen(true);
        zeile5.sichtbarSetzen(true);
        weiterText.sichtbarSetzen(true);
    }
    
    public void verstecken() {
        sichtbar = false;
        hintergrund.sichtbarSetzen(false);
        zeile1.sichtbarSetzen(false);
        zeile2.sichtbarSetzen(false);
        zeile3.sichtbarSetzen(false);
        zeile4.sichtbarSetzen(false);
        zeile5.sichtbarSetzen(false);
        weiterText.sichtbarSetzen(false);
    }
    
    public boolean istSichtbar() { return sichtbar; }
    public boolean istAbgeschlossen() { return abgeschlossen; }
}
