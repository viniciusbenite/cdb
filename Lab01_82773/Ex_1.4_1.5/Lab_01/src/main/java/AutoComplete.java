import redis.clients.jedis.Jedis;

import java.io.File;
import java.util.*;

public class AutoComplete {

    private Jedis jedis;
    private String key = "";
    private String word;

    private void connect() { this.jedis = new Jedis("localhost"); System.out.println("DataBase running!"); }
    private void disconnect(){ jedis.disconnect();}

    private void insert_file(String file_name) {
        // Open a file using Scanner class and inset into Redis
        try {
            File base_text = new File(file_name);
            Scanner sc = new Scanner(base_text);
            while (sc.hasNextLine()) {
                jedis.zadd(key, 0, sc.nextLine()); // Sorted SET
            }
            System.out.println("File inserted in DB successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void search_for_word(String word) {
        try {
            Set<String>  search = jedis.zrangeByLex(key, "["+word, "+");
            if (search == null) {
                System.exit(0);
            }
            for (String entry : search) {
                if (!entry.startsWith(word)) { break; }
                System.out.println(entry);
            }
        } catch (Exception e) {e.printStackTrace();}
        finally { disconnect(); }
    }

    public static void main(String[] args) {
        AutoComplete ac = new AutoComplete();
        try {
            ac.connect();
            // ATENCAO ao path do ficheiro!
            ac.insert_file("/home/vinicius/Downloads/CDB/Lab_01/female-names.txt");
        } catch (Exception e) {e.printStackTrace();}
        Scanner kb = new Scanner(System.in);
        System.out.print("Search for ('Enter' for quit): ");
        // LOOP ate apertar enter
        while ((ac.word = kb.nextLine()).length() > 0) {
            ac.search_for_word(ac.word);
            System.out.print("Search for ('Enter' for quit): ");
        }

        System.out.println("Good bye!");
    }
}
