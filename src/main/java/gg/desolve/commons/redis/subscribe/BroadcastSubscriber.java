package gg.desolve.commons.redis.subscribe;

import gg.desolve.commons.Commons;
import redis.clients.jedis.JedisPubSub;

public class BroadcastSubscriber extends JedisPubSub {

    @Override
    public void onMessage(String channel, String message) {
        String[] parts = message.split("&%\\$");
        String msg = parts[0];
        String permission = parts[1];

        if (permission.equalsIgnoreCase("none")) {
            Commons.getInstance().getInstanceManager().getInstance().announce(msg);
            return;
        }

        Commons.getInstance().getInstanceManager().getInstance().announce(msg, permission);
    }
}
