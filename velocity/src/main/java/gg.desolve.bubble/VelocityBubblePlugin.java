package gg.desolve.bubble;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import org.slf4j.Logger;

@Plugin(
        id = "bubble",
        name = "Bubble",
        version = "1.0",
        authors = "Desolve"
)
public class VelocityBubblePlugin {

    @Inject
    private Logger logger;

    @Subscribe
    public void onProxyInit(ProxyInitializeEvent event) {
        new Bubble();
        logger.info("Bubble initialized for Velocity!");
    }
}
