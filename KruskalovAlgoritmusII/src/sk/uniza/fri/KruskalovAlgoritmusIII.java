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
public class KruskalovAlgoritmusIII {

    private int pocetVrcholov;
    private int[][] hrany;

    public KruskalovAlgoritmusIII(String cestaKSuboru) {
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

        int[] vrcholy = new int[this.pocetVrcholov + 1];
        for (int i = 1; i <= this.pocetVrcholov; i++) {
            vrcholy[i] = i;
        }

        for (int i = 0; i < this.hrany.length; i++) {
            if (najlacnejsiaKostra.size() == vrcholy.length - 1) {
                break;
            }

            int[] hrana = this.hrany[i];

            int hodnotaVrcholaZo = vrcholy[hrana[0]];
            int hodnotaVrcholaDo = vrcholy[hrana[1]];

            if (hodnotaVrcholaZo == hodnotaVrcholaDo) {
                continue;
            }

            najlacnejsiaKostra.add(hrana);

            int mensiaHodnotaVrchola, vacsiaHodnotaVrchola = 0;
            if (hodnotaVrcholaZo < hodnotaVrcholaDo) {
                mensiaHodnotaVrchola = hodnotaVrcholaZo;
                vacsiaHodnotaVrchola = hodnotaVrcholaDo;
            } else {
                mensiaHodnotaVrchola = hodnotaVrcholaDo;
                vacsiaHodnotaVrchola = hodnotaVrcholaZo;
            }

            for (int j = 0; j < vrcholy.length; j++) {
                if (vrcholy[j] == vacsiaHodnotaVrchola) {
                    vrcholy[j] = mensiaHodnotaVrchola;
                }
            }
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
