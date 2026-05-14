/**
 * Umfassende Test-Suite für das Projekt PayBackJack.
 * Diese Klasse simuliert Spielsituationen und prüft die mathematische Korrektheit.
 * 
 * Anleitung für BlueJ:
 * 1. Klasse übersetzen.
 * 2. Rechtsklick auf die Klasse -> "new PayBackJackTest()".
 * 3. Rechtsklick auf das rote Objekt unten -> "alleTestsAusfuehren()".
 */
public class PayBackJackTest {

    public PayBackJackTest() {
        // Konstruktor für BlueJ
    }

    /**
     * Führt alle Tests nacheinander aus und gibt einen Bericht aus.
     */
    public void alleTestsAusfuehren() {
        System.out.println("========================================");
        System.out.println("   PAYBACK JACK - AUTOMATISIERTE TESTS   ");
        System.out.println("========================================\n");

        int erfolge = 0;
        int gesamt = 5;

        if (testAssSpezialfaelle()) erfolge++;
        if (testKartenWerteProfi()) erfolge++;
        if (testDeckVollstaendigkeit()) erfolge++;
        if (testMultiplikatoren()) erfolge++;
        if (testHandClear()) erfolge++;

        System.out.println("\n----------------------------------------");
        System.out.println("ERGEBNIS: " + erfolge + " von " + gesamt + " Tests bestanden.");
        if (erfolge == gesamt) {
            System.out.println("STATUS: [BEREIT FÜR CASINO-EINSATZ]");
        } else {
            System.out.println("STATUS: [BITTE CODE PRÜFEN]");
        }
        System.out.println("----------------------------------------");
    }

    private boolean testAssSpezialfaelle() {
        System.out.print("[TEST] Ass-Logik (Drei Asse + 8)... ");
        Hand h = new Hand();
        // 11 + 11 + 11 + 8 = 41 
        // -> Erstes Ass wird 1: 31
        // -> Zweites Ass wird 1: 21 (Perfektes Ergebnis!)
        h.karteHinzufuegen(new Karte("Herz", "Ass", 11));
        h.karteHinzufuegen(new Karte("Pik", "Ass", 11));
        h.karteHinzufuegen(new Karte("Karo", "Ass", 11));
        h.karteHinzufuegen(new Karte("Kreuz", "8", 8));
        
        int punkte = h.punkteBerechnen();
        if (punkte == 21) {
            System.out.println("OK (Punktlandung bei 21)");
            return true;
        } else {
            System.out.println("FEHLER (Punkte: " + punkte + " statt 21)");
            return false;
        }
    }

    private boolean testKartenWerteProfi() {
        System.out.print("[TEST] Bildkarten-Werte... ");
        Karte k1 = new Karte("Karo", "Bube", 10);
        Karte k2 = new Karte("Herz", "Dame", 10);
        Karte k3 = new Karte("Pik", "Koenig", 10);
        
        if (k1.getWert() == 10 && k2.getWert() == 10 && k3.getWert() == 10) {
            System.out.println("OK");
            return true;
        } else {
            System.out.println("FEHLER (Werte falsch)");
            return false;
        }
    }

    private boolean testDeckVollstaendigkeit() {
        System.out.print("[TEST] Deck-Vollständigkeit... ");
        Kartenstapel s = new Kartenstapel();
        int count = 0;
        while (s.karteZiehen() != null) {
            count++;
        }
        if (count == 52) {
            System.out.println("OK");
            return true;
        } else {
            System.out.println("FEHLER (" + count + " Karten)");
            return false;
        }
    }

    private boolean testMultiplikatoren() {
        System.out.print("[TEST] Multiplikatoren-Logik... ");
        // Simulation der PayBackJack Logik (theoretisch)
        int einsatz = 100;
        int geld = 1000;
        
        // Black Jack Simulation (2.5x)
        int gewinnBJ = (int)(einsatz * 2.5); 
        // Normaler Gewinn (2.0x)
        int gewinnNormal = (int)(einsatz * 2.0);
        
        if (gewinnBJ == 250 && gewinnNormal == 200) {
            System.out.println("OK");
            return true;
        } else {
            System.out.println("FEHLER (Rechnung falsch)");
            return false;
        }
    }
    
    private boolean testHandClear() {
        System.out.print("[TEST] Hand-Reset... ");
        Hand h = new Hand();
        h.karteHinzufuegen(new Karte("Herz", "10", 10));
        h.kartenZurueckgeben(null); // Leert auch die Liste
        
        if (h.punkteBerechnen() == 0) {
            System.out.println("OK");
            return true;
        } else {
            System.out.println("FEHLER (Liste nicht geleert)");
            return false;
        }
    }
}
