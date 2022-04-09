import java.io.File;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // Odkomentovať, pre testovacie účely.
//        LabelSet labelSet = new LabelSet("./data/Florida.hrn", 1, 1070376);

        Scanner sc = new Scanner(System.in);
        System.out.print("Zadajte cestu k súboru (.hrn): ");
        String cestaKSuboru = sc.next();

        if (!(new File(cestaKSuboru).exists())) {
            System.out.print("Súbor neexistuje!");
            return;
        }

        System.out.print("Zadajte začiatočný vrchol: ");
        int zaciatocnyVrchol = sc.nextInt();

        System.out.print("Zadajte koncový vrchol: ");
        int koncovyVrchol = sc.nextInt();

        System.out.println();

        LabelSet labelSet = new LabelSet(cestaKSuboru, zaciatocnyVrchol, koncovyVrchol);
    }
}
