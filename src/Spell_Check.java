import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Spell_Check {
    public static void main(String args[]) {
        HashTable dict = new HashTable(100);
        File full_dict = new File(args[0]);
        int lines = 0;
        try {
            Scanner sc = new Scanner(full_dict);
            while (sc.hasNextLine()) {
                lines = lines + 1;
                String word = sc.nextLine();
                dict.insert(word);
            }
        }
        catch (IOException e) {
            System.out.println("File not found!");
        }
        File spelt = new File(args[1]);
        long input1 = spelt.length();
        //writes to file if spelt is not in dict
        int lines1 = 0;
        int lines2 = 0;
        try{
            Scanner sc = new Scanner(spelt);
            FileWriter fileWriter = new FileWriter(args[2]);
            while(sc.hasNextLine()){
                lines1 = lines1 + 1;
                String word = sc.nextLine();
                if (!dict.lookup(word)) {
                    lines2++;
                    fileWriter.write(word);
                }
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

}
