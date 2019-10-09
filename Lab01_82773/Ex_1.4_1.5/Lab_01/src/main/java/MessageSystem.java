import org.apache.log4j.BasicConfigurator;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageSystem {

    private Jedis jedis;
    private String my_set = "";
    private static final Logger logger = LoggerFactory.getLogger(MessageSystem.class);

    // Add user by name into a set
    private void add_users(String usr_name) {
        jedis.zadd(my_set, 0, usr_name);
    }

    // Main class
    public static void main(String[] args) {
        BasicConfigurator.configure(); // logger configurations

        final JedisPoolConfig pool_config = new JedisPoolConfig();
        final JedisPool jedis_pool = new JedisPool(pool_config, "localhost", 6379, 0);
        Jedis sub_jedis = jedis_pool.getResource();
        Subscriber sub = new Subscriber();

        new Thread(() -> {
            try {
                logger.info("Subscribing to channel 1");
                sub_jedis.subscribe(sub, "Channel 1");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        Jedis pub = jedis_pool.getResource();
        Publisher publisher = new Publisher(pub, "Channel 1");
        publisher.send_msg();
        sub.unsubscribe();
        jedis_pool.close();
        jedis_pool.close();
    }
}