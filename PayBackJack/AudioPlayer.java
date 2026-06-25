import javax.sound.sampled.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AudioPlayer {
    private static float globalSfxVolume = 0.5f; // 0.0 to 1.0
    private static float globalMusicVolume = 0.5f;
    private static List<AudioPlayer> allPlayers = new ArrayList<>();
    
    private Clip clip;
    private boolean isMusic;
    private long clipTimePosition;

    public static void setGlobalSfxVolume(float vol) {
        globalSfxVolume = Math.max(0.0f, Math.min(1.0f, vol));
        for(AudioPlayer p : allPlayers) {
            if(!p.isMusic) p.updateVolume();
        }
    }
    public static float getGlobalSfxVolume() { return globalSfxVolume; }
    
    public static void setGlobalMusicVolume(float vol) {
        globalMusicVolume = Math.max(0.0f, Math.min(1.0f, vol));
        for(AudioPlayer p : allPlayers) {
            if(p.isMusic) p.updateVolume();
        }
    }
    public static float getGlobalMusicVolume() { return globalMusicVolume; }

    public AudioPlayer(String filePath, boolean isMusic) {
        this.isMusic = isMusic;
        try {
            File file = new File(filePath);
            if (!file.exists() && !filePath.endsWith(".wav") && !filePath.endsWith(".mp3")) {
                file = new File(filePath + ".wav");
            }
            if(file.exists()) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(file);
                clip = AudioSystem.getClip();
                clip.open(audioIn);
                updateVolume();
            } else {
                System.err.println("Audio file not found: " + filePath);
            }
        } catch (Exception e) {
            System.err.println("Error loading sound: " + filePath);
            e.printStackTrace();
        }
        allPlayers.add(this);
    }
    
    public void updateVolume() {
        if(clip != null && clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float vol = isMusic ? globalMusicVolume : globalSfxVolume;
            
            float db = (vol == 0) ? -80.0f : (float)(Math.log10(vol) * 20.0);
            
            float min = gainControl.getMinimum();
            float max = gainControl.getMaximum();
            
            gainControl.setValue(Math.max(min, Math.min(max, db)));
        }
    }

    public void play() {
        if (clip != null) {
            clip.setFramePosition(0);
            clip.start();
        }
    }

    public void loop() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void pause() {
        if (clip != null && clip.isRunning()) {
            clipTimePosition = clip.getMicrosecondPosition();
            clip.stop();
        }
    }

    public void unpause() {
        if (clip != null) {
            clip.setMicrosecondPosition(clipTimePosition);
            if (!clip.isRunning()) {
                if (isMusic) {
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                } else {
                    clip.start();
                }
            }
        }
    }
}
