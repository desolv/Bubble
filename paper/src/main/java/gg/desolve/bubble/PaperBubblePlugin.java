package gg.desolve.bubble;

import org.bukkit.plugin.java.JavaPlugin;

public class PaperBubblePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();
        new Bubble();

        long end = System.currentTimeMillis() - start;
        Bubble.getLogger().info("Blinked with paper in " + end + "ms.");
    }
}
