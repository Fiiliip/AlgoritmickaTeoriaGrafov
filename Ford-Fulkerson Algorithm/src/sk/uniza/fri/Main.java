package sk.uniza.fri;

import java.io.File;
import java.util.Scanner;

/**
 * Created by IntelliJ IDEA.
 * User: Fíla
 * Date: 19. 5. 2022
 * Time: 9:01
 */
public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Zadajte cestu k súboru (.hrn): ");
        String cestaKSuboru = sc.next();

        if (!new File(cestaKSuboru).exists()) {
            System.out.print("Súbor neexistuje!");
            return;
        }

        FordFulkerson fordFulkerson = new FordFulkerson(cestaKSuboru);
    }
}
