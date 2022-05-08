package sk.uniza.fri;

import java.io.File;
import java.util.Scanner;

/**
 * Created by IntelliJ IDEA.
 * User: Fíla
 * Date: 7. 5. 2022
 * Time: 15:25
 */
public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Zadajte cestu k súboru bez koncovky typu súboru: ");
        String cestaKSuboru = sc.next();

        if (!new File(cestaKSuboru + ".hrn").exists() || !new File(cestaKSuboru + ".tim").exists()) {
            System.out.print("Súbor neexistuje!");
            return;
        }

        CPM cpm = new CPM(cestaKSuboru);
    }
}
