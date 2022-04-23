package sk.uniza.fri;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * 20. 4. 2022 - 10:31
 *
 * @author Fíla
 */
public class KruskalovAlgoritmusII {

    private int pocetVrcholov;
    private ArrayList<int[]> hrany;

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

    public ArrayList<int[]> nacitajDataZoSuboru(String cestaKSuboru) {
        System.out.println("[Načítavanie dát zo súboru] Pracuje sa na...");
        ArrayList<int[]> data = new ArrayList<int[]>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(cestaKSuboru));
            String riadok;
            while ((riadok = bufferedReader.readLine()) != null) {
                // Upravíme si načítavaný riadok tak, aby sme mali hodnoty rozdelené len jednou medzerou.
                String upravenyRiadok = riadok.trim().replaceAll("\\s+", " ");
                String[] rozdelenyRiadok = upravenyRiadok.split(" ");

                // Zapisovanie jednotlivých dát z riadku do poľa { vrchZo, vrchDo, ohodnotenie }.
                int[] hrana = new int[3];
                hrana[0] = Integer.parseInt(rozdelenyRiadok[0]);
                hrana[1] = Integer.parseInt(rozdelenyRiadok[1]);
                hrana[2] = Integer.parseInt(rozdelenyRiadok[2]);
                data.add(hrana);

                // Hľadanie počtu vrcholov.
                if (this.pocetVrcholov < hrana[0]) {
                    this.pocetVrcholov = hrana[0];
                }
                if (this.pocetVrcholov < hrana[1]) {
                    this.pocetVrcholov = hrana[1];
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("[Načítavanie dát zo súboru] Hotovo!");
        return data;
    }

    public void zoradPrvkyPodlaOhodnotenia(ArrayList<int[]> data) {
        System.out.println("[Zoraďovanie prvkov podľa ohodnotenia] Pracuje sa na...");
        data.sort(new Comparator<int[]>() {
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


    // Veeeľmi pomalé.
//    public void zoradPrvkyPodlaOhodnotenia(ArrayList<int[]> data) {
//        System.out.println("[Zoraďovanie prvkov podľa ohodnotenia] Pracuje sa na...");
//        for (int i = 0; i < data.size() - 1; i++) {
//            System.out.println("Index: " + i);
//            boolean boliVymenene = false;
//            for (int j = 0; j < data.size() - i - 1; j++) {
//                if (data.get(j)[2] > data.get(j + 1)[2]) {
//                    int[] docasne = new int[3];
//                    docasne[0] = data.get(j)[0];
//                    docasne[1] = data.get(j)[1];
//                    docasne[2] = data.get(j)[2];
//
//                    data.get(j)[0] = data.get(j + 1)[0];
//                    data.get(j)[1] = data.get(j + 1)[1];
//                    data.get(j)[2] = data.get(j + 1)[2];
//
//                    data.get(j + 1)[0] = docasne[0];
//                    data.get(j + 1)[1] = docasne[1];
//                    data.get(j + 1)[2] = docasne[2];
//
//                    boliVymenene = true;
//                }
//            }
//            if (!boliVymenene) {
//                break;
//            }
//        }
//        System.out.println("[Zoraďovanie prvkov podľa ohodnotenia] Hotovo!");
//    }

    public void najdiNajlacnejsiuKostru() {
        System.out.println("[Hľadanie najlacnejšej kostry] Pracuje sa na...");

        ArrayList<int[]> najlacnejsiaKostra = new ArrayList<int[]>();

        // Vytvorím si pole s vrcholmi a nastavím im hodnotu od 1 po celkový počet vrcholov.
        int[] vrcholy = new int[this.pocetVrcholov + 1];
        for (int i = 0; i < vrcholy.length; i++) {
            vrcholy[i] = i;
        }

        while (najlacnejsiaKostra.size() != vrcholy.length - 1 || !this.hrany.isEmpty()) {
            if (this.hrany.isEmpty()) {
                break;
            }

            int[] hrana = this.hrany.get(0);
            this.hrany.remove(0);

            int u = vrcholy[hrana[0]]; // prvý vrchol
            int v = vrcholy[hrana[1]]; // druhý vrchol

            if (u == v) {
                continue;
            }

            najlacnejsiaKostra.add(hrana);

            int mensie = 0;
            int vacsie = 0;
            if (u < v) {
                mensie = u;
                vacsie = v;
            } else {
                mensie = v;
                vacsie = u;
            }

            for (int i = 0; i < vrcholy.length; i++) {
                if (vrcholy[i] == vacsie) {
                    vrcholy[i] = mensie;
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
