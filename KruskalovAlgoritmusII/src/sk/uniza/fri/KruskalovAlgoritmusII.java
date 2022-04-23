package sk.uniza.fri;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * 20. 4. 2022 - 10:31
 *
 * @author Fíla
 */
public class KruskalovAlgoritmusII {

    private int pocetVrcholov;
    private int najvacsieOhodnotenie;
    private int[][] hrany;

    public KruskalovAlgoritmusII(String cestaKSuboru) {
        Instant start = Instant.now();

        this.pocetVrcholov = 0;
        this.hrany = this.nacitajDataZoSuboru(cestaKSuboru);

        this.zoradPrvkyPodlaOhodnotenia(this.hrany);
        this.najdiNajlacnejsiuKostru();

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        System.out.println("\nTrvanie programu bolo " + timeElapsed.toMillis() + " milisekúnd.");
    }

    public int vratPocetRiadkovVSubore(String cestaKSuboru) {
        System.out.println("[Zisťovanie počtu riadkov v súbore] Pracuje sa na ...");
        int pocetRiadkov = 0;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(cestaKSuboru));
            while (bufferedReader.readLine() != null) {
                pocetRiadkov++;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("[Zisťovanie počtu riadkov v súbore] Hotovo! (" + pocetRiadkov + ")");
        return pocetRiadkov;
    }

    public int[][] nacitajDataZoSuboru(String cestaKSuboru) {
        System.out.println("[Načítavanie dát zo súboru] Pracuje sa na...");
        int[][] data = new int[this.vratPocetRiadkovVSubore(cestaKSuboru)][3];
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(cestaKSuboru));
            int index = 0;
            String riadok;
            while ((riadok = bufferedReader.readLine()) != null) {
                // Upravíme si riadok tak, aby sme mali hodnoty rozdelené len jednou medzerou. Všetky prebytočné "whitespaces" pred a za číslami sa odstránia.
                String upravenyRiadok = riadok.trim().replaceAll("\\s+", " ");
                String[] rozdelene = upravenyRiadok.split(" ");

                // Zapisovanie jednotlivých dát z riadku do poľa { vrchZo, vrchoDo, ohodnotenie }.
                data[index][0] = Integer.parseInt(rozdelene[0]); // vrch zo
                data[index][1] = Integer.parseInt(rozdelene[1]); // vrch do
                data[index][2] = Integer.parseInt(rozdelene[2]); // ohodnotenie hrany / cena hrany

                // Hľadanie počtu vrcholov.
                if (this.pocetVrcholov < data[index][0]) {
                    this.pocetVrcholov = data[index][0];
                }
                if (this.pocetVrcholov < data[index][1]) {
                    this.pocetVrcholov = data[index][1];
                }

                // Hľadanie najväčšieho ohodnotenia.
                if (this.najvacsieOhodnotenie < data[index][2]) {
                    this.najvacsieOhodnotenie = data[index][2];
                }

                index++;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("[Načítavanie dát zo súboru] Hotovo!");
        return data;
    }

