package gg.desolve.paper;

import co.aikar.commands.PaperCommandManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PaperBubbleCommandManager {

    private final PaperCommandManager manager;;

    public PaperBubbleCommandManager(JavaPlugin plugin) {
        manager = new PaperCommandManager(plugin);
        manager.enableUnstableAPI("help");
    }

    public PaperCommandManager getInstance() {
        if (manager == null) {
            throw new IllegalStateException("PaperBubbleCommandManager not initialized.");
        }
        return manager;
    }
}
