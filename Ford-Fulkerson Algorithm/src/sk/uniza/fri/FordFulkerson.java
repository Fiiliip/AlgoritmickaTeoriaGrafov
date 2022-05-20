package sk.uniza.fri;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

/**
 * 19. 5. 2022 - 9:01
 *
 * @author Fíla
 */
public class FordFulkerson {

    private int pocetVrcholov;
    private int[][] hrany;

    private int zdroj;
    private int ustie;

    public FordFulkerson(String cestaKSuboru) {
        Instant start = Instant.now();

        this.pocetVrcholov = 0;
        this.hrany = this.nacitajDataZoSuboru(cestaKSuboru);
        this.zoradHranyPodlaVrcholaOd(this.hrany);

        this.zdroj = 0;
        this.ustie = 0;

        this.najdiZdrojAUstie();
        this.najdiMaximalnyTokVSieti();

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);

        System.out.println("\n\nTrvanie programu bolo " + timeElapsed.toMillis() + " milisekúnd.");
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
        int[][] data = new int[this.vratPocetRiadkovVSubore(cestaKSuboru)][4];
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
                data[index][2] = Integer.parseInt(rozdelene[3]); // priepustnosť
                data[index][3] = 0; // tok

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

    public void zoradHranyPodlaVrcholaOd(int[][] data) {
        System.out.println("[Zoraďovanie prvkov podľa vrchola od] Pracuje sa na...");
        Arrays.sort(data, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                if (o1[0] < o2[0]) {
                    return -1;
                }
                if (o1[0] > o2[0]) {
                    return 1;
                }
                return 0;
            }
        });
        System.out.println("[Zoraďovanie prvkov vrchola od] Hotovo!");
    }

    public void najdiZdrojAUstie() {
        System.out.println("[Hľadanie zdroja a ústia] Pracuje sa na...");
        // Hľadanie zdroja a ústia.
        // Inicializácia 2-rozmerného poľa, kde prvé pole značí vrcholy a druhé pole vstupné a výstupné
        // hodnoty daného poľa.
        // Príklad:
        //  - pocetVstupnychAVystupnychHran[vrchol][pocetVstupnychHran]
        //  - pocetVstupnychAVystupnychHran[vrchol][pocetVystupnychHran]
        int[][] pocetVstupnychAVystupnychHran = new int[this.pocetVrcholov + 1][2];
        for (int hrana = 0; hrana < this.hrany.length; hrana++) {
            pocetVstupnychAVystupnychHran[this.hrany[hrana][1]][0]++;
            pocetVstupnychAVystupnychHran[this.hrany[hrana][0]][1]++;
        }

        for (int vrchol = 0; vrchol < pocetVstupnychAVystupnychHran.length; vrchol++) {
            // Ak vrchol nemá žiadne vstupné hrany, tak ho pridám do zdroja.
            if (pocetVstupnychAVystupnychHran[vrchol][0] == 0) {
                this.zdroj = vrchol;
            }

            // Ak vrchol nemá žiadne výstupné hrany, tak ho pridám do ústia.
            if (pocetVstupnychAVystupnychHran[vrchol][1] == 0) {
                this.ustie = vrchol;
            }

            if (this.zdroj != 0 && this.ustie != 0) {
                break;
            }
        }

        if (this.zdroj == 0) {
            System.out.println("\nZdroj neexistuje! Ukončujem program.");
            System.exit(0);
        }

        if (this.ustie == 0) {
            System.out.println("\nÚstie neexistuje! Ukončujem program.");
            System.exit(0);
        }

        System.out.println("[Hľadanie zdroja a ústia] Hotovo!");
    }

    public ArrayList<Integer> vratZvacsujucuPolocestu() {
//        System.out.println("[Hľadanie zväčšujúcej polocesty] Pracuje sa na...");

        double[] hodnotyVrcholov = new double[this.pocetVrcholov + 1];
        for (int vrchol = 1; vrchol < hodnotyVrcholov.length; vrchol++) {
            hodnotyVrcholov[vrchol] = Double.POSITIVE_INFINITY;
        }
        hodnotyVrcholov[this.zdroj] = 0;

        ArrayList<Integer> epsilon = new ArrayList<>();
        epsilon.add(this.zdroj);
        while (!epsilon.isEmpty()) {
            int vrchol = epsilon.remove(0);
            for (int hrana = 0; hrana < this.hrany.length; hrana++) {
                int vrchZo = this.hrany[hrana][0];
                int vrchDo = this.hrany[hrana][1];
                int priepustnost = this.hrany[hrana][2];
                int tok = this.hrany[hrana][3];

                // Ak je v SMERE.
                if (vrchZo == vrchol) {
                    // Ak hodnota vrchola do je rovná nekonečnu, teda nemá nastavenú inú hodnotu, než nekonečno.
                    if (hodnotyVrcholov[vrchDo] == Double.POSITIVE_INFINITY) {
                        if ((priepustnost - tok) != 0) {
                            hodnotyVrcholov[vrchDo] = vrchZo;
                            epsilon.add(vrchDo);

                            if (vrchDo == this.ustie) {
//                                System.out.println("[Hľadanie zväčšujúcej polocesty] Hotovo!");
                                return this.vratUpravenuZvacsujucuPolocestu(hodnotyVrcholov);
                            }
                        }
                    }
                }

                // Ak je v PROTISMERE.
                if (vrchDo == vrchol) {
                    // Ak hodnota vrchola do je rovná nekonečnu, teda nemá nastavenú inú hodnotu, než nekonečno.
                    if (hodnotyVrcholov[vrchZo] == Double.POSITIVE_INFINITY) {
                        if (tok != 0) {
                            hodnotyVrcholov[vrchZo] = -vrchDo;
                            epsilon.add(vrchZo);

                            if (vrchZo == this.ustie) {
//                                System.out.println("[Hľadanie zväčšujúcej polocesty] Hotovo!");
                                return this.vratUpravenuZvacsujucuPolocestu(hodnotyVrcholov);
                            }
                        }
                    }
                }
            }
        }

        System.out.println("[Hľadanie zväčšujúcej polocesty] Hotovo! - nenašla sa.");
        return null;
    }

    public ArrayList<Integer> vratUpravenuZvacsujucuPolocestu(double[] hodnotyVrcholov) {
        ArrayList<Integer> zvacsujucaPolocesta = new ArrayList<>();
        int aktualnyVrchol = this.ustie;
        zvacsujucaPolocesta.add(aktualnyVrchol);
        while (Math.abs(aktualnyVrchol) != this.zdroj) {
            aktualnyVrchol = (int)hodnotyVrcholov[aktualnyVrchol];
            zvacsujucaPolocesta.add(aktualnyVrchol);
        }

        Collections.reverse(zvacsujucaPolocesta);
        return zvacsujucaPolocesta;
    }

    public int vratRezervu(ArrayList<Integer> zvacsujucaPolocesta) {
//        System.out.println("[Hľadanie najmenšej rezervy] Pracuje sa na...");
        ArrayList<Integer> rezervy = new ArrayList<>();
        for (int vrchol = 0; vrchol < zvacsujucaPolocesta.size() - 1; vrchol++) {
            int aktualnyVrchol = zvacsujucaPolocesta.get(vrchol);
            int nasledujuciVrchol = zvacsujucaPolocesta.get(vrchol + 1);
            for (int hrana = 0; hrana < this.hrany.length; hrana++) {
                int vrchZo = this.hrany[hrana][0];
                int vrchDo = this.hrany[hrana][1];

                int priepustnost = this.hrany[hrana][2];
                int tok = this.hrany[hrana][3];

                // Ak je v SMERE, teda ak je následujúci vrchol kladný.
                if (nasledujuciVrchol > 0 && vrchZo == aktualnyVrchol && vrchDo == nasledujuciVrchol) {
                    int rezerva = priepustnost - tok;
                    rezervy.add(rezerva);
                }

                // Ak je v PROTISMERE, teda ak je následujúci vrchol záporný.
                if (nasledujuciVrchol < 0 && vrchZo == nasledujuciVrchol && vrchDo == aktualnyVrchol) {
                    int rezerva = tok;
                    rezervy.add(rezerva);
                }
            }
        }

//        System.out.println("[Hľadanie najmenšej rezervy] Hotovo!");
        return Collections.min(rezervy);
    }

    public void najdiMaximalnyTokVSieti() {
        System.out.println("[Hľadanie maximalného toku v sieti] Pracuje sa na...");
        ArrayList<Integer> zvacsujucaPolocesta;
        while ((zvacsujucaPolocesta = this.vratZvacsujucuPolocestu()) != null) {
            int rezerva = this.vratRezervu(zvacsujucaPolocesta);
            for (int vrchol = 0; vrchol < zvacsujucaPolocesta.size() - 1; vrchol++) {
                int aktualnyVrchol = zvacsujucaPolocesta.get(vrchol);
                int nasledujuciVrchol = zvacsujucaPolocesta.get(vrchol + 1);
                for (int hrana = 0; hrana < this.hrany.length; hrana++) {
                    int vrchZo = this.hrany[hrana][0];
                    int vrchDo = this.hrany[hrana][1];

                    // Ak je v SMERE, teda ak je následujúci vrchol kladný.
                    if (nasledujuciVrchol > 0 && vrchZo == aktualnyVrchol && vrchDo == nasledujuciVrchol) {
                        this.hrany[hrana][3] += rezerva;
                    }

                    // Ak je v PROTISMERE, teda ak je následujúci vrchol záporný.
                    if (nasledujuciVrchol < 0 && vrchZo == nasledujuciVrchol && vrchDo == aktualnyVrchol) {
                        this.hrany[hrana][3] -= rezerva;
                    }
                }
            }
        }

        System.out.println("[Hľadanie maximalného toku v sieti] Hotovo!");
        this.vypisMaximalnyTokVSieti();
    }

    public void vypisMaximalnyTokVSieti() {
        int maximalnyTokZoZdroja = 0;
        int maximalnyTokZUstia = 0;
        for (int hrana = 0; hrana < this.hrany.length; hrana++) {
            if (this.hrany[hrana][0] == this.zdroj) {
                maximalnyTokZoZdroja += this.hrany[hrana][3];
            }

            if (this.hrany[hrana][1] == this.ustie) {
                maximalnyTokZUstia += this.hrany[hrana][3];
            }
        }

        if (maximalnyTokZoZdroja != maximalnyTokZUstia) {
            System.out.format("\nNiečo sa pokazilo! Maximálny tok zo zdroja (%d) sa nerovná maximálnemu toku z ústia (%d).", maximalnyTokZoZdroja, maximalnyTokZUstia);
            return;
        }

        System.out.print("\nToky na hranách siete: ");
        for (int hrana = 0; hrana < this.hrany.length; hrana++) {
            if (hrana != this.hrany.length - 1) {
                System.out.format("[%d, %d, %d, %d], ", this.hrany[hrana][0], this.hrany[hrana][1], this.hrany[hrana][2], this.hrany[hrana][3]);
            } else {
                System.out.format("[%d, %d, %d, %d] ", this.hrany[hrana][0], this.hrany[hrana][1], this.hrany[hrana][2], this.hrany[hrana][3]);
            }
        }

        System.out.format("\nMaximálny tok v sieti je: %d", maximalnyTokZoZdroja);
    }
}
