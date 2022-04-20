package sk.uniza.fri;

import java.io.File;
import java.util.Scanner;

/**
 * Created by IntelliJ IDEA.
 * User: Fíla
 * Date: 20. 4. 2022
 * Time: 10:31
 */
public class Main {

    public static void main(String[] args) {
//        KruskalovAlgoritmusII kruskalovAlgoritmusII = new KruskalovAlgoritmusII("./data/pr3.hrn");

        Scanner sc = new Scanner(System.in);
        System.out.print("Zadajte cestu k súboru (.hrn): ");
        String cestaKSuboru = sc.next();

        if (!new File(cestaKSuboru).exists()) {
            System.out.print("Súbor neexistuje!");
            return;
        }

        KruskalovAlgoritmusII kruskalovAlgoritmusII = new KruskalovAlgoritmusII(cestaKSuboru);
    }
}
