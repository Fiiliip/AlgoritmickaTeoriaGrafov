import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class LabelSet {



    public LabelSet(String cestaKSuboru) {
        this.nacitajDataZoSuboru(cestaKSuboru);
    }

    public void nacitajDataZoSuboru(String cestaKSuboru) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(cestaKSuboru));
            while(bufferedReader.readLine() != null) {
                String[] rozdelene = bufferedReader.readLine().split(" ");
                System.out.println(rozdelene[0] + " " + rozdelene[1]);
            }
            bufferedReader.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public int vratPocetRiadkovVSubore(String cestaKSuboru) {
        int pocetRiadkov = 0;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(cestaKSuboru));
            while (bufferedReader.readLine() != null) pocetRiadkov++;
        } catch (Exception e) {
            System.out.println(e);
        }

        return pocetRiadkov;
    }
}
