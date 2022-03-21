package sk.uniza.fri;

import java.util.Scanner;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Fíla
 * Date: 6. 3. 2022
 * Time: 12:07
 */
public class Main {

    // Vrcholy: 1, 2, 3, 4, 5, 6, 7
    // Hrany grafu: {1,2}, {1,5}, {2,3}, {2,5}, {3,4}, {3,5}, {4,5}, {6,7}
    // Hrany digrafu: (1,2), (2,3), (3,6), (1,4), (1,5), (4,2), (6,2), (5,6), (3,5), (4,5)

    private static String[] vrcholy;
    private static String[] hrany;

    private static boolean jeGraf; // Ak nie je graf, tak je digraf.

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Návod ku \"správnemu\" inputu.");
        System.out.println("Vrcholy zadávajte vo formáte 1, b, a, G.");
        System.out.println("Hrany zadávajte podľa toho, či sa jedna o graf, alebo digraf.");
        System.out.println("    - Ak sa jedná o graf, zadajte ich vo formáte {1, 2}, {1, 5}, {a, G}.");
        System.out.println("    - Ak sa jedná o digraf, zadajte ich vo formáte (1, 2), (1, 5), (a, G).\n");

        System.out.print("> Zadajte vrcholy: ");
        String input = sc.nextLine();
        vrcholy = input.split(", ");

        System.out.print("> Zadajte hrany: ");
        input = sc.nextLine();
        if (input.charAt(0) == '{') {
            jeGraf = true;
            // Rozdelenie prvkov do pola vo formáte {x,y}
            input = input.replaceAll("}, ", "};"); //.replaceAll(", ", ",");
        } else {
            jeGraf = false;
            // Rozdelenie prvkov do pola vo formáte (x,y)
            input = input.replaceAll("\\), ", ");"); //.replaceAll(", ", ",");
        }
        hrany = input.split(";");

        //vypisMaticuIncidencie(vratMaticuIncidencie());
        vypisMaticuSusednosti(vratMaticuSusednosti());

