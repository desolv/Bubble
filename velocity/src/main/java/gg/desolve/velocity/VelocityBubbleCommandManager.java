package gg.desolve.velocity;


import co.aikar.commands.VelocityCommandManager;
import com.velocitypowered.api.proxy.ProxyServer;

public class VelocityBubbleCommandManager {

    private final VelocityCommandManager manager;;

    public VelocityBubbleCommandManager(Object plugin, ProxyServer server) {
        manager = new VelocityCommandManager(server, plugin);
        manager.enableUnstableAPI("help");
    }

    public VelocityCommandManager getInstance() {
        if (manager == null) {
            throw new IllegalStateException("VelocityBubbleCommandManager not initialized.");
        }
        return manager;
    }
}
