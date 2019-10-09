import redis.clients.jedis.JedisPubSub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Subscriber extends JedisPubSub {

    private static final Logger logger = LoggerFactory.getLogger(Subscriber.class);

    @Override
    public void onMessage(String channel, String message) {
        super.onMessage(channel, message);
        logger.info("Message received on channel {} \n MSG: {}", channel, message);
    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        super.onSubscribe(channel, subscribedChannels);
        logger.info("User is subscribed to channel {}", channel);
        logger.info("User is subscribed to {} channels", subscribedChannels);
    }

    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
        super.onUnsubscribe(channel, subscribedChannels);
        logger.info("User is unsubscribed from channel {}", channel);
        logger.info("User is subscribed to {}", subscribedChannels);
    }

    public String get_msg(String user) {

        return "";
    }
}
