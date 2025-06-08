package gg.desolve.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import gg.desolve.api.Bubble;
import gg.desolve.velocity.command.PingCommand;

@Plugin(
        id = "bubble",
        name = "Bubble",
        version = "1.0",
        authors = "Desolve"
)
public class VelocityBubblePlugin {

    private final ProxyServer server;
    private VelocityBubbleCommandManager velocityBubbleCommandManager;

    @Inject
    public VelocityBubblePlugin(ProxyServer server) {
        this.server = server;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        new Bubble();

        velocityBubbleCommandManager = new VelocityBubbleCommandManager(this, server);
        velocityBubbleCommandManager.getInstance().registerCommand(new PingCommand());
    }
}