    public void zoradPrvkyPodlaOhodnotenia(int[][] data) {
        System.out.println("[Zoraďovanie prvkov podľa ohodnotenia] Pracuje sa na...");
        Arrays.sort(data, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                if (o1[2] < o2[2]) {
                    return -1;
                }
                if (o1[2] > o2[2]) {
                    return 1;
                }
                return 0;
            }
        });
        System.out.println("[Zoraďovanie prvkov podľa ohodnotenia] Hotovo!");
    }

    public void najdiNajlacnejsiuKostru() {
        System.out.println("[Hľadanie najlacnejšej kostry] Pracuje sa na...");

        ArrayList<int[]> najlacnejsiaKostra = new ArrayList<int[]>();

        // Algoritmus je pri väčších objemoch hrán rýchlejší kvôli komplexnejšiemu meneniu
        // vrcholov s väčšou hodnotou na menšiu. Vytváram si 2 polia, kde do v 1. (vrcholy) sú indexy
        // vrcholy a hodnoty sú hodnoty vrcholov. V 2. poli (hodnoty) sú indexy hodnoty a hodnoty
        // na danom indexe sú vrcholy, ktoré danú hodnotu majú priradenú. Takže pri menení väčších hodnôt
        // vrcholom na menšie nemusím prechádzať všetkými vrcholmi v poli s vrcholmi (vrcholy) a tam,
        // kde je tá hodnota väčšia, ju zmením na menšiu. Stačí mi vedieť len vrcholy, ktoré majú tú
        // väčšiu hodnotu a keďže vrcholy sú v poli s vrcholmi (vrcholy) ako indexy, tak pristúpim
        // len ku špecifickým prvkom.

        //               Príklad:
        //  Pole "vrcholy"      Pole "hodnoty"
        //  index | hodnota     index | hodnota
        //    0   |    0          0   |    0
        //    1   |    23         1   |    4
        //    2   |    7         ...  |   ...
        //    3   |    23         7   |    2
        //    4   |    1         ...  |   ...
        //    5   |    23         23  |  1;3;5

        // Vytvorenie poľa Stringov, kde index je hodnota vrchola a hodnota na danom indexe je vrchol alebo sú vrcholy, ktoré danú hodnotu majú.
        String[] hodnoty = this.pocetVrcholov > this.najvacsieOhodnotenie ? new String[this.pocetVrcholov + 1] : new String[this.najvacsieOhodnotenie + 1];

        // Vytvorenie poľa Integerov, kde index je vrchol a hodnota na danom indexe je hodnota vrchola.
        int[] vrcholy = new int[this.pocetVrcholov + 1];

        for (int i = 1; i <= this.pocetVrcholov; i++) {
            hodnoty[i] = String.valueOf(i);
            vrcholy[i] = i;
        }

        for (int i = 0; i < this.hrany.length; i++) {
            if (najlacnejsiaKostra.size() == this.pocetVrcholov) {
                break;
            }

            int[] hrana = this.hrany[i];

            int hodnotaVrcholaZo = vrcholy[hrana[0]];
            int hodnotaVrcholaDo = vrcholy[hrana[1]];

            // Ak sa hodnoty vrcholov rovnajú, tak hranu "zahodím".
            if (hodnotaVrcholaZo == hodnotaVrcholaDo) {
                continue;
            }

            najlacnejsiaKostra.add(hrana);

            // Porovnanie, ktorý vrchol má menšiu a väčšiu hodnotu. Nasledne hodnoty zapíšem do premenných.
            int mensiaHodnotaVrchola = 0;
            int vacsiaHodnotaVrchola = 0;
            if (hodnotaVrcholaZo < hodnotaVrcholaDo) {
                mensiaHodnotaVrchola = hodnotaVrcholaZo;
                vacsiaHodnotaVrchola = hodnotaVrcholaDo;
            } else {
                mensiaHodnotaVrchola = hodnotaVrcholaDo;
                vacsiaHodnotaVrchola = hodnotaVrcholaZo;
            }

            // Načítam si jednotlivé vrcholy s väčšou hodnotou do poľa Stringov. Prejdem si každým vrcholom
            // a zmením podľa neho hodnotu v poli s vrcholmi. Následne pridám bývale vrcholy s väčšiou hodnotou
            // do Stringu, kde sa nachádzajú vrcholy s menšou hodnotou. Vrcholy s väčšou hodnotou odstránim
            // zo Stringu, kde boli predtým.

            // Načítam si jednotlivé vrcholy s väčšou hodnotou do poľa Stringov.
            String[] vrcholySVacsouHodnotou = hodnoty[vacsiaHodnotaVrchola].split(";");

            // Prejdem si každým vrcholom a zmením podľa neho hodnotu v poli s vrcholmi.
            for (String vrchol : vrcholySVacsouHodnotou) {
                if (vrchol.isEmpty()) {
                    continue;
                }
                vrcholy[Integer.parseInt(vrchol)] = mensiaHodnotaVrchola;
            }

            // Následne pridám bývale vrcholy s väčšou hodnotou do Stringu, kde sa nachádzajú vrcholy
            // s menšou hodnotou. Vrcholy s väčšou hodnotou odstránim zo Stringu, kde boli predtým.
            hodnoty[mensiaHodnotaVrchola] += ";" + hodnoty[vacsiaHodnotaVrchola];
            hodnoty[vacsiaHodnotaVrchola] = "";
        }

        System.out.println("[Hľadanie najlacnejšej kostry] Hotovo!");
        this.vypisNajlacnejsiuKostru(najlacnejsiaKostra);
    }

    private void vypisNajlacnejsiuKostru(ArrayList<int[]> najlacnejsiaKostra) {
        int cena = 0;
        System.out.print("\nNajlacnejšia kostra: ");
        for (int i = 0; i < najlacnejsiaKostra.size(); i++) {
            if (najlacnejsiaKostra.size() - 1 != i) {
                System.out.format("{%d, %d}, ", najlacnejsiaKostra.get(i)[0], najlacnejsiaKostra.get(i)[1]);
            } else {
                System.out.format("{%d, %d}", najlacnejsiaKostra.get(i)[0], najlacnejsiaKostra.get(i)[1]);
            }
            cena += najlacnejsiaKostra.get(i)[2];
        }

        System.out.println("\nCena najlacnejšej kostry je: " + cena);
    }
}
