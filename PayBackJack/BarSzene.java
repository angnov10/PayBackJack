import ea.edu.*;

/**
 * Die Bar-Szene: Juan und seine Angebote.
 * Dynamische Preise basierend auf dem Tisch-Level.
 */
public class BarSzene {
    
    // Hintergrund
    private BildE hintergrund;
    
    // Juan
    private BildE juanKoerper;
    private BildE juanKopf;
    private TextE juanName;
    private TextE juanDialog;
    
    // Menükarte
    private TextE menuTitle;
    private TextE menuSuppe;
    private TextE menuWasser;
    private TextE menuBier;
    private TextE menuSchmutzWasser;
    private TextE menuVerdorbeneSuppe;
    
    // Items
    private TextE itemTitle;
    private TextE itemLupe;
    private TextE itemZigarette;
    
    // Steuerung
    private TextE steuerungText;
    
    // Feedback
    private TextE feedbackText;
    
    private boolean sichtbar;
    
    private static final int OFFSET_X = 600; // Rechts von der Sidebar
    
    public BarSzene() {
        hintergrund = new BildE(0, 0, "../Assets/Sprites/Bar/Background_640x360.png");
        hintergrund.sichtbarSetzen(false);
        
        juanKoerper = new BildE(OFFSET_X + 150, 540, "../Assets/Sprites/Bar/JuanBody_40x80.png");
        juanKoerper.sichtbarSetzen(false);
        
        juanKopf = new BildE(OFFSET_X + 170, 460, "../Assets/Sprites/Bar/JuanHead_26x26.png");
        juanKopf.sichtbarSetzen(false);
        
        juanName = new TextE("Juan");
        juanName.positionSetzen(OFFSET_X + 180, 420);
        juanName.farbeSetzen("Weiß");
        juanName.groesseSetzen(16);
        juanName.sichtbarSetzen(false);
        
        juanDialog = new TextE("Willkommen... Was darfs sein?");
        juanDialog.positionSetzen(OFFSET_X + 300, 500);
        juanDialog.farbeSetzen("Weiß");
        juanDialog.groesseSetzen(18);
        juanDialog.sichtbarSetzen(false);
        
        // Menü
        menuTitle = new TextE("--- MENUE ---");
        menuTitle.positionSetzen(OFFSET_X + 200, 50);
        menuTitle.farbeSetzen("Gelb");
        menuTitle.groesseSetzen(20);
        menuTitle.sichtbarSetzen(false);
        
        menuSuppe = new TextE("[3] Suppe");
        menuSuppe.positionSetzen(OFFSET_X + 200, 90);
        menuSuppe.farbeSetzen("Weiß");
        menuSuppe.groesseSetzen(16);
        menuSuppe.sichtbarSetzen(false);
        
        menuWasser = new TextE("[4] Wasser");
        menuWasser.positionSetzen(OFFSET_X + 200, 130);
        menuWasser.farbeSetzen("Weiß");
        menuWasser.groesseSetzen(16);
        menuWasser.sichtbarSetzen(false);
        
        menuBier = new TextE("[5] Bier");
        menuBier.positionSetzen(OFFSET_X + 200, 170);
        menuBier.farbeSetzen("Weiß");
        menuBier.groesseSetzen(16);
        menuBier.sichtbarSetzen(false);
        
        menuSchmutzWasser = new TextE("[U] Schmutziges Wasser");
        menuSchmutzWasser.positionSetzen(OFFSET_X + 200, 210);
        menuSchmutzWasser.farbeSetzen("Grau");
        menuSchmutzWasser.groesseSetzen(16);
        menuSchmutzWasser.sichtbarSetzen(false);
        
        menuVerdorbeneSuppe = new TextE("[I] Verdorbene Suppe");
        menuVerdorbeneSuppe.positionSetzen(OFFSET_X + 200, 250);
        menuVerdorbeneSuppe.farbeSetzen("Grau");
        menuVerdorbeneSuppe.groesseSetzen(16);
        menuVerdorbeneSuppe.sichtbarSetzen(false);
        
        // Items
        itemTitle = new TextE("--- SPEZIAL ---");
        itemTitle.positionSetzen(OFFSET_X + 200, 320);
        itemTitle.farbeSetzen("Rot");
        itemTitle.groesseSetzen(20);
        itemTitle.sichtbarSetzen(false);
        
        itemLupe = new TextE("[6] Lupe - 10.000€");
        itemLupe.positionSetzen(OFFSET_X + 200, 370);
        itemLupe.farbeSetzen("Weiß");
        itemLupe.groesseSetzen(16);
        itemLupe.sichtbarSetzen(false);
        
        itemZigarette = new TextE("[7] Zigarette - 500€");
        itemZigarette.positionSetzen(OFFSET_X + 200, 410);
        itemZigarette.farbeSetzen("Weiß");
        itemZigarette.groesseSetzen(16);
        itemZigarette.sichtbarSetzen(false);
        
        // Steuerung
        steuerungText = new TextE("[1] Zum Blackjack Tisch | [3-7, U, I] Kaufen");
        steuerungText.positionSetzen(OFFSET_X + 200, 1000);
        steuerungText.farbeSetzen("Gelb");
        steuerungText.groesseSetzen(16);
        steuerungText.sichtbarSetzen(false);
        
        // Feedback
        feedbackText = new TextE("");
        feedbackText.positionSetzen(OFFSET_X + 200, 500);
        feedbackText.farbeSetzen("Grün");
        feedbackText.groesseSetzen(18);
        feedbackText.sichtbarSetzen(false);
        
        FontHelper.anwenden(juanName, juanDialog, menuTitle, menuSuppe, menuWasser, menuBier, menuSchmutzWasser, menuVerdorbeneSuppe);
        FontHelper.anwenden(itemTitle, itemLupe, itemZigarette, steuerungText, feedbackText);
        
        sichtbar = false;
    }
    
