package gg.desolve.bubble;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class PaperBubblePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();
        new Bubble();

        long end = System.currentTimeMillis() - start;
        Bubble.getLogger().info("Hooked bubble with paper in " + end + "ms.");
    }
}
