//package sk.uniza.fri;
//
//import java.util.ArrayList;
//import java.util.Scanner;
//
///**
// * Created by IntelliJ IDEA.
// * User: Fíla
// * Date: 6. 3. 2022
// * Time: 12:07
// */
//public class Main {
//
//    // Vrcholy: 1, 2, 3, 4, 5, 6, 7
//    // Hrany grafu: {1, 2}, {1, 5}, {2, 3}, {2, 5}, {3, 4}, {3, 5}, {4, 5}, {6, 7}
//    // Hrany digrafu: (1, 2), (2, 3), (3, 6), (1, 4), (1, 5), (4, 2), (6, 2), (5, 6), (3, 5), (4, 5)
//
//    private static String[] vrcholy;
//    private static String iVrcholy;
//    private static String iHrany;
//
//    public static void main(String[] args) {
//        Scanner sc = new Scanner(System.in);
//
//        //System.out.print("Zadajte vrcholy vo formáte uvedenom v zátvorkách (1, b, a, G): ");
//        System.out.print("Zadajte vrcholy: ");
//        iVrcholy = sc.nextLine();
//
//        //System.out.print("Zadajte hrany. Ak sa jedná o graf, zadajte ich vo formáte {1, 2}, {1, 5}, {a, G}. Ak sa jedná o digraf, zadajte ich vo formáte (1, 2), (1, 5), (a, G): ");
//        System.out.print("Zadajte hrany: ");
//        iHrany = sc.nextLine();
//
//        vrcholy = iVrcholy.split(", ");
//
//        // Ak je graf
//        if (iHrany.charAt(0) == '{') {
//            System.out.println("Vyberte si jeden z uvedených vrcholov " + iVrcholy);
//            String vrchol = sc.next();
//
//            System.out.format("Stupeň vrcholu %s je %d %n", vrchol, stupenVrcholu(vrchol));
//            System.out.format("Najvyšší stupeň vrcholu má vrchol %s %n", vrcholSNajvacsimStupnom());
//        } else {
//            System.out.print("Zoznam so vstupnym 0: ");
//            for (Character stupen : diagraf()) {
//                System.out.print(stupen + ", ");
//            }
//        }
//
//
//        String hranyGrafu = "{1, 2}, {1, 5}, {2, 3}, {2, 5}, {3, 4}, {3, 5}, {4, 5}, {6, 7}";
//        String hranyDigrafu = "(1, 2), (2, 3), (3, 6), (1, 4), (1, 5), (4, 2), (6, 2), (5, 6), (3, 5), (4, 5)";
//    }
//
//    private static String vrcholSNajvacsimStupnom() {
//        String vrcholSNajvyssimStupnom = "";
//        int pocetStupnovVrchola = 0;
//        for (int i = 0; i < vrcholy.length; i++) {
//            String vrchol = vrcholy[i];
//            int pocet = -1;
//
//            for (int j = 0; j < iHrany.length(); j++) {
//                if (vrchol.charAt(0) == iHrany.charAt(j)) {
//                    pocet++;
//                }
//            }
//
//            if (pocet > pocetStupnovVrchola) {
//                vrcholSNajvyssimStupnom = vrchol;
//                pocetStupnovVrchola = pocet;
//            }
//        }
//        return vrcholSNajvyssimStupnom;
//    }
//
//    private static int stupenVrcholu(String vrchol) {
//        int pocet = 0;
//        for (int i = 0; i < iHrany.length(); i++) {
//            if (vrchol.charAt(0) == iHrany.charAt(i)) {
//                pocet++;
//            }
//        }
//
//        return pocet;
//    }
//
//    private static ArrayList<Character> diagraf() {
//        ArrayList<Character> stupne = new ArrayList<Character>();
//        ArrayList<Character> vstupne = new ArrayList<Character>();
//
//        for (int i = 0; i < iHrany.length(); i++) {
//            if (iHrany.charAt(i) == ')') {
//                vstupne.add(iHrany.charAt(i - 1));
//            }
//        }
//
//        for (int i = 0; i < vrcholy.length; i++) {
//            boolean jeTam = false;
//            for (int j = 0; j < vstupne.size(); j++) {
//                if (vstupne.get(j) == vrcholy[i].charAt(0)) {
//                    jeTam = true;
//                }
//            }
//
//            if (!jeTam) {
//                stupne.add(vrcholy[i].charAt(0));
//            }
//        }
//
//        return stupne;
//    }
//}
