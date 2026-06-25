import ea.edu.*;
import java.util.Timer;
import java.util.TimerTask;

public class Huan {
    private BildE bodyClosed;
    private BildE bodyOpen;
    private TextE dialogText;
    
    private String fullText = "";
    private String currentText = "";
    private int textIndex = 0;
    
    private Timer timer;
    private boolean isTalking = false;
    private boolean mouthOpen = false;
    private int mouthToggleCounter = 0;
    
    public Huan(int x, int y) {
        super();
        
        bodyClosed = new BildE(x, y, "../Assets/Sprites/Huan/huan_closed.png");
        bodyOpen = new BildE(x, y, "../Assets/Sprites/Huan/huan_open.png");
        bodyOpen.sichtbarSetzen(false);
        // Text is positioned above Huan
        dialogText = new TextE("");
        dialogText.positionSetzen(x - 50, y - 40);
        dialogText.farbeSetzen("Weiß");
        FontHelper.anwenden(dialogText);
    }
    
    public void sichtbarSetzen(boolean sichtbar) {
        if (sichtbar) {
            zeigen();
        } else {
            verstecken();
        }
    }
    
    public void sprich(String text) {
        if (timer != null) {
            timer.cancel();
        }
        
        this.fullText = text;
        this.currentText = "";
        this.textIndex = 0;
        this.isTalking = true;
        this.mouthOpen = false;
        
        bodyClosed.sichtbarSetzen(false);
        bodyOpen.sichtbarSetzen(true);
        dialogText.inhaltSetzen("");
        
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (textIndex < fullText.length()) {
                    currentText += fullText.charAt(textIndex);
                    dialogText.inhaltSetzen(currentText);
                    textIndex++;
                    
                    mouthToggleCounter++;
                    if (mouthToggleCounter % 3 == 0) {
                        mouthOpen = !mouthOpen;
                        bodyOpen.sichtbarSetzen(mouthOpen);
                        bodyClosed.sichtbarSetzen(!mouthOpen);
                    }
                } else {
                    isTalking = false;
                    bodyOpen.sichtbarSetzen(false);
                    bodyClosed.sichtbarSetzen(true);
                    timer.cancel();
                }
            }
        }, 0, 40); // 40ms per letter
    }
    
    public void verstecken() {
        bodyClosed.sichtbarSetzen(false);
        bodyOpen.sichtbarSetzen(false);
        dialogText.sichtbarSetzen(false);
        dialogText.inhaltSetzen("");
        if (timer != null) timer.cancel();
    }
    
    public void zeigen() {
        bodyClosed.sichtbarSetzen(true);
        bodyOpen.sichtbarSetzen(false);
        dialogText.sichtbarSetzen(true);
    }
    
    public void setzePunkte(String text) {
        dialogText.inhaltSetzen(text);
        if (timer != null) timer.cancel();
        bodyClosed.sichtbarSetzen(true);
        bodyOpen.sichtbarSetzen(false);
    }
}
