package gg.desolve.mithril.redis;

import gg.desolve.mithril.Mithril;
import redis.clients.jedis.JedisPubSub;

public class BroadcastSubscriber extends JedisPubSub {

    @Override
    public void onMessage(String channel, String message) {
        String[] parts = message.split("&%\\$");
        String msg = parts[0];
        String permission = parts[1];

        if (permission.equalsIgnoreCase("none")) {
            Mithril.getInstance().getInstanceManager().getInstance().announce(msg);
            return;
        }

        Mithril.getInstance().getInstanceManager().getInstance().announce(msg, permission);
    }
}