    public void anzeigen() {
        sichtbar = true;
        hintergrund.sichtbarSetzen(true);
        juanKoerper.sichtbarSetzen(true);
        juanKopf.sichtbarSetzen(true);
        juanName.sichtbarSetzen(true);
        juanDialog.sichtbarSetzen(true);
        menuTitle.sichtbarSetzen(true);
        menuSuppe.sichtbarSetzen(true);
        menuWasser.sichtbarSetzen(true);
        menuBier.sichtbarSetzen(true);
        menuSchmutzWasser.sichtbarSetzen(true);
        menuVerdorbeneSuppe.sichtbarSetzen(true);
        itemTitle.sichtbarSetzen(true);
        itemLupe.sichtbarSetzen(true);
        itemZigarette.sichtbarSetzen(true);
        steuerungText.sichtbarSetzen(true);
        feedbackText.sichtbarSetzen(true);
    }
    
    public void verstecken() {
        sichtbar = false;
        hintergrund.sichtbarSetzen(false);
        juanKoerper.sichtbarSetzen(false);
        juanKopf.sichtbarSetzen(false);
        juanName.sichtbarSetzen(false);
        juanDialog.sichtbarSetzen(false);
        menuTitle.sichtbarSetzen(false);
        menuSuppe.sichtbarSetzen(false);
        menuWasser.sichtbarSetzen(false);
        menuBier.sichtbarSetzen(false);
        menuSchmutzWasser.sichtbarSetzen(false);
        menuVerdorbeneSuppe.sichtbarSetzen(false);
        itemTitle.sichtbarSetzen(false);
        itemLupe.sichtbarSetzen(false);
        itemZigarette.sichtbarSetzen(false);
        steuerungText.sichtbarSetzen(false);
        feedbackText.sichtbarSetzen(false);
    }
    
    public boolean istSichtbar() { return sichtbar; }
    
    public void setFeedback(String text) {
        feedbackText.inhaltSetzen(text);
    }
    
    public void juanSagt(String text) {
        juanDialog.inhaltSetzen(text);
    }
    
    public int getPreis(String item, int level) {
        if (level == 0) {
            if (item.equals("Suppe")) return 80;
            if (item.equals("Wasser")) return 60;
            if (item.equals("Bier")) return 40;
            if (item.equals("SchmutzigesWasser")) return 4;
            if (item.equals("VerdorbeneSuppe")) return 5;
        } else {
            if (item.equals("Suppe")) return 200;
            if (item.equals("Wasser")) return 150;
            if (item.equals("Bier")) return 100;
            if (item.equals("SchmutzigesWasser")) return 10;
            if (item.equals("VerdorbeneSuppe")) return 15;
        }
        return 0; // fallback
    }
    
    public void aktualisieren(Spielstand s) {
        int level = s.getTischLevel();
        
        menuSuppe.inhaltSetzen("[3] Suppe - " + getPreis("Suppe", level) + "€");
        menuWasser.inhaltSetzen("[4] Wasser - " + getPreis("Wasser", level) + "€");
        menuBier.inhaltSetzen("[5] Bier - " + getPreis("Bier", level) + "€");
        menuSchmutzWasser.inhaltSetzen("[U] Schm. Wasser - " + getPreis("SchmutzigesWasser", level) + "€ (-Durst, +Verdacht)");
        menuVerdorbeneSuppe.inhaltSetzen("[I] Verd. Suppe - " + getPreis("VerdorbeneSuppe", level) + "€ (-Hunger, Nausea)");
        
        if (s.hatLupe()) {
            itemLupe.inhaltSetzen("[GEKAUFT] Lupe");
            itemLupe.farbeSetzen("Grün");
        }
    }
}
