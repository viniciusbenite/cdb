import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;

import java.util.Scanner;
import java.util.Set;

public class Publisher {

    private static final Logger logger = LoggerFactory.getLogger(Publisher.class);

    private Jedis pub;
    private Jedis jedis;
    private String channel;
    private String msgs;

    public Publisher(Jedis pub, String channel) {
        this.channel = channel;
        this.pub = pub;
    }

    public void send_msg() {
        System.out.print("Enter your msg: ");
        Scanner kb = new Scanner(System.in);
        while (1 == 1) {
            System.out.print("Enter your msg: ");
            String msg = kb.nextLine();
            if (msg.length() == 0) {
                logger.info("Exiting!");
                break;
            } else {
                pub.publish("Channel 1", msg);
                jedis.sadd(msgs, msg);
            }
        }
    }
}
