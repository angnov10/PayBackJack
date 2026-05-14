import java.util.ArrayList;
import java.util.Collections;

/**
 * Ein Stapel mit 52 Spielkarten.
 */
public class Kartenstapel {
    private ArrayList<Karte> karten;

    public Kartenstapel() {
        // Wichtig: Liste hier initialisieren
        this.karten = new ArrayList<Karte>();
        this.erzeugeKarten();
        this.mischen();
    }

    private void erzeugeKarten() {
        String[] farben = {"Herz", "Karo", "Pik", "Kreuz"};
        String[] namen = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Bube", "Dame", "Koenig", "Ass"};
        int[] werte = {2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10, 11};

        for (String f : farben) {
            for (int i = 0; i < namen.length; i++) {
                Karte neueKarte = new Karte(f, namen[i], werte[i]);
                this.karten.add(neueKarte);
            }
        }
    }

    public void mischen() {
        Collections.shuffle(this.karten);
    }

    public Karte karteZiehen() {
        if (this.karten.size() > 0) {
            return this.karten.remove(0);
        }
        return null;
    }
    
    /**
     * Nimmt eine Karte wieder in den Stapel auf (verhindert Bild-Duplikate).
     */
    public void zuruecknehmen(Karte k) {
        if (k != null) {
            karten.add(k);
        }
    }
}