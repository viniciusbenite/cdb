import redis.clients.jedis.Jedis;
import java.util.Set;

public class Forum {

    private Jedis jedis;

    public Forum() {
        this.jedis = new Jedis("localhost");
        System.out.println("Hello");
        System.out.println(jedis.info());
    }

    public static void main(String[] args) {
        new Forum();
    }
}
