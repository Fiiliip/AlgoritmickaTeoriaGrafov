package sk.uniza.fri;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * 7. 5. 2022 - 15:25
 *
 * @author Fíla
 */
public class CPM {

    private int pocetVrcholov;
    private int[][] hrany;
    private int[] dobyTrvania;

    private int[] lokacieVrcholov;
    private int[] monotonneOcislovanie;

    private int dobaTrvaniaProjektu;

    private int[] najskorMozneZaciatky;
    private int[] najneskorNutneKonce;

    private ArrayList<Integer> kritickaCesta;
    private int[] rezervy;

    public CPM(String cestaKSuboru) {
        Instant start = Instant.now();

        this.pocetVrcholov = 0;
        this.hrany = this.nacitajHranyZoSuboru(cestaKSuboru + ".hrn");
        this.dobyTrvania = this.nacitajDobyTrvaniaZoSuboru(cestaKSuboru + ".tim");

        this.zoradHranyPodlaVrcholaOd(this.hrany);

        this.lokacieVrcholov = this.vratLokacieVrcholov();
        this.monotonneOcislovanie = this.vratMonotonneOcislovanie();

        this.dobaTrvaniaProjektu = 0;

        this.najskorMozneZaciatky = this.vratNajskorMozneZaciatky();
        this.najneskorNutneKonce = this.vratNajneskorNutneKonce();

        this.kritickaCesta = new ArrayList<Integer>();
        this.rezervy = this.vratRezervy();

        this.vypis();

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
            bufferedReader.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("[Zisťovanie počtu riadkov v súbore] Hotovo! (" + pocetRiadkov + ")");
        return pocetRiadkov;
    }

