package sk.uniza.fri;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * 20. 4. 2022 - 18:16
 *
 * @author Fíla
 */
public class Generator {

    private final int maxPocetVrcholov = 23;
    private final int maxOhodnotenie = 7;

    public Generator(String nazovSuboru) {
        Random random = new Random();

        String hrany = "";
        int pocetHran = this.maxPocetVrcholov * ((this.maxPocetVrcholov - 1) / 2);
//        int pocetHran = this.maxPocetVrcholov;
        for (int i = 0; i < pocetHran; i++) {
            int vrcholZo = random.nextInt(1, this.maxPocetVrcholov);
            int vrcholDo = random.nextInt(1, this.maxPocetVrcholov);
            int ohodnotenie = random.nextInt(1, this.maxOhodnotenie);

            hrany += String.format("%d %d %d\n", vrcholZo, vrcholDo, ohodnotenie);
        }

        try {
            FileWriter fileWriter = new FileWriter(nazovSuboru);
            fileWriter.write(hrany);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
