package gg.desolve.bubble.redis;

import gg.desolve.bubble.Bubble;
import redis.clients.jedis.JedisPubSub;

public class BroadcastSubscriber extends JedisPubSub {

    @Override
    public void onMessage(String channel, String message) {
        String[] parts = message.split("&%\\$");
        String msg = parts[0];
        String permission = parts[1];

        if (permission.equalsIgnoreCase("none")) {
            Bubble.getInstance().getInstanceManager().getInstance().announce(msg);
            return;
        }

        Bubble.getInstance().getInstanceManager().getInstance().announce(msg, permission);
    }
}
