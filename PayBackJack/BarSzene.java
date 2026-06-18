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
    
    // Regal & Items (Grafisch)
    private BildE iconSuppe, iconWasser, iconBier, iconSchmutzWasser, iconVerdorbeneSuppe, iconLupe, iconZigarette;
    private TextE preisSuppe, preisWasser, preisBier, preisSchmutzWasser, preisVerdorbeneSuppe, preisLupe, preisZigarette;
    
    private TextE itemTitle;
    
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
        
        // Menü Title
        menuTitle = new TextE("--- REGAL ---");
        menuTitle.positionSetzen(1400, 50);
        menuTitle.farbeSetzen("Gelb");
        menuTitle.groesseSetzen(24);
        menuTitle.sichtbarSetzen(false);
        
        // Helper für Shelf Items
        int startX = 1400;
        int startY = 100;
        int stepY = 80;
        int size = 40; // Wir skalieren die 20x20 Bilder ca 2x
        
        iconSuppe = erstelleIcon(startX, startY, "Suppe");
        preisSuppe = erstellePreis(startX + 60, startY + 10, "Suppe");
        
        iconWasser = erstelleIcon(startX, startY + stepY, "Wasser");
        preisWasser = erstellePreis(startX + 60, startY + stepY + 10, "Wasser");
        
        iconBier = erstelleIcon(startX, startY + stepY * 2, "Bier");
        preisBier = erstellePreis(startX + 60, startY + stepY * 2 + 10, "Bier");
        
        iconSchmutzWasser = erstelleIcon(startX, startY + stepY * 3, "SchmutzigesWasser");
        preisSchmutzWasser = erstellePreis(startX + 60, startY + stepY * 3 + 10, "Schmutziges Wasser");
        
        iconVerdorbeneSuppe = erstelleIcon(startX, startY + stepY * 4, "VerdorbeneSuppe");
        preisVerdorbeneSuppe = erstellePreis(startX + 60, startY + stepY * 4 + 10, "Verdorbene Suppe");
        
        // Spezial Items
        itemTitle = new TextE("--- SPEZIAL ---");
        itemTitle.positionSetzen(1400, startY + stepY * 5);
        itemTitle.farbeSetzen("Rot");
        itemTitle.groesseSetzen(20);
        itemTitle.sichtbarSetzen(false);
        
        iconLupe = erstelleIcon(startX, startY + stepY * 5 + 40, "Lupe");
        preisLupe = erstellePreis(startX + 60, startY + stepY * 5 + 50, "Lupe");
        
        iconZigarette = erstelleIcon(startX, startY + stepY * 6 + 40, "Zigarette");
        preisZigarette = erstellePreis(startX + 60, startY + stepY * 6 + 50, "Zigarette");
        
        // Steuerung
        steuerungText = new TextE("[1] Zum Tisch | Klicke auf Items zum Kaufen");
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
        
        FontHelper.anwenden(juanName, juanDialog, menuTitle);
        FontHelper.anwenden(preisSuppe, preisWasser, preisBier, preisSchmutzWasser, preisVerdorbeneSuppe);
        FontHelper.anwenden(itemTitle, preisLupe, preisZigarette, steuerungText, feedbackText);
        
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
        iconSuppe.sichtbarSetzen(true); preisSuppe.sichtbarSetzen(true);
        iconWasser.sichtbarSetzen(true); preisWasser.sichtbarSetzen(true);
        iconBier.sichtbarSetzen(true); preisBier.sichtbarSetzen(true);
        iconSchmutzWasser.sichtbarSetzen(true); preisSchmutzWasser.sichtbarSetzen(true);
        iconVerdorbeneSuppe.sichtbarSetzen(true); preisVerdorbeneSuppe.sichtbarSetzen(true);
        
        itemTitle.sichtbarSetzen(true);
        iconLupe.sichtbarSetzen(true); preisLupe.sichtbarSetzen(true);
        iconZigarette.sichtbarSetzen(true); preisZigarette.sichtbarSetzen(true);
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
        iconSuppe.sichtbarSetzen(false); preisSuppe.sichtbarSetzen(false);
        iconWasser.sichtbarSetzen(false); preisWasser.sichtbarSetzen(false);
        iconBier.sichtbarSetzen(false); preisBier.sichtbarSetzen(false);
        iconSchmutzWasser.sichtbarSetzen(false); preisSchmutzWasser.sichtbarSetzen(false);
        iconVerdorbeneSuppe.sichtbarSetzen(false); preisVerdorbeneSuppe.sichtbarSetzen(false);
        
        itemTitle.sichtbarSetzen(false);
        iconLupe.sichtbarSetzen(false); preisLupe.sichtbarSetzen(false);
        iconZigarette.sichtbarSetzen(false); preisZigarette.sichtbarSetzen(false);
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
        
        preisSuppe.inhaltSetzen("Suppe - " + getPreis("Suppe", level) + "€");
        preisWasser.inhaltSetzen("Wasser - " + getPreis("Wasser", level) + "€");
        preisBier.inhaltSetzen("Bier - " + getPreis("Bier", level) + "€");
        preisSchmutzWasser.inhaltSetzen("Schmutz. W. - " + getPreis("SchmutzigesWasser", level) + "€");
        preisVerdorbeneSuppe.inhaltSetzen("Verdorb. S. - " + getPreis("VerdorbeneSuppe", level) + "€");
        
        preisLupe.inhaltSetzen("Lupe - 10000€");
        preisZigarette.inhaltSetzen("Zigarette - 500€");
        
        if (s.hatLupe()) {
            preisLupe.inhaltSetzen("[GEKAUFT] Lupe");
            preisLupe.farbeSetzen("Grün");
        } else {
            preisLupe.farbeSetzen("Weiß");
        }
    }
    
    private BildE erstelleIcon(int x, int y, String name) {
        BildE b = new BildE(x, y, "../Assets/Sprites/Items/" + name + "_20x20.png");
        b.skalieren(2.0f); // -> 40x40 sichtbar
        b.sichtbarSetzen(false);
        return b;
    }
    
    private TextE erstellePreis(int x, int y, String text) {
        TextE t = new TextE(text);
        t.positionSetzen(x, y);
        t.farbeSetzen("Weiß");
        t.groesseSetzen(16);
        t.sichtbarSetzen(false);
        return t;
    }
    
    // Gibt das geklickte Item zurück oder null
    public String checkKaufKlick(int x, int y) {
        if (!sichtbar) return null;
        
        int w = 40; int h = 40;
        
        if (x >= iconSuppe.zentrumX() - w/2 && x <= iconSuppe.zentrumX() + w/2 && y >= iconSuppe.zentrumY() - h/2 && y <= iconSuppe.zentrumY() + h/2) return "Suppe";
        if (x >= iconWasser.zentrumX() - w/2 && x <= iconWasser.zentrumX() + w/2 && y >= iconWasser.zentrumY() - h/2 && y <= iconWasser.zentrumY() + h/2) return "Wasser";
        if (x >= iconBier.zentrumX() - w/2 && x <= iconBier.zentrumX() + w/2 && y >= iconBier.zentrumY() - h/2 && y <= iconBier.zentrumY() + h/2) return "Bier";
        if (x >= iconSchmutzWasser.zentrumX() - w/2 && x <= iconSchmutzWasser.zentrumX() + w/2 && y >= iconSchmutzWasser.zentrumY() - h/2 && y <= iconSchmutzWasser.zentrumY() + h/2) return "SchmutzigesWasser";
        if (x >= iconVerdorbeneSuppe.zentrumX() - w/2 && x <= iconVerdorbeneSuppe.zentrumX() + w/2 && y >= iconVerdorbeneSuppe.zentrumY() - h/2 && y <= iconVerdorbeneSuppe.zentrumY() + h/2) return "VerdorbeneSuppe";
        if (x >= iconLupe.zentrumX() - w/2 && x <= iconLupe.zentrumX() + w/2 && y >= iconLupe.zentrumY() - h/2 && y <= iconLupe.zentrumY() + h/2) return "Lupe";
        if (x >= iconZigarette.zentrumX() - w/2 && x <= iconZigarette.zentrumX() + w/2 && y >= iconZigarette.zentrumY() - h/2 && y <= iconZigarette.zentrumY() + h/2) return "Zigarette";
        
        return null;
    }
}
