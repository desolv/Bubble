package gg.desolve.velocity;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import gg.desolve.api.Bubble;
import gg.desolve.api.blueprint.Logger;

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
        Logger.info("Blinked with velocity in " + end + "ms.");
    }
}