//        if (jeGraf) {
//            System.out.format("> Zadaj vrchol (%s), ktorého chceš vrátiť stupeň vrcholu: ", vratVrcholyVTexte());
//            Character vrchol = sc.nextLine().charAt(0);
//            System.out.format("\nStupeň vrcholu \"%s\" je %d. %n", vrchol, vratStupenVrcholu(vrchol));
//            System.out.format("Najvyšší stupeň (%d) má vrchol \"%s\". %n", vratStupenVrcholu(vratVrcholSNajvyssimStupnom()), vratVrcholSNajvyssimStupnom());
//        } else {
//            System.out.print("> Zadaj vstupný stupeň, ktorého zoznam vrcholov to má vrátiť: ");
//            int vstupnyStupen = sc.nextInt();
//            System.out.format("\nZoznam vrcholov digrafu so vstupným stupňom \"%d\": ", vstupnyStupen);
//            for (Character vrchol : vratZoznamVrcholovSoVstupnymStupnom(vstupnyStupen)) {
//                System.out.print(vrchol + "  ");
//            }
//        }

    }

    private static int vratStupenVrcholu(Character vrchol) {
        int pocet = 0;
        for (int i = 0; i < hrany.length; i++) {
            for (int znak = 0; znak < hrany[i].length(); znak++) {
                if (hrany[i].charAt(znak) == vrchol) {
                    pocet++;
                }
            }
        }

        return pocet;
    }

    private static Character vratVrcholSNajvyssimStupnom() {
        char vrcholSNajvyssimStupnom = vrcholy[0].charAt(0);
        int pocetStupnov = vratStupenVrcholu(vrcholSNajvyssimStupnom);
        for (int vrchol = 1; vrchol < vrcholy.length; vrchol++) {
            if (vratStupenVrcholu(vrcholy[vrchol].charAt(0)) > pocetStupnov) {
                vrcholSNajvyssimStupnom = vrcholy[vrchol].charAt(0);
            }
        }

        return vrcholSNajvyssimStupnom;
    }

    private static ArrayList<Character> vratZoznamVrcholovSoVstupnymStupnom(int vstupnyStupen) {
        ArrayList<Character> vrcholySoZiadnymVstupnymStupnom = new ArrayList<Character>();
        for (int vrchol = 0; vrchol < vrcholy.length; vrchol++) {
            int pocetVstupnychStupnov = 0;
            for (int hrana = 0; hrana < hrany.length; hrana++) {
                if (hrany[hrana].charAt(hrany[hrana].length() - 2) == vrcholy[vrchol].charAt(0)) {
                    pocetVstupnychStupnov++;
                }
            }

            if (pocetVstupnychStupnov == vstupnyStupen) {
                vrcholySoZiadnymVstupnymStupnom.add(vrcholy[vrchol].charAt(0));
            }
        }

        return vrcholySoZiadnymVstupnymStupnom;
    }

    private static String vratVrcholyVTexte() {
        String text = "";
        for (int vrchol = 0; vrchol < vrcholy.length; vrchol++) {
            if (vrchol == vrcholy.length - 1) {
                text += vrcholy[vrchol];
            } else {
                text += vrcholy[vrchol] + " ";
            }
        }

        return text;
    }

    public static int[][] vratMaticuSusednosti() {
//        Daný je graf G = (V, H, c)
//        V = {1, 2, 3, 4, 5, 6, 7, 8, 9}
//        H = {{1,2}, {2,3}, {2,5}, {3,6}, {4,5}, {4,8}, {5,6}, {5,8}, {6,9}, {7,8}, {7,9}}
//        H = {(1,2), (2,3), (2,5), (3,6), (4,5), (4,8), (5,6), (5,8), (6,9), (7,8), (7,9)}

//        h    | {1,2} | {2,3} | {2,5} | {3,6} | {4,5} | {4,8} | {5,6} | {5,8} | {6,9} | {7,8} | {7,9} |
//        c(h) |   5   |   4   |   4   |   5   |   2   |   1   |   7   |   3   |   6   |   1   |   2   |

//        Naprogramujte metódu
//        1. ktorá vráti maticu susednosti grafu G,
//        2. ktorá vráti maticu incidencie grafu G,
//        3. ktorá vráti maticu ohodnotení hrán grafu G.
//        4. Základný algoritmus, ktorá v grafe G nájde a vráti najkratšiu cestu z vrcholu u do vrcholu v a jej dĺžku.

//        Matica susednosti / priľahlosti pri grafe.
//            |  1  |  2  |  3  |  4  |  5  |  6  |  7  |  8  |  9  |
//        | 1 |     |  1  |     |     |     |     |     |     |     |
//        | 2 |  1  |     |  1  |     |  1  |     |     |     |     |
//        | 3 |     |  1  |     |     |     |  1  |     |     |     |
//        | 4 |     |     |     |     |  1  |     |     |  1  |     |
//        | 5 |     |  1  |     |  1  |     |  1  |     |  1  |     |
//        | 6 |     |     |  1  |     |  1  |     |     |     |  1  |
//        | 7 |     |     |     |     |     |     |     |  1  |  1  |
//        | 8 |     |     |     |  1  |  1  |     |  1  |     |     |
//        | 9 |     |     |     |     |     |  1  |  1  |     |     |

//        Matica incidencie pri grafe.
//            | 1,2 | 2,3 | 2,5 | 3,6 | 4,5 | 4,8 | 5,6 | 5,8 | 6,9 | 7,8 | 7,9 |
//        | 1 |  1  |     |     |     |     |     |     |     |     |     |     |
//        | 2 |  1  |  1  |  1  |     |     |     |     |     |     |     |     |
//        | 3 |     |  1  |     |  1  |     |     |     |     |     |     |     |
//        | 4 |     |     |     |     |  1  |  1  |     |     |     |     |     |
//        | 5 |     |     |  1  |     |  1  |     |  1  |  1  |     |     |     |
//        | 6 |     |     |     |  1  |     |     |  1  |     |  1  |     |     |
//        | 7 |     |     |     |     |     |     |     |     |     |  1  |  1  |
//        | 8 |     |     |     |     |     |  1  |     |  1  |     |  1  |     |
//        | 9 |     |     |     |     |     |     |     |     |  1  |     |  1  |

//        Matica incidencie pri digrafe.
//            | 1,2 | 2,3 | 2,5 | 3,6 | 4,5 | 4,8 | 5,6 | 5,8 | 6,9 | 7,8 | 7,9 |
//        | 1 |  1  |     |     |     |     |     |     |     |     |     |     |
//        | 2 | -1  |  1  |  1  |     |     |     |     |     |     |     |     |
//        | 3 |     | -1  |     |  1  |     |     |     |     |     |     |     |
//        | 4 |     |     |     |     |  1  |  1  |     |     |     |     |     |
//        | 5 |     |     | -1  |     | -1  |     |  1  |  1  |     |     |     |
//        | 6 |     |     |     | -1  |     |     | -1  |     |  1  |     |     |
//        | 7 |     |     |     |     |     |     |     |     |     |  1  |  1  |
//        | 8 |     |     |     |     |     | -1  |     | -1  |     | -1  |     |
//        | 9 |     |     |     |     |     |     |     |     | -1  |     | -1  |

        int[][] maticaSusednosti = new int[vrcholy.length][vrcholy.length];
        ArrayList<String> susedneVrcholy = new ArrayList<String>();
        ArrayList<Integer> indexySusedov = new ArrayList<Integer>();
        for (int vrchol = 0; vrchol < vrcholy.length; vrchol++) {
            indexySusedov.add(susedneVrcholy.size());
            for (int hrana = 0; hrana < hrany.length; hrana++) {
                for (int znak = 0; znak < hrany[hrana].length(); znak++) {
                    if (vrcholy[vrchol].charAt(0) == hrany[hrana].charAt(znak)) {
                        for (int susednyVrchol = 0; susednyVrchol < vrcholy.length; susednyVrchol++) {
                            for (int susednyZnak = 0; susednyZnak < hrany[hrana].length(); susednyZnak++) {
                                if (!vrcholy[susednyVrchol].equals(vrcholy[vrchol]) && vrcholy[susednyVrchol].charAt(0) == hrany[hrana].charAt(susednyZnak)) {
                                    susedneVrcholy.add(vrcholy[susednyVrchol]);
                                }
                            }
                        }
                    }
                }
            }
        }

        for (int i = 0; i < maticaSusednosti.length; i++) {
            for (int j = 0; j < maticaSusednosti[i].length; j++) {
                maticaSusednosti[i][j] = 0;
            }
        }

        for (int index = 0; index < indexySusedov.size(); index++) {
            for (int vrchol = indexySusedov.get(index); (index + 1 < indexySusedov.size()) ? vrchol < indexySusedov.get(index + 1) : vrchol < susedneVrcholy.size(); vrchol++) {
                maticaSusednosti[index][Integer.parseInt(susedneVrcholy.get(vrchol)) - 1] = 1;
            }
        }

        return maticaSusednosti;
    }

    public static void vypisMaticuSusednosti(int[][] maticaSusednosti) {
        System.out.print("    |");
        for (int i = 0; i < maticaSusednosti.length; i++) {
            System.out.format(" %d |", i + 1);
        }
        System.out.println();
        for (int i = 0; i < maticaSusednosti.length; i++) {
            System.out.format("| %d |", i + 1);
            for (int j = 0; j < maticaSusednosti[i].length; j++) {
                System.out.format(" %d |", maticaSusednosti[i][j]);
            }
            System.out.println();
        }
    }

    public static int[][] vratMaticuIncidencie() {
        int[][] maticaIncidencie = new int[vrcholy.length][hrany.length];
        for (int vrchol = 0; vrchol < vrcholy.length; vrchol++) {
            for (int hrana = 0; hrana < hrany.length; hrana++) {
                maticaIncidencie[vrchol][hrana] = 0;
                for (int znak = 0; znak < hrany[hrana].length(); znak++) {
                    if (jeGraf) {
                        if (hrany[hrana].charAt(znak) == vrcholy[vrchol].charAt(0)) {
                            maticaIncidencie[vrchol][hrana] = 1;
                        }
                    } else {
                        if (hrany[hrana].charAt(1) == vrcholy[vrchol].charAt(0)) {
                            maticaIncidencie[vrchol][hrana] = 1;
                        } else if (hrany[hrana].charAt(3) == vrcholy[vrchol].charAt(0)) {
                            maticaIncidencie[vrchol][hrana] = -1;
                        }
                    }
                }
            }
        }

        return maticaIncidencie;
    }

    public static void vypisMaticuIncidencie(int[][] maticaIncidencie) {
        System.out.print("    |");
        for (int hrana = 0; hrana < hrany.length; hrana++) {
            System.out.format(" %s |", hrany[hrana]);
        }
        System.out.println();
        for (int vrchol = 0; vrchol < maticaIncidencie.length; vrchol++) {
            System.out.format("| %s |", vrcholy[vrchol]);
            for (int hrana = 0; hrana < maticaIncidencie[vrchol].length; hrana++) {
                if (maticaIncidencie[vrchol][hrana] >= 0) {
                    System.out.format("   %d   |", maticaIncidencie[vrchol][hrana]);
                } else {
                    System.out.format("  %d   |", maticaIncidencie[vrchol][hrana]);
                }
            }
            System.out.println();
        }
    }
}
