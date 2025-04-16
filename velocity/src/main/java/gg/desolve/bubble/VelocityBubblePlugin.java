package gg.desolve.bubble;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;

@Plugin(
        id = "bubble",
        name = "Bubble",
        version = "1.0",
        authors = "Desolve"
)
public class VelocityBubblePlugin {

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        long start = System.currentTimeMillis();
        new Bubble();

        long end = System.currentTimeMillis() - start;
        Bubble.getLogger().info("Blinked with velocity in " + end + "ms.");
    }
}
