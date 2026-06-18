import ea.edu.*;
import ea.Sound;

public class DurchsuchungMinigame {
    
    private RechteckE leisteBg;
    private RechteckE safeZone;
    private RechteckE zeiger;
    private TextE statusText;
    private BildE warnungBild;
    
    private boolean aktiv;
    private double zeigerPos; // 0.0 to 1.0
    private double zeigerSpeed;
    private int safeBreiteProzent; // 0 to 100
    private int safePosProzent; // 0 to 100
    private int schwierigkeitCounter; // wie oft schon durchsucht
    
    private Sound sfxStart;
    
    private Thread animationThread;
    
    private static final int LEISTE_X = 600;
    private static final int LEISTE_Y = 500;
    private static final int LEISTE_W = 600;
    private static final int LEISTE_H = 40;
    
    public DurchsuchungMinigame() {
        leisteBg = new RechteckE();
        leisteBg.positionSetzen(LEISTE_X, LEISTE_Y);
        leisteBg.breiteSetzen(LEISTE_W);
        leisteBg.hoeheSetzen(LEISTE_H);
        leisteBg.farbeSetzen("Grau");
        leisteBg.sichtbarSetzen(false);
        
        safeZone = new RechteckE();
        safeZone.positionSetzen(LEISTE_X, LEISTE_Y);
        safeZone.hoeheSetzen(LEISTE_H);
        safeZone.farbeSetzen("Grün");
        safeZone.sichtbarSetzen(false);
        
        zeiger = new RechteckE();
        zeiger.breiteSetzen(10);
        zeiger.hoeheSetzen(LEISTE_H + 20);
        zeiger.positionSetzen(LEISTE_X, LEISTE_Y - 10);
        zeiger.farbeSetzen("Weiß");
        zeiger.sichtbarSetzen(false);
        
        statusText = new TextE("DURCHSUCHUNG! Druecke LEERTASTE!");
        statusText.positionSetzen(LEISTE_X, LEISTE_Y - 50);
        statusText.farbeSetzen("Rot");
        statusText.groesseSetzen(24);
        statusText.sichtbarSetzen(false);
        
        warnungBild = new BildE(LEISTE_X - 100, LEISTE_Y - 50, "../Assets/Sprites/Tisch/JackWarnung.png");
        warnungBild.sichtbarSetzen(false);
        
        FontHelper.anwenden(statusText);
        
        sfxStart = new Sound("../Assets/Sounds/SFX/sfx_search_start.wav");
        
        aktiv = false;
        schwierigkeitCounter = 0;
    }
    
    public void starten(boolean extremSchwer) {
        aktiv = true;
        
        if (extremSchwer) {
            safeBreiteProzent = 5; // Fast unmoeglich
            zeigerSpeed = 0.04;
        } else {
            safeBreiteProzent = 30 - (schwierigkeitCounter * 10);
            if (safeBreiteProzent < 5) safeBreiteProzent = 5;
            zeigerSpeed = 0.02 + (schwierigkeitCounter * 0.005);
            schwierigkeitCounter++;
        }
        
        // Random Position für SafeZone
        safePosProzent = (int)(Math.random() * (100 - safeBreiteProzent));
        
        int sW = (int)(LEISTE_W * (safeBreiteProzent / 100.0));
        int sX = LEISTE_X + (int)(LEISTE_W * (safePosProzent / 100.0));
        
        safeZone.breiteSetzen(sW);
        safeZone.positionSetzen(sX, LEISTE_Y);
        
        zeigerPos = 0.0;
        
        leisteBg.sichtbarSetzen(true);
        safeZone.sichtbarSetzen(true);
        zeiger.sichtbarSetzen(true);
        statusText.sichtbarSetzen(true);
        warnungBild.sichtbarSetzen(true);
        
        sfxStart.play();
        
        animationThread = new Thread(() -> {
            boolean right = true;
            while(aktiv) {
                if (right) {
                    zeigerPos += zeigerSpeed;
                    if (zeigerPos >= 1.0) right = false;
                } else {
                    zeigerPos -= zeigerSpeed;
                    if (zeigerPos <= 0.0) right = true;
                }
                
                int zX = LEISTE_X + (int)(LEISTE_W * zeigerPos);
                zeiger.positionSetzen(zX, LEISTE_Y - 10);
                
                try { Thread.sleep(16); } catch(Exception e) {}
            }
        });
        animationThread.start();
    }
    
    /**
     * Stoppt den Zeiger und gibt zurück ob man in der SafeZone war.
     */
    public boolean stoppen() {
        aktiv = false;
        int posP = (int)(zeigerPos * 100);
        boolean erfolg = (posP >= safePosProzent && posP <= (safePosProzent + safeBreiteProzent));
        
        leisteBg.sichtbarSetzen(false);
        safeZone.sichtbarSetzen(false);
        zeiger.sichtbarSetzen(false);
        statusText.sichtbarSetzen(false);
        warnungBild.sichtbarSetzen(false);
        
        return erfolg;
    }
    
    public boolean istAktiv() {
        return aktiv;
    }
    
    public void verstecken() {
        aktiv = false;
        leisteBg.sichtbarSetzen(false);
        safeZone.sichtbarSetzen(false);
        zeiger.sichtbarSetzen(false);
        statusText.sichtbarSetzen(false);
        warnungBild.sichtbarSetzen(false);
    }
}
