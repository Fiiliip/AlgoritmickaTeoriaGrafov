import java.io.File;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        LabelSet labelSet = new LabelSet("./data/SlovRep.hrn", 1, 4);

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

//        LabelSet labelSet = new LabelSet(cestaKSuboru, zaciatocnyVrchol, koncovyVrchol);

//        ./data/TEST_mini.hrn
//        LabelSet labelSet = new LabelSet("./data/cvicenie.hrn");
//        LabelSet labelSet = new LabelSet("./data/SlovRep.hrn");
//        LabelSet labelSet = new LabelSet("./data/Florida.hrn");
//        LabelSet labelSet = new LabelSet("./data/pr1.hrn");
    }
}
