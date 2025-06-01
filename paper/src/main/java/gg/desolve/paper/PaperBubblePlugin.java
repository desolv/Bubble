package gg.desolve.paper;

import gg.desolve.api.Bubble;
import gg.desolve.api.blueprint.Logger;
import org.bukkit.plugin.java.JavaPlugin;

public class PaperBubblePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();
        new Bubble();

        long end = System.currentTimeMillis() - start;
        Logger.info("Blinked with paper in " + end + "ms.");
    }
}
