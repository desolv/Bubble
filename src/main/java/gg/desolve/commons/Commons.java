package gg.desolve.commons;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class Commons extends JavaPlugin {

    @Getter
    public static Commons instance;

    @Override
    public void onEnable() {
        instance = this;

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