    public int[][] nacitajHranyZoSuboru(String cestaKSuboru) {
        System.out.println("[Načítavanie hrán zo súboru] Pracuje sa na...");
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

                // Hľadanie počtu vrcholov.
                if (this.pocetVrcholov < data[index][0]) {
                    this.pocetVrcholov = data[index][0];
                }
                if (this.pocetVrcholov < data[index][1]) {
                    this.pocetVrcholov = data[index][1];
                }

                index++;
            }
            bufferedReader.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("[Načítavanie hrán zo súboru] Hotovo!");
        return data;
    }

    public int[] nacitajDobyTrvaniaZoSuboru(String cestaKSuboru) {
        System.out.println("[Načítavanie doby trvania zo súboru] Pracuje sa na...");
        int[] data = new int[this.vratPocetRiadkovVSubore(cestaKSuboru) + 1];
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(cestaKSuboru));
            int index = 1;
            String riadok;
            while ((riadok = bufferedReader.readLine()) != null) {
                data[index] = Integer.parseInt(riadok);
                index++;
            }
            bufferedReader.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("[Načítavanie doby trvania zo súboru] Hotovo!");
        return data;
    }

    private void zoradHranyPodlaVrcholaOd(int[][] data) {
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

    public int[] vratLokacieVrcholov() {
        System.out.println("[Vytváranie poľa so začínajúcimi lokáciami vrcholov] Pracuje sa na...");
        int[] data = new int[this.pocetVrcholov + 1];
        for (int hrana = 0, lokacia = 0; hrana < this.hrany.length; hrana++) {
            if (this.hrany[hrana][0] != lokacia) {
                lokacia = this.hrany[hrana][0];
                data[lokacia] = hrana;
            }
        }
        System.out.println("[Vytváranie poľa so začínajúcimi lokáciami vrcholov] Hotovo!");
        return data;
    }

    public int[] vratMonotonneOcislovanie() {
        System.out.println("[Monotónne očíslovanie] Pracuje sa na...");

        ArrayList<Integer> data = new ArrayList<Integer>();

        // Načítame si do poľa vstupné stupne vrcholov. Index značí vrchol a hodnota značí vstupný stupeň daného vrchola.
        int[] vstupneStupneVrcholov = new int[this.pocetVrcholov + 1];
        for (int hrana = 0; hrana < this.hrany.length; hrana++) {
            vstupneStupneVrcholov[this.hrany[hrana][1]]++;
        }

        ArrayList<Integer> vrcholySNulovymStupnom = new ArrayList<Integer>();
        for (int i = 1; i < vstupneStupneVrcholov.length; i++) {
            if (vstupneStupneVrcholov[i] == 0) {
                vrcholySNulovymStupnom.add(i);
            }
        }

        while (!vrcholySNulovymStupnom.isEmpty()) {
            int vrcholSNulovymStupnom = vrcholySNulovymStupnom.remove(0);

            data.add(vrcholSNulovymStupnom);
            vstupneStupneVrcholov[vrcholSNulovymStupnom] = -1;

            for (int hrana = 0; hrana < this.hrany.length; hrana++) {
                if (this.hrany[hrana][0] == vrcholSNulovymStupnom) {
                    vstupneStupneVrcholov[this.hrany[hrana][1]]--;
                    if (vstupneStupneVrcholov[this.hrany[hrana][1]] == 0) {
                        vrcholySNulovymStupnom.add(this.hrany[hrana][1]);
                    }
                }
            }
        }

        int[] monOcislovanie = new int[data.size() + 1];
        for (int i = 1; i < monOcislovanie.length; i++) {
            monOcislovanie[i] = data.get(i - 1);
        }

        System.out.println("[Monotónne očíslovanie] Hotovo!");
        return monOcislovanie;
    }

    public int[] vratNajskorMozneZaciatky() {
        System.out.println("[Hľadanie najskôr možných začiatkov] Pracuje sa na...");

        int[] najskorMoznyZaciatok = new int[this.pocetVrcholov + 1];

        int vrcholSNajvacsouDobouTrvania = 0;

        for (int i = 0; i < najskorMoznyZaciatok.length; i++) {
            najskorMoznyZaciatok[i] = 0;
        }

        for (int i = 0; i < this.monotonneOcislovanie.length; i++) {
            int vrchol = this.monotonneOcislovanie[i];
            for (int hrana = 0; hrana < this.hrany.length; hrana++) {
                if (this.hrany[hrana][0] == vrchol) {
                    int novyNajskorMoznyZaciatok = najskorMoznyZaciatok[vrchol] + this.dobyTrvania[vrchol];
                    if (novyNajskorMoznyZaciatok > najskorMoznyZaciatok[this.hrany[hrana][1]]) {
                        najskorMoznyZaciatok[this.hrany[hrana][1]] = novyNajskorMoznyZaciatok;
                        if (najskorMoznyZaciatok[vrcholSNajvacsouDobouTrvania] < najskorMoznyZaciatok[this.hrany[hrana][1]]) {
                            vrcholSNajvacsouDobouTrvania = this.hrany[hrana][1];
                        }
                    }
                }
            }
        }

        this.dobaTrvaniaProjektu = najskorMoznyZaciatok[vrcholSNajvacsouDobouTrvania] + this.dobyTrvania[vrcholSNajvacsouDobouTrvania];

        System.out.println("[Hľadanie najskôr možných začiatkov] Hotovo!");
        return najskorMoznyZaciatok;
    }

    public int[] vratNajneskorNutneKonce() {
        System.out.println("[Hľadanie najneskôr nutných koncov] Pracuje sa na...");

        int[] najneskorNutnyKoniec = new int[this.pocetVrcholov + 1];

        for (int i = 0; i < najneskorNutnyKoniec.length; i++) {
            najneskorNutnyKoniec[i] = this.dobaTrvaniaProjektu;
        }

        for (int i = this.monotonneOcislovanie.length - 1; i >= 0; i--) {
            int vrchol = this.monotonneOcislovanie[i];
            for (int hrana = 0; hrana < this.hrany.length; hrana++) {
                if (this.hrany[hrana][1] == vrchol) {
                    int novyNajneskorNutnyKoniec = najneskorNutnyKoniec[vrchol] - this.dobyTrvania[vrchol];
                    if (novyNajneskorNutnyKoniec < najneskorNutnyKoniec[this.hrany[hrana][0]]) {
                        najneskorNutnyKoniec[this.hrany[hrana][0]] = novyNajneskorNutnyKoniec;
                    }
                }
            }
        }

        System.out.println("[Hľadanie najneskôr nutných koncov] Hotovo!");

        return najneskorNutnyKoniec;
    }

    public int[] vratRezervy() {
        System.out.println("[Výpočet rezervy] Pracuje sa na...");
        int[] rezerva = new int[this.pocetVrcholov + 1];
        for (int vrchol = 1; vrchol < this.monotonneOcislovanie.length; vrchol++) {
            rezerva[this.monotonneOcislovanie[vrchol]] = this.najneskorNutneKonce[this.monotonneOcislovanie[vrchol]] - (this.dobyTrvania[this.monotonneOcislovanie[vrchol]] + this.najskorMozneZaciatky[this.monotonneOcislovanie[vrchol]]);
            if (rezerva[this.monotonneOcislovanie[vrchol]] == 0) {
                this.kritickaCesta.add(this.monotonneOcislovanie[vrchol]);
            }
        }

        System.out.println("[Výpočet rezervy] Hotovo!");

        return rezerva;
    }

    public void vypisCislo(int cislo) {
        if (cislo < 10) {
            System.out.format("|   %d    ", cislo);
        } else if (cislo < 100) {
            System.out.format("|   %d   ", cislo);
        } else if (cislo < 1000) {
            System.out.format("|   %d  ", cislo);
        }
    }

    public void vypis() {
        System.out.format("\nDoba trvania projektu: %d \n", this.dobaTrvaniaProjektu);
        System.out.print("Kritická cesta: [");
        for (int vrchol = 0; vrchol < this.kritickaCesta.size(); vrchol++) {
            if (vrchol == this.kritickaCesta.size() - 1) {
                System.out.print(this.kritickaCesta.get(vrchol) + "]");
            } else {
                System.out.print(this.kritickaCesta.get(vrchol) + ", ");
            }
        }

        System.out.println("\n\n|   v    |  t(v)  |  z(v)  |  k(v)  |  r(v)  ");
        for (int vrchol = 1; vrchol < this.monotonneOcislovanie.length; vrchol++) {
//            System.out.format("|  %d  |   %d   |   %d   |    %d    |  %d  |\n", this.monotonneOcislovanie[vrchol], this.dobyTrvania[this.monotonneOcislovanie[vrchol]], this.najskorMozneZaciatky[this.monotonneOcislovanie[vrchol]], this.najneskorNutneKonce[this.monotonneOcislovanie[vrchol]], this.rezervy[this.monotonneOcislovanie[vrchol]]);

            this.vypisCislo(this.monotonneOcislovanie[vrchol]);
            this.vypisCislo(this.dobyTrvania[this.monotonneOcislovanie[vrchol]]);
            this.vypisCislo(this.najskorMozneZaciatky[this.monotonneOcislovanie[vrchol]]);
            this.vypisCislo(this.najneskorNutneKonce[this.monotonneOcislovanie[vrchol]]);
            this.vypisCislo(this.rezervy[this.monotonneOcislovanie[vrchol]]);

            System.out.println();
        }
    }
}
