package gg.desolve.bubble;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class PaperBubblePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        new Bubble();
        getLogger().info("Bubble initialized for Paper!");
    }
}
