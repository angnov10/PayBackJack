import ea.edu.*;
import ea.Sound;
import ea.Taste;

/**
 * PayBackJack - Das Hauptspiel.
 * 15-Minuten Arcade-Modus Umsetzung.
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
    private DurchsuchungMinigame durchsuchung;
    
    // Blackjack-Tisch Elemente
    private BildE tischHintergrundLow, tischHintergrundMid, tischHintergrundHigh, tischHintergrund;
    // Blackjack UI
    private TextE bjStatus;
    private TextE bjDealerLabel;
    private TextE bjSpielerLabel;
    private TextE bjSpielerPunkte;
    private TextE bjDealerPunkte;
    
    // Blackjack-Logik
    private Kartenstapel deck;
    private Hand spielerHand;
    private Hand dealerHand;
    private BotSpieler bot1;
    private TextE bot1Label;
    
    private boolean rundeLaeuft;
    private boolean setzPhase;
    private boolean dealerKarteSichtbar; 
    private boolean wartetAufRundeStart;
    private int aktuellerTischLevel = -1;
    
    // Jack High-End Logik
    private BildE jackBody, jackHead, jackWarnung;
    private TextE jackDialog;
    private boolean jackBietetBier;
    private boolean qteAktiv;
    private long qteStartTime;
    
    // Tutorials
    private boolean tutorialLowGesehen;
    private boolean tutorialMidGesehen;
    
    // Threads
    private Thread gameLoopThread;
    
    // Debug Menu
    private boolean debugMenuAktiv;
    private TextE debugLabel;
    
    // Settings Menu
    private boolean settingsAktiv;
    private RechteckE settingsBg;
    private TextE settingsTitle, settingsMusic, settingsSfx, settingsInfo;
    private BildE settingsMusicBg, settingsSfxBg;
    private RechteckE settingsMusicBar, settingsSfxBar;
    private int settingsSelection = 0; // 0 = Music, 1 = SFX
    
    // SOUNDS
    private AudioPlayer musikTisch, musikBar;
    private AudioPlayer sfxCardDeal, sfxCardFlip, sfxCollapse, sfxError, sfxHover, sfxBuy, sfxMagnifier, sfxSmoke, sfxEat;
    private AudioPlayer sfxHeartbeat, sfxSearchSuccess, sfxSearchFail, sfxWarning;
    
    // Alkohol Effekt Overlay
    private RechteckE alkoholOverlay;
    // Jack's Blick Animation (12 Frames)
    private BildE[] jacksBlickFrames;
    private Thread jacksBlickThread;
    // Musik Mute bei Panik
    private boolean musikGedaempft;
    // High-End Lupe Counter (max 2x)
    private int highEndLupeUses;
    
    static {
        System.setProperty("sun.java2d.uiScale", "0.85");
    }
    
    public PayBackJack() {
        super(1920, 1080, false, false, true);
        aktuelleSzene = "intro_loading";
        
        // --- SOUNDS ---
        musikTisch = new AudioPlayer("../Assets/Sounds/SFX/music_table.wav", true);
        musikBar = new AudioPlayer("../Assets/Sounds/SFX/music_bar.wav", true);
        sfxCardDeal = new AudioPlayer("../Assets/Sounds/SFX/sfx_card_deal.wav", false);
        sfxCardFlip = new AudioPlayer("../Assets/Sounds/SFX/sfx_card_flip", false);
        sfxCollapse = new AudioPlayer("../Assets/Sounds/SFX/sfx_collapse", false);
        sfxError = new AudioPlayer("../Assets/Sounds/SFX/sfx_error", false);
        sfxHover = new AudioPlayer("../Assets/Sounds/SFX/sfx_hover", false);
        sfxBuy = new AudioPlayer("../Assets/Sounds/SFX/sfx_buy", false);
        sfxEat = new AudioPlayer("../Assets/Sounds/SFX/sfx_eat.wav", false);
        sfxMagnifier = new AudioPlayer("../Assets/Sounds/SFX/sfx_magnifier", false);
        sfxSmoke = new AudioPlayer("../Assets/Sounds/SFX/sfx_smoke.wav", false);
        sfxHeartbeat = new AudioPlayer("../Assets/Sounds/SFX/sfx_heartbeat.wav", false);
        sfxSearchSuccess = new AudioPlayer("../Assets/Sounds/SFX/sfx_search_success.wav", false);
        sfxSearchFail = new AudioPlayer("../Assets/Sounds/SFX/sfx_search_fail.wav", false);
        sfxWarning = new AudioPlayer("../Assets/Sounds/SFX/sfx_warning.wav", false);
        
        musikTisch.loop();
        musikTisch.pause();
        musikBar.loop();
        musikBar.pause();
        
        spielstand = new Spielstand();
        barSzene = new BarSzene();
        cutscene = new CutsceneSzene();
        durchsuchung = new DurchsuchungMinigame();
        
        // === BLACKJACK TISCH AUFBAUEN ===
        tischHintergrundLow = new BildE(0, 0, "../Assets/Sprites/Tisch/Background_640x360.png");
        tischHintergrundLow.sichtbarSetzen(false);
        tischHintergrundMid = new BildE(0, 0, "../Assets/Sprites/Tisch/Background_Mid_640x360.png");
        tischHintergrundMid.sichtbarSetzen(false);
        tischHintergrundHigh = new BildE(0, 0, "../Assets/Sprites/Tisch/Background_High_640x360.png");
        tischHintergrundHigh.sichtbarSetzen(false);
        tischHintergrund = tischHintergrundLow;
        
        bjStatus = new TextE("");
        bjStatus.mittelpunktSetzen(960, 480);
        bjStatus.sichtbarSetzen(false);
        
        anleitung = new BildE(1510, 20, "../Assets/Sprites/Tisch/Instructions_130x100.png");
        anleitung.sichtbarSetzen(false);
        
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
        
        bjSpielerPunkte = new TextE("Punkte: 0");
        bjSpielerPunkte.mittelpunktSetzen(960, 710); 
        bjSpielerPunkte.farbeSetzen("Weiß");
        bjSpielerPunkte.sichtbarSetzen(false);
        
        bot1 = new BotSpieler("Max");
        bot1Label = new TextE("Max");
        bot1Label.positionSetzen(1428, 500);
        bot1Label.farbeSetzen("Weiß");
        bot1Label.sichtbarSetzen(false);
        
        // Jack Sprites
        jackBody = new BildE(400, 300, "../Assets/Sprites/Bar/JackBody_40x80.png");
        jackBody.sichtbarSetzen(false);
        jackHead = new BildE(410, 274, "../Assets/Sprites/Bar/JackHead_26x26.png");
        jackHead.sichtbarSetzen(false);
        jackWarnung = new BildE(410, 240, "../Assets/Sprites/Tisch/JackWarnung.png");
        jackWarnung.sichtbarSetzen(false);
        jackDialog = new TextE("Jack bietet ein Bier an. [J] Ja | [N] Nein");
        jackDialog.positionSetzen(350, 400);
        jackDialog.farbeSetzen("Rot");
        jackDialog.sichtbarSetzen(false);
        
        debugMenuAktiv = false;
        debugLabel = new TextE("");
        debugLabel.positionSetzen(20, 1040);
        debugLabel.farbeSetzen("Gelb");
        debugLabel.sichtbarSetzen(false);
        
        // Settings UI
        settingsAktiv = false;
        settingsBg = new RechteckE();
        settingsBg.breiteSetzen(600);
        settingsBg.hoeheSetzen(450);
        settingsBg.positionSetzen(700, 250);
        settingsBg.farbeSetzen("Grau");
        settingsBg.sichtbarSetzen(false);
        
        settingsTitle = new TextE("Einstellungen");
        settingsTitle.positionSetzen(730, 270);
        settingsTitle.farbeSetzen("Weiß");
        settingsTitle.groesseSetzen(36);
        settingsTitle.sichtbarSetzen(false);
        
        settingsMusic = new TextE("Musik Lautstärke");
        settingsMusic.positionSetzen(730, 360);
        settingsMusic.farbeSetzen("Gelb");
        settingsMusic.groesseSetzen(24);
        settingsMusic.sichtbarSetzen(false);
        
        settingsMusicBg = new BildE(730, 400, "../Assets/Sprites/Sidebar/frame_hunger.png");
        settingsMusicBg.sichtbarSetzen(false);
        settingsMusicBar = new RechteckE();
        settingsMusicBar.breiteSetzen((int)(334 * AudioPlayer.getGlobalMusicVolume()));
        settingsMusicBar.hoeheSetzen(15);
        settingsMusicBar.positionSetzen(738, 405);
        settingsMusicBar.farbeSetzen("Gelb");
        settingsMusicBar.sichtbarSetzen(false);
        
        settingsSfx = new TextE("SFX Lautstärke");
        settingsSfx.positionSetzen(730, 470);
        settingsSfx.farbeSetzen("Weiß");
        settingsSfx.groesseSetzen(24);
        settingsSfx.sichtbarSetzen(false);
        
        settingsSfxBg = new BildE(730, 510, "../Assets/Sprites/Sidebar/frame_hunger.png");
        settingsSfxBg.sichtbarSetzen(false);
        settingsSfxBar = new RechteckE();
        settingsSfxBar.breiteSetzen((int)(334 * AudioPlayer.getGlobalSfxVolume()));
        settingsSfxBar.hoeheSetzen(15);
        settingsSfxBar.positionSetzen(738, 515);
        settingsSfxBar.farbeSetzen("Weiß");
        settingsSfxBar.sichtbarSetzen(false);
        
        settingsInfo = new TextE("Hoch/Runter zum Auswählen. Links/Rechts für Lautstärke. 'O' = Zurück");
        settingsInfo.positionSetzen(730, 600);
        settingsInfo.farbeSetzen("Weiß");
        settingsInfo.groesseSetzen(14);
        settingsInfo.sichtbarSetzen(false);
        
        FontHelper.anwenden(bjStatus, bjDealerLabel, bjDealerPunkte, bjSpielerLabel, bjSpielerPunkte, bot1Label, jackDialog, debugLabel);
        FontHelper.anwenden(settingsTitle, settingsMusic, settingsSfx, settingsInfo);
        
        rundeLaeuft = false;
        setzPhase = true;
        dealerKarteSichtbar = false;
        wartetAufRundeStart = false;
        
        deck = new Kartenstapel();
        spielerHand = new Hand();
        dealerHand = new Hand();
        
        sidebar = new Sidebar();
        
        // === ALKOHOL OVERLAY (nach Sidebar, damit er über dem Tisch liegt) ===
        alkoholOverlay = new RechteckE();
        alkoholOverlay.breiteSetzen(1320); // Spielbereich: 600..1920
        alkoholOverlay.hoeheSetzen(1080);
        alkoholOverlay.positionSetzen(600, 0);
        alkoholOverlay.farbeSetzen("Blau");
        alkoholOverlay.sichtbarSetzen(false);
        
        // === JACK'S BLICK ANIMATION (12 Frames) ===
        jacksBlickFrames = new BildE[12];
        for (int i = 0; i < 12; i++) {
            String frameNr = String.format("%02d", i + 1);
            jacksBlickFrames[i] = new BildE(0, 0, "../Assets/Sprites/Tisch/JacksBlick/zoom_" + frameNr + ".png");
            jacksBlickFrames[i].sichtbarSetzen(false);
        }
        musikGedaempft = false;
        highEndLupeUses = 0;
        
        aktuelleSzene = "cutscene";
        cutscene.ladeIntro();
        cutscene.anzeigen();
        sidebar.aktualisieren(spielstand);
        
        startGameLoop();
    }
    
    private void startGameLoop() {
        gameLoopThread = new Thread(() -> {
            long lastTime = System.currentTimeMillis();
            while (true) {
                long now = System.currentTimeMillis();
                long delta = now - lastTime;
                
                // 1-Sekunden Timer Update
                if (delta >= 1000) {
                    if (aktuelleSzene != null && !aktuelleSzene.equals("cutscene")) {
                        spielstand.zeitAbziehen(1);
                        sidebar.aktualisieren(spielstand);
                        if (spielstand.getZeitVerbleibend() <= 0 && !spielstand.hatGewonnen()) {
                            spielstand.setGeld(-1); // Game Over triggern
                            pruefeSpielstatus();
                        }
                    }
                    lastTime = now;
                }
                
                // QTE Timeout Jack
                if (qteAktiv && (now - qteStartTime > 800)) {
                    qteAktiv = false;
                    jackWarnung.sichtbarSetzen(false);
                    stoppeJacksBlickAnimation();
                    spielstand.verdachtAendern(30);
                    bjStatus.inhaltSetzen("Jack hat dich beobachtet! +30% Verdacht");
                    sfxError.play();
                    sidebar.aktualisieren(spielstand);
                }
                
                // Herzklopfen - Frequenz skaliert mit Verdacht
                int verd = spielstand.getVerdacht();
                double heartbeatChance = verd >= 90 ? 0.08 : (verd >= 60 ? 0.04 : 0);
                if (heartbeatChance > 0 && Math.random() < heartbeatChance) {
                    sfxHeartbeat.play();
                }
                
                // Alkohol Overlay Update (alle 50ms)
                aktualisiereAlkoholEffekte();
                
                // Mute bei Panik (Verdacht >= 90%)
                if (verd >= 90 && !musikGedaempft && !"cutscene".equals(aktuelleSzene)) {
                    musikGedaempft = true;
                    musikTisch.pause();
                    musikBar.pause();
                } else if (verd < 90 && musikGedaempft) {
                    musikGedaempft = false;
                    if ("blackjack".equals(aktuelleSzene)) musikTisch.unpause();
                    else if ("bar".equals(aktuelleSzene)) musikBar.unpause();
                }
                
                try { Thread.sleep(50); } catch(Exception e) {}
            }
        });
        gameLoopThread.start();
    }
    
    private void aktualisiereTischGrafik() {
        int level = spielstand.getTischLevel();
        if (level != aktuellerTischLevel) {
            aktuellerTischLevel = level;
            
            // High-End Lupe Counter beim Eintritt zurücksetzen
            if (level == 2) highEndLupeUses = 0;
            
            // Alten Hintergrund verstecken
            if (tischHintergrund != null) tischHintergrund.sichtbarSetzen(false);
            
            if (level == 0) tischHintergrund = tischHintergrundLow;
            else if (level == 1) tischHintergrund = tischHintergrundMid;
            else if (level == 2) tischHintergrund = tischHintergrundHigh;
            
            tischHintergrund.sichtbarSetzen("blackjack".equals(aktuelleSzene));
            
            if (level == 2) {
                jackBody.sichtbarSetzen(true);
                jackHead.sichtbarSetzen(true);
            } else {
                jackBody.sichtbarSetzen(false);
                jackHead.sichtbarSetzen(false);
            }
            
            // Limit Update
            if (level == 0) spielstand.setEinsatz(Math.min(spielstand.getEinsatz(), 200));
            if (level == 1) spielstand.setEinsatz(Math.min(spielstand.getEinsatz(), 500));
            if (level == 2) spielstand.setEinsatz(Math.min(spielstand.getEinsatz(), 1000));
        }
    }
    
    @Override
    public void tasteReagieren(int taste) {
        if (aktuelleSzene == null) return;
        
        // === SETTINGS MENU ===
        if (taste == Taste.O) {
            settingsAktiv = !settingsAktiv;
            toggleSettings(settingsAktiv);
            return;
        }
        if (settingsAktiv) {
            handleSettingsInput(taste);
            return;
        }
        
        // === DEBUG MENU ===
        if (taste == Taste.M) {
            debugMenuAktiv = !debugMenuAktiv;
            debugLabel.sichtbarSetzen(debugMenuAktiv);
            return;
        }
        if (debugMenuAktiv) {
            if (taste == Taste.X) {
                spielstand.setGeld(2000);
                aktualisiereTischGrafik();
                sidebar.aktualisieren(spielstand);
                if (bjStatus != null) bjStatus.inhaltSetzen("DEBUG: Skip Mid-Tisch");
                return;
            }
            if (taste == Taste.C) {
                spielstand.setGeld(5000);
                aktualisiereTischGrafik();
                sidebar.aktualisieren(spielstand);
                if (bjStatus != null) bjStatus.inhaltSetzen("DEBUG: Skip High-Tisch");
                return;
            }
            if (taste == Taste.V) {
                spielstand.setGeld(10000);
                pruefeSpielstatus();
                return;
            }
            if (taste == Taste.B) {
                spielstand.setGeld(-1);
                pruefeSpielstatus();
                return;
            }
            if (taste == Taste.A) {
                spielstand.alkoholAendern(50);
                aktualisiereAlkoholEffekte();
                sidebar.aktualisieren(spielstand);
                if (bjStatus != null) bjStatus.inhaltSetzen("DEBUG: Alkohol jetzt " + spielstand.getAlkohol() + "%");
                return;
            }
        }
        
        // === CUTSCENE ===
        if ("cutscene".equals(aktuelleSzene)) {
            if (taste == Taste.LEERTASTE) {
                if (cutscene.weiter()) {
                    if (!spielstand.istPleite() && !spielstand.hatGewonnen()) {
                        wechselZuBlackjack();
                    }
                }
            }
            return;
        }
        
        // === MINIGAME AKTIV ===
        if (durchsuchung.istAktiv()) {
            if (taste == Taste.LEERTASTE) {
                boolean erfolg = durchsuchung.stoppen();
                if (erfolg) {
                    sfxSearchSuccess.play();
                    bjStatus.inhaltSetzen("Jack hat nichts gefunden...");
                } else {
                    sfxSearchFail.play();
                    bjStatus.inhaltSetzen("ERWISCHT! Geld auf 1000€, Items weg.");
                    spielstand.setGeld(1000);
                    spielstand.inventarLeeren();
                    aktuellerTischLevel = -1; // Force Graphic Update
                    aktualisiereTischGrafik();
                }
                spielstand.setVerdacht(0);
                spielstand.setGeschummelt(false);
                sidebar.aktualisieren(spielstand);
                
                if (wartetAufRundeStart) {
                    wartetAufRundeStart = false;
                    fortsetzeRundeStart();
                } else if (setzPhase) {
                    bjStatus.inhaltSetzen("Einsatz wählen...");
                }
            }
            return;
        }
        
        // === SZENEN-WECHSEL ===
        if (taste == Taste._1 && !rundeLaeuft) {
            sfxHover.play();
            wechselZuBlackjack();
            return;
        }
        if (taste == Taste._2 && !rundeLaeuft) {
            if (spielstand.getTischLevel() == 2) {
                sfxError.play();
                bjStatus.inhaltSetzen("Kein Bar-Zugang am High-End Tisch!");
                return;
            }
            sfxHover.play();
            wechselZuBar();
            return;
        }
        
        // === BLACKJACK SZENE ===
        if ("blackjack".equals(aktuelleSzene)) {
            blackjackTaste(taste);
        }
        
        // === BAR SZENE ===
        if ("bar".equals(aktuelleSzene)) {
            barTaste(taste);
        }
        
        // === INVENTAR NUTZEN ===
        if (("blackjack".equals(aktuelleSzene) || "bar".equals(aktuelleSzene)) && taste >= Taste._3 && taste <= Taste._8) {
            int slot = taste - Taste._3; 
            nutzeItem(slot);
        }
    }
    
    @Override
    public void klickReagieren(int x, int y) {
        if ("intro_loading".equals(aktuelleSzene)) return;
        
        // Inventar Klicks (Sidebar)
        if (x < 600) {
            if ("blackjack".equals(aktuelleSzene) || "bar".equals(aktuelleSzene)) {
                int slot = sidebar.checkInventarKlick(x, y);
                if (slot >= 0) {
                    nutzeItem(slot);
                }
            }
        } 
        // Bar Shop Klicks
        else if ("bar".equals(aktuelleSzene) && x >= 600) {
            String clickedItem = barSzene.checkKaufKlick(x, y);
            if (clickedItem != null) {
                int preis = barSzene.getPreis(clickedItem, spielstand.getTischLevel());
                if (clickedItem.equals("Lupe")) preis = 10000;
                if (clickedItem.equals("Zigarette")) preis = 500;
                
                if (spielstand.kaufen(clickedItem, preis)) {
                    sfxBuy.play();
                    barSzene.setFeedback(clickedItem + " gekauft!");
                    barSzene.huanSagt("Gute Wahl.");
                } else {
                    sfxError.play();
                    barSzene.setFeedback("Zu teuer oder Inventar voll!");
                    barSzene.huanSagt("Dafür reicht die Kohle nicht, Kumpel.");
                }
                sidebar.aktualisieren(spielstand);
                barSzene.aktualisieren(spielstand);
            }
        }
    }
    
    private void nutzeItem(int slot) {
        String genutzt = spielstand.itemNutzen(slot);
        if (genutzt != null) {
            boolean zuBetrunken = spielstand.getAlkohol() >= 90;
            if (genutzt.equals("Zigarette")) {
                if (zuBetrunken) {
                    bjStatus.inhaltSetzen("Zu betrunken! Zigarette wirkungslos.");
                    sfxError.play();
                } else {
                    sfxSmoke.play();
                    spielstand.setGeschummelt(true);
                    if (spielstand.getVerdacht() > 80 && "blackjack".equals(aktuelleSzene)) {
                        durchsuchung.starten(false);
                    }
                }
            } else if (genutzt.equals("Lupe") && "blackjack".equals(aktuelleSzene)) {
                if (zuBetrunken) {
                    bjStatus.inhaltSetzen("Zu betrunken zum Schummeln!");
                    sfxError.play();
                } else {
                    lupeBenuetzen();
                }
            } else if (genutzt.equals("Ass") && "blackjack".equals(aktuelleSzene) && rundeLaeuft) {
                if (zuBetrunken) {
                    bjStatus.inhaltSetzen("Zu betrunken! Ass verloren.");
                    sfxError.play();
                } else {
                    spielerHand.karteHinzufuegen(new Karte("Herz", "Ass", 11));
                    sfxCardDeal.play();
                    spielerHand.alleZentriertAnzeigen(960, 768, false);
                    bjSpielerPunkte.inhaltSetzen("Punkte: " + spielerHand.punkteBerechnen());
                    spielstand.setGeschummelt(true);
                }
            } else {
                sfxEat.play(); 
            }
            
            sidebar.aktualisieren(spielstand);
            if (aktuelleSzene.equals("bar")) barSzene.aktualisieren(spielstand);
            if (spielerHand != null) bjSpielerPunkte.inhaltSetzen("Punkte: " + spielerHand.punkteBerechnen());
            pruefeSpielstatus();
        }
    }
    
    private void wechselZuBlackjack() {
        barSzene.verstecken();
        cutscene.verstecken();
        musikBar.pause();
        musikTisch.unpause();
        
        aktualisiereTischGrafik();
        blackjackAnzeigen();
        aktuelleSzene = "blackjack";
        sidebar.szeneMarkieren("blackjack");
        anleitung.sichtbarSetzen(true);
        bjStatus.inhaltSetzen("");
        setzPhase = true;
        
        checkTutorials();
    }
    
    private void wechselZuBar() {
        blackjackVerstecken();
        cutscene.verstecken();
        musikTisch.pause();
        musikBar.unpause();
        
        barSzene.anzeigen();
        barSzene.aktualisieren(spielstand);
        aktuelleSzene = "bar";
        sidebar.szeneMarkieren("bar");
        
        checkTutorials();
    }
    
    private void checkTutorials() {
        int level = spielstand.getTischLevel();
        if (level == 0 && spielstand.getRundenGespielt() >= 2 && !tutorialLowGesehen) {
            tutorialLowGesehen = true;
            spielstand.itemHinzufuegen("Lupe");
            spielstand.itemHinzufuegen("Bier");
            if (!aktuelleSzene.equals("bar")) wechselZuBar();
            barSzene.huanSagt("Dein Nachbar wurde erwischt. Hier, nimm das.");
            sidebar.aktualisieren(spielstand);
        }
        if (level == 1 && !tutorialMidGesehen) {
            tutorialMidGesehen = true;
            spielstand.itemHinzufuegen("Ass");
            spielstand.itemHinzufuegen("Ass");
            spielstand.itemHinzufuegen("Zigarette");
            spielstand.itemHinzufuegen("Zigarette");
            if (!aktuelleSzene.equals("bar")) wechselZuBar();
            barSzene.huanSagt("Mid-End Tische sind hart. Hier, ein kleines Geschenk.");
            sidebar.aktualisieren(spielstand);
        }
    }
    
    private void blackjackTaste(int taste) {
        if (qteAktiv) {
            if (taste == Taste.F) {
                qteAktiv = false;
                jackWarnung.sichtbarSetzen(false);
                stoppeJacksBlickAnimation();
                bjStatus.inhaltSetzen("Jack Blick abgewehrt.");
            } else {
                qteAktiv = false;
                jackWarnung.sichtbarSetzen(false);
                stoppeJacksBlickAnimation();
                spielstand.verdachtAendern(30);
                bjStatus.inhaltSetzen("Jack hat dich beobachtet! +30% Verdacht");
            }
            sidebar.aktualisieren(spielstand);
            return;
        }
        
        if (jackBietetBier) {
            if (taste == Taste.J) {
                jackBietetBier = false;
                jackDialog.sichtbarSetzen(false);
                spielstand.alkoholAendern(20);
                spielstand.verdachtAendern(-10);
                sidebar.aktualisieren(spielstand);
                bjStatus.inhaltSetzen("Jack nickt. Du trinkst das Bier.");
            } else if (taste == Taste.N) {
                jackBietetBier = false;
                jackDialog.sichtbarSetzen(false);
                bjStatus.inhaltSetzen("Jack ist beleidigt. Er durchsucht dich hart!");
                durchsuchung.starten(true); 
            }
            return;
        }

        if (setzPhase) {
            int maxLimit = 200;
            if (spielstand.getTischLevel() == 1) maxLimit = 500;
            if (spielstand.getTischLevel() == 2) maxLimit = 1000;
            
            if (taste == Taste.OBEN) {
                int neuerEinsatz = spielstand.getEinsatz() + 50;
                if (neuerEinsatz <= spielstand.getGeld() && neuerEinsatz <= maxLimit) {
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
            // Alkohol >= 90%: Zufällige Fehleingaben (Hit/Stand vertauscht)
            if (spielstand.getAlkohol() >= 90 && (taste == Taste.H || taste == Taste.S) && Math.random() < 0.3) {
                bjStatus.inhaltSetzen("Zu betrunken! Kontrolle verloren!");
                sfxError.play();
                if (taste == Taste.H) { stand(); return; }
                if (taste == Taste.S) { hit(); return; }
            }
            if (taste == Taste.H) hit();
            if (taste == Taste.S) stand();
            if (taste == Taste.D && spielerHand.anzahlKarten() == 2) doubleDown();
            if (taste == Taste.L) lupeBenuetzen();
            
            // Random Jack's Blick
            if (spielstand.getTischLevel() == 2 && Math.random() < 0.05 && !qteAktiv && !durchsuchung.istAktiv()) {
                qteAktiv = true;
                qteStartTime = System.currentTimeMillis();
                jackWarnung.sichtbarSetzen(true);
                starteJacksBlickAnimation();
                sfxWarning.play();
            }
        } else {
            if (taste == Taste.LEERTASTE) {
                pruefeSpielstatus();
            }
        }
    }
    
    private void starteRunde() {
        setzPhase = false;
        
        int level = spielstand.getTischLevel();
        double baseChance = 0;
        if (level == 1) baseChance = 1.0 / 8.0;
        if (level == 2) baseChance = 1.0 / 5.0;
        if (spielstand.hatGeschummelt()) baseChance += 0.05;
        
        if (Math.random() < baseChance && level > 0) {
            bjStatus.inhaltSetzen("JACK WIRD MISSTRAUISCH! DURCHSUCHUNG!");
            wartetAufRundeStart = true;
            durchsuchung.starten(false);
            return;
        }
        
        fortsetzeRundeStart();
    }
    
    private void fortsetzeRundeStart() {
        rundeLaeuft = true;
        dealerKarteSichtbar = false;
        
        int einsatz = spielstand.getEinsatz();
        spielstand.geldAendern(-einsatz);
        
        spielerHand.kartenZurueckgeben(deck);
        dealerHand.kartenZurueckgeben(deck);
        if (bot1 != null) bot1.zuruecksetzen(deck);
        deck.mischen();
        
        spielerHand.karteHinzufuegen(deck.karteZiehen());
        sfxCardDeal.play();
        if (bot1 != null) bot1.getHand().karteHinzufuegen(deck.karteZiehen());
        dealerHand.karteHinzufuegen(deck.karteZiehen());
        sfxCardDeal.play();
        
        spielerHand.karteHinzufuegen(deck.karteZiehen());
        sfxCardDeal.play();
        if (bot1 != null) bot1.getHand().karteHinzufuegen(deck.karteZiehen());
        dealerHand.karteHinzufuegen(deck.karteZiehen()); 
        sfxCardDeal.play();
        
        botsSpielenLassen();
        
        spielerHand.alleZentriertAnzeigen(960, 768, false); 
        dealerHand.positionAnzeigen(714, 78, true); 
        if (bot1 != null) bot1.kartenAnzeigen(1428, 720); 
        
        bjSpielerPunkte.inhaltSetzen("Du: " + spielerHand.punkteBerechnen());
        bjDealerPunkte.inhaltSetzen("Dealer: ?");
        bjStatus.inhaltSetzen("");
        
        sidebar.aktualisieren(spielstand);
        
        if (spielerHand.punkteBerechnen() == 21) {
            gewinn(2.5);
            bjStatus.inhaltSetzen("BLACKJACK! Gewinn: " + (int)(einsatz * 2.5) + "€");
        }
    }
    
    private void botsSpielenLassen() {
        if (bot1 != null) {
            while (bot1.willZiehen()) {
                bot1.getHand().karteHinzufuegen(deck.karteZiehen());
                sfxCardDeal.play();
            }
        }
    }
    
    private void hit() {
        spielerHand.karteHinzufuegen(deck.karteZiehen());
        sfxCardDeal.play();
        spielerHand.alleZentriertAnzeigen(960, 768, false);
        int p = spielerHand.punkteBerechnen();
        bjSpielerPunkte.inhaltSetzen("Punkte: " + p);
        bjStatus.inhaltSetzen("");
        
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
            sfxCardDeal.play();
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
        sfxCardFlip.play();
        dealerHand.positionAnzeigen(714, 78, false);
        
        while (dealerHand.punkteBerechnen() < 17) {
            dealerHand.karteHinzufuegen(deck.karteZiehen());
            sfxCardDeal.play();
        }
        dealerHand.positionAnzeigen(714, 78, false);
        
        int sP = spielerHand.punkteBerechnen();
        int dP = dealerHand.punkteBerechnen();
        int einsatz = spielstand.getEinsatz();
        bjDealerPunkte.inhaltSetzen("Dealer: " + dP);
        
        if (dP > 21) {
            bjStatus.inhaltSetzen("Dealer Bust! Du gewinnst " + (einsatz * 2) + "€!");
            gewinn(2.0);
        } else if (dP > sP) {
            bjStatus.inhaltSetzen("Dealer gewinnt. -" + einsatz + "€");
            rundeBeenden();
        } else if (dP < sP) {
            bjStatus.inhaltSetzen("Du gewinnst " + (einsatz * 2) + "€!");
            gewinn(2.0);
        } else {
            bjStatus.inhaltSetzen("Push! Einsatz zurück.");
            gewinn(1.0);
        }
    }
    
    private void lupeBenuetzen() {
        if (spielstand.kannLupeNutzen() && !dealerKarteSichtbar) {
            sfxMagnifier.play();
            spielstand.lupeBenutzen();
            spielstand.setGeschummelt(true);
            spielstand.verdachtAendern(20);
            bjDealerPunkte.inhaltSetzen("Dealer: " + dealerHand.punkteBerechnen());
            dealerKarteSichtbar = true;
            dealerHand.positionAnzeigen(714, 78, false);
            sidebar.aktualisieren(spielstand);
            
            // High-End: max 2x Lupe, danach automatische Durchsuchung
            if (spielstand.getTischLevel() == 2) {
                highEndLupeUses++;
                if (highEndLupeUses >= 2) {
                    bjStatus.inhaltSetzen("Jack reicht's! Sofortige Durchsuchung!");
                    durchsuchung.starten(false);
                }
            }
        }
    }
    
    private void toggleSettings(boolean aktiv) {
        settingsBg.sichtbarSetzen(aktiv);
        settingsTitle.sichtbarSetzen(aktiv);
        settingsMusic.sichtbarSetzen(aktiv);
        settingsSfx.sichtbarSetzen(aktiv);
        settingsMusicBg.sichtbarSetzen(aktiv);
        settingsMusicBar.sichtbarSetzen(aktiv);
        settingsSfxBg.sichtbarSetzen(aktiv);
        settingsSfxBar.sichtbarSetzen(aktiv);
        settingsInfo.sichtbarSetzen(aktiv);
        if (aktiv) {
            updateSettingsUI();
        } 
    }

    private void handleSettingsInput(int taste) {
        if (taste == Taste.OBEN || taste == Taste.UNTEN) {
            settingsSelection = 1 - settingsSelection;
            sfxHover.play();
            updateSettingsUI();
        }
        
        if (taste == Taste.LINKS || taste == Taste.RECHTS) {
            float delta = (taste == Taste.RECHTS) ? 0.1f : -0.1f;
            if (settingsSelection == 0) {
                float vol = AudioPlayer.getGlobalMusicVolume() + delta;
                AudioPlayer.setGlobalMusicVolume(vol);
            } else {
                float vol = AudioPlayer.getGlobalSfxVolume() + delta;
                AudioPlayer.setGlobalSfxVolume(vol);
                sfxHover.play(); // Test sound
            }
            updateSettingsUI();
        }
    }

    private void updateSettingsUI() {
        settingsMusic.farbeSetzen(settingsSelection == 0 ? "Gelb" : "Weiß");
        settingsSfx.farbeSetzen(settingsSelection == 1 ? "Gelb" : "Weiß");
        settingsMusicBar.farbeSetzen(settingsSelection == 0 ? "Gelb" : "Weiß");
        settingsSfxBar.farbeSetzen(settingsSelection == 1 ? "Gelb" : "Weiß");
        
        float mVol = AudioPlayer.getGlobalMusicVolume();
        settingsMusicBar.breiteSetzen(Math.max(1, (int)(334 * mVol)));
        
        float sVol = AudioPlayer.getGlobalSfxVolume();
        settingsSfxBar.breiteSetzen(Math.max(1, (int)(334 * sVol)));
    }
    
    private void gewinn(double multiplikator) {
        int gewinn = (int)(spielstand.getEinsatz() * multiplikator);
        spielstand.geldAendern(gewinn);
        rundeBeenden();
    }
    
    private void rundeBeenden() {
        rundeLaeuft = false;
        spielstand.rundeVorbei();
        
        if (spielstand.istKollabiert()) {
            sfxCollapse.play();
            spielstand.kollapsDurchfuehren();
            bjStatus.inhaltSetzen("KOLLABIERT! -5000€ Strafe. [LEERTASTE]");
        }
        
        sidebar.aktualisieren(spielstand);
        
        if (spielstand.getEinsatz() > spielstand.getGeld() && spielstand.getGeld() > 0) {
            spielstand.setEinsatz(50);
        }
        
        if (spielstand.getTischLevel() == 2 && spielstand.getGeld() > 0) {
            jackBietetBier = true;
            jackDialog.sichtbarSetzen(true);
        }
    }
    
    private void pruefeSpielstatus() {
        if (spielstand.istKollabiert()) {
            sfxCollapse.play();
            spielstand.kollapsDurchfuehren();
            bjStatus.inhaltSetzen("KOLLABIERT! -5000€ Strafe. [LEERTASTE]");
            sidebar.aktualisieren(spielstand);
            return; 
        }
        
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
            bjStatus.inhaltSetzen("Einsatz wählen... [LEERTASTE]");
            aktualisiereTischGrafik();
            checkTutorials();
        }
    }
    
    private void barTaste(int taste) {
        int lvl = spielstand.getTischLevel();
        if (taste == Taste._3) {
            if (spielstand.kaufen("Suppe", barSzene.getPreis("Suppe", lvl))) {
                sfxBuy.play();
                barSzene.setFeedback("Suppe gekauft!");
            } else {
                sfxError.play();
                barSzene.setFeedback("Inventar voll oder kein Geld!");
            }
        }
        if (taste == Taste._4) {
            if (spielstand.kaufen("Wasser", barSzene.getPreis("Wasser", lvl))) {
                sfxBuy.play();
                barSzene.setFeedback("Wasser gekauft!");
            } else {
                sfxError.play();
                barSzene.setFeedback("Inventar voll oder kein Geld!");
            }
        }
        if (taste == Taste._5) {
            if (spielstand.kaufen("Bier", barSzene.getPreis("Bier", lvl))) {
                sfxBuy.play();
                barSzene.setFeedback("Bier gekauft!");
            } else {
                sfxError.play();
                barSzene.setFeedback("Inventar voll oder kein Geld!");
            }
        }
        if (taste == Taste.U) {
            if (spielstand.kaufen("SchmutzigesWasser", barSzene.getPreis("SchmutzigesWasser", lvl))) {
                sfxBuy.play();
                barSzene.setFeedback("Schm. Wasser gekauft!");
            } else {
                sfxError.play();
                barSzene.setFeedback("Inventar voll oder kein Geld!");
            }
        }
        if (taste == Taste.I) {
            if (spielstand.kaufen("VerdorbeneSuppe", barSzene.getPreis("VerdorbeneSuppe", lvl))) {
                sfxBuy.play();
                barSzene.setFeedback("Verd. Suppe gekauft!");
            } else {
                sfxError.play();
                barSzene.setFeedback("Inventar voll oder kein Geld!");
            }
        }
        if (taste == Taste._6) {
            if (spielstand.kaufen("Lupe", 10000)) {
                sfxBuy.play();
                barSzene.setFeedback("Lupe gekauft!");
            } else {
                sfxError.play();
                barSzene.setFeedback("Zu teuer oder Inventar voll!");
            }
        }
        if (taste == Taste._7) {
            if (spielstand.kaufen("Zigarette", 500)) {
                sfxBuy.play();
                barSzene.setFeedback("Zigarette gekauft!");
            } else {
                sfxError.play();
                barSzene.setFeedback("Nicht genug Geld oder voll!");
            }
        }
        
        sidebar.aktualisieren(spielstand);
        barSzene.aktualisieren(spielstand);
    }
    
    private void blackjackAnzeigen() {
        tischHintergrund.sichtbarSetzen(true);
        anleitung.sichtbarSetzen(true);
        bjDealerLabel.sichtbarSetzen(true);
        
        bjDealerPunkte.sichtbarSetzen(true);
        bjSpielerPunkte.sichtbarSetzen(true);
        if (bot1Label != null) bot1Label.sichtbarSetzen(true);
        
        if (rundeLaeuft || !setzPhase) {
            spielerHand.alleZentriertAnzeigen(960, 768, false);
            dealerHand.positionAnzeigen(714, 78, !dealerKarteSichtbar);
            bot1.kartenAnzeigen(1428, 720);
        }
    }
    
    private void blackjackVerstecken() {
        tischHintergrund.sichtbarSetzen(false);
        anleitung.sichtbarSetzen(false);
        bjDealerLabel.sichtbarSetzen(false);
        
        bjDealerPunkte.sichtbarSetzen(false);
        bjSpielerPunkte.sichtbarSetzen(false);
        if (bot1Label != null) bot1Label.sichtbarSetzen(false);
        
        if (spielerHand != null) spielerHand.alleOptischVerstecken();
        if (dealerHand != null) dealerHand.alleOptischVerstecken();
        if (bot1 != null && bot1.getHand() != null) bot1.getHand().alleOptischVerstecken();
    }
    
    /**
     * Aktualisiert den Alkohol-Overlay-Effekt im Spielbereich.
     * 30-60%: Blau (leicht), 60-90%: Lila (mittel), 90-100%: Rot (stark, blockt Items).
     */
    private void aktualisiereAlkoholEffekte() {
        if (alkoholOverlay == null) return;
        int alk = spielstand.getAlkohol();
        if (alk >= 90) {
            alkoholOverlay.farbeSetzen("Rot");
            alkoholOverlay.sichtbarSetzen(true);
        } else if (alk >= 60) {
            alkoholOverlay.farbeSetzen("Lila");
            alkoholOverlay.sichtbarSetzen(true);
        } else if (alk >= 30) {
            alkoholOverlay.farbeSetzen("Blau");
            alkoholOverlay.sichtbarSetzen(true);
        } else {
            alkoholOverlay.sichtbarSetzen(false);
        }
    }
    
    /**
     * Startet die Jack's-Blick-Sequenz (12 Frames in ~0.8s).
     * Frames sind Platzhalter bis echte Bilder bereitgelegt werden.
     */
    private void starteJacksBlickAnimation() {
        stoppeJacksBlickAnimation();
        if (jacksBlickFrames == null) return;
        jacksBlickThread = new Thread(() -> {
            try {
                for (int i = 0; i < 12; i++) {
                    if (i > 0 && jacksBlickFrames[i-1] != null)
                        jacksBlickFrames[i-1].sichtbarSetzen(false);
                    if (jacksBlickFrames[i] != null)
                        jacksBlickFrames[i].sichtbarSetzen(true);
                    Thread.sleep(66); // 12 Frames x 66ms = ~0.8s
                }
            } catch (InterruptedException e) {
                // Thread gestoppt - normal
            }
        });
        jacksBlickThread.setDaemon(true);
        jacksBlickThread.start();
    }
    
    /**
     * Stoppt die Jack's-Blick-Animation und versteckt alle Frames.
     */
    private void stoppeJacksBlickAnimation() {
        if (jacksBlickThread != null) jacksBlickThread.interrupt();
        if (jacksBlickFrames != null) {
            for (BildE f : jacksBlickFrames) {
                if (f != null) f.sichtbarSetzen(false);
            }
        }
    }
}