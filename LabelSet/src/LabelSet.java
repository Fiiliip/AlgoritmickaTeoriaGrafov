import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

public class LabelSet {

    private int pocetVrcholov;
    private int[][] hrany;
    private int[] lokacieVrcholov;

    public LabelSet(String cestaKSuboru, int zaciatocnyVrchol, int koncovyVrchol) {
        Instant start = Instant.now();

        this.pocetVrcholov = 0;

        this.hrany = this.nacitajDataZoSuboru(cestaKSuboru);
        this.zoradPrvkyPodlaVrcholaOd(hrany);

        this.lokacieVrcholov = this.vratLokacieVrcholov();

        this.najdiNajkratsiuCestu(zaciatocnyVrchol, koncovyVrchol);

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
        int[][] hrany = new int[this.vratPocetRiadkovVSubore(cestaKSuboru)][3];
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(cestaKSuboru));
            int index = 0;
            String riadok;
            while ((riadok = bufferedReader.readLine()) != null) {
                String[] rozdelene = riadok.split(" ");

                // Zapisovanie jednotlivých dát z riadku do poľa { vrchZo, vrchoDo, ohodnotenie }.
                hrany[index][0] = Integer.parseInt(rozdelene[0]); // vrch zo
                hrany[index][1] = Integer.parseInt(rozdelene[1]); // vrch do
                hrany[index][2] = Integer.parseInt(rozdelene[2]); // ohodnotenie hrany / cena hrany

                // Hľadanie počtu vrcholov.
                if (this.pocetVrcholov < hrany[index][0]) {
                    this.pocetVrcholov = hrany[index][0];
                }
                if (this.pocetVrcholov < hrany[index][1]) {
                    this.pocetVrcholov = hrany[index][1];
                }

                index++;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("[Načítavanie dát zo súboru] Hotovo!");
        return hrany;
    }

//    public void zoradPrvkyPodlaVrcholaOd(int[][] data) {
//        System.out.println("[Zoraďovanie prvkov poľa podľa vrchola od] Pracuje sa na...");
//        Arrays.sort(data);
//        System.out.println("[Zoraďovanie prvkov poľa podľa vrchola od] Hotovo!");
//    }

    public void zoradPrvkyPodlaVrcholaOd(int[][] data) {
        System.out.println("[Zoraďovanie prvkov poľa podľa vrchola od] Pracuje sa na...");
        for (int i = 0; i < data.length - 1; i++) {
            boolean boliVymenene = false;
            for (int j = 0; j < data.length - i - 1; j++) {
                if (data[j][0] > data[j + 1][0]) {
                    int[] docasne = data[j];
                    data[j] = data[j + 1];
                    data[j + 1] = docasne;
                    boliVymenene = true;
                }
            }
            if (!boliVymenene) {
                break;
            }
        }
        System.out.println("[Zoraďovanie prvkov poľa podľa vrchola od] Hotovo!");
    }

    public int[] vratLokacieVrcholov() {
        int[] lokacieVrcholov = new int[this.pocetVrcholov + 1];
        for (int hrana = 0, lokacia = 0; hrana < this.hrany.length; hrana++) {
            if (this.hrany[hrana][0] != lokacia) {
                lokacia = this.hrany[hrana][0];
                lokacieVrcholov[lokacia] = hrana;
            }
        }
        return lokacieVrcholov;
    }

    public void najdiNajkratsiuCestu(int zaciatocnyVrchol, int koncovyVrchol) {
        System.out.println("[Hľadanie najkratšej cesty] Pracuje sa na...");

        // Inicializácia polí podľa t(i) / x(i) kde t(i) je najkratšia cesta do vrchoľa a x(i) je predchodca vrchoľa.
        // Inicializujeme si t(i) - najkratšiu cestu do vrchola. Veľkosť bude mať o 1 väčšiu, ako počet vrcholov preto, aby sme indexy v poli mohli použiť ako názvy vrcholov.
        // Pole bude typu double, pretože Double podporuje nastaviť hodnotu na nekonečno (POSITIVE_INFINITY - hodnoty od 0 po "nekonečno"). Môžeme použiť aj pole typu Integer,
        // ale museli by sme potom mierne zmeniť logiku a to napr. tak, že všetkým vrcholom, okrem koncového, nastavíme hodnotu -1.
        double[] najkratsiaCestaDo = new double[this.pocetVrcholov + 1];
        for (int i = 1; i < najkratsiaCestaDo.length ; i++) {
            najkratsiaCestaDo[i] = Double.POSITIVE_INFINITY;
        }
        najkratsiaCestaDo[zaciatocnyVrchol] = 0;

        // Tak isto ako v poli najkratsiaCestaDo, tak aj tu si inicializujeme pole s veľkosťou o 1 väčšiu, aby sme indexy mohli použiť ako názvy vrcholov.
        int[] predchodca = new int[this.pocetVrcholov + 1];
        for (int i = 1; i < predchodca.length; i++) {
            predchodca[i] = 0;
        }

        // Inicializácia množiny epsilon, do ktorej uložíme začiatočný vrchol a do ktorej ukladáme vrcholy, ktorým sa zmenila najkratsiaCestaDo.
        ArrayList<Integer> epsilon = new ArrayList<Integer>();
        epsilon.add(zaciatocnyVrchol);

        while (!epsilon.isEmpty()) {
            int riadiaciVrchol = this.vratVrcholSNajkratsouCestouDo(najkratsiaCestaDo, epsilon);
            epsilon.remove((Integer) riadiaciVrchol);

            // prejdem vsetkymi hranami, kde riadiaci vrchol je vstupny vrchol, tu potrebujem pole, kde budem vediet, kde zacinaju jednotlive vrcholy a kde koncia
            for (int vrchol = this.lokacieVrcholov[riadiaciVrchol]; vrchol < ((riadiaciVrchol + 1) != this.lokacieVrcholov.length ? this.lokacieVrcholov[riadiaciVrchol + 1] : this.lokacieVrcholov.length); vrchol++) {
                if ((najkratsiaCestaDo[riadiaciVrchol] + this.hrany[vrchol][2]) < najkratsiaCestaDo[this.hrany[vrchol][1]]) {
                    najkratsiaCestaDo[this.hrany[vrchol][1]] = najkratsiaCestaDo[riadiaciVrchol] + this.hrany[vrchol][2];
                    predchodca[this.hrany[vrchol][1]] = riadiaciVrchol;
                    epsilon.add(this.hrany[vrchol][1]);
                }
            }
        }

        System.out.println("[Hľadanie najkratšej cesty] Hotovo!");

        this.vypisNajkratsiuCestu(zaciatocnyVrchol, koncovyVrchol, najkratsiaCestaDo, predchodca);
    }

    public int vratVrcholSNajkratsouCestouDo(double[] najkratsiaCestaDo, ArrayList<Integer> epsilon) {
        int vrcholSNajmensouZnackou = epsilon.get(0);
        for (Integer vrchol : epsilon) {
            if (najkratsiaCestaDo[vrchol] < najkratsiaCestaDo[vrcholSNajmensouZnackou]) {
                vrcholSNajmensouZnackou = vrchol;
            }
        }
        return vrcholSNajmensouZnackou;
    }

    public void vypisNajkratsiuCestu(int zaciatocnyVrchol, int koncovyVrchol, double[] najkratsiaCestaDo, int[] predchodca) {
        if (najkratsiaCestaDo[koncovyVrchol] == Double.POSITIVE_INFINITY) {
            System.out.format("\nNajkratšia cesta z %d do %d neexistuje. \n", zaciatocnyVrchol, koncovyVrchol, (int)najkratsiaCestaDo[koncovyVrchol]);
            return;
        }

        ArrayList<Integer> cesta = new ArrayList<Integer>();
        int vrchol = koncovyVrchol;
        while (vrchol != 0) {
            cesta.add(vrchol);
            vrchol = predchodca[vrchol];
        }

        System.out.format("\nNajkratšia cesta z %d do %d je: %d \n", zaciatocnyVrchol, koncovyVrchol, (int)najkratsiaCestaDo[koncovyVrchol]);
        System.out.print("[");
        for (int i = cesta.size() - 1; i >= 0; i--) {
            if (i != 0) {
                System.out.format("%d, ", cesta.get(i));
            } else {
                System.out.format("%d", cesta.get(i));
            }
        }
        System.out.print("]");
    }
}
