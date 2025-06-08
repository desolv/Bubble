package gg.desolve.paper;

import gg.desolve.api.Bubble;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class PaperBubblePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        new Bubble();
    }
}
