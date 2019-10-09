import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SimplePost {

    private Jedis jedis;

    public static String USERS = "users"; //key set for users name
    public static String USERS_LIST = "users_list";
    public static String USERS_HASH = "users_hash";

    public SimplePost() {
        this.jedis = new Jedis(("localhost"));
        System.out.println("Connected to Redis server!");
    }

    // SET
    public void save_user_set(String user_name) {
        jedis.sadd(USERS, user_name);
    }
    public Set<String> get_user_set() {
        return jedis.smembers(USERS);
    }
    public Set<String> get_all_keys_set() {
        return jedis.keys("*");
    }

    // LIST
    public void save_user_list(String user_name) {
        jedis.lpush(USERS_LIST, user_name);
    }
    public List<String> get_user_list() {
        long end = jedis.llen(USERS_LIST);
        return jedis.lrange(USERS_LIST, 0, end);
    }

    // HASHMAP
    public void save_user_hash(HashMap<String, String> user_info) {
        jedis.hset(USERS_HASH, user_info);
    }
    public Map<String, String> get_user_hash() {
        return jedis.hgetAll(USERS_HASH);
    }

    public static void main(String[] args) {
        // Inicializar servidor
        SimplePost post = new SimplePost();
        // Set e List
        String[] users = {"Ana", "Bia", "Carol", "Maria"};
        String[] IDs = {"111", "222", "333", "444"};
        HashMap<String, String> user_info = new HashMap<String, String>();
        for (int i = 0 ; i < users.length ; i++) {
            user_info.put(IDs[i], users[i]);
        }
        System.out.println(user_info);
        post.save_user_hash(user_info);
        for (String user : users) {
            post.save_user_set(user);
            post.save_user_list(user);
        }
        // Set
//        post.get_all_keys_set().stream().forEach(System.out::println);
        System.out.println("All users with SET: ");
        post.get_user_set().stream().forEach(System.out::println);
        // List
//        post.get_all_keys_set().stream().forEach(System.out::println);
        System.out.println("All users with LIST: ");
        post.get_user_list().stream().forEach(System.out::println);
        // Hash
        System.out.println("All users with HASH: ");
//        post.get_all_keys_set().stream().forEach(System.out::println);
        System.out.println(post.get_user_hash());
    }

}
