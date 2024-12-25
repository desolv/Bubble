package gg.desolve.commons;

import gg.desolve.commons.config.Config;
import gg.desolve.commons.config.ConfigurationManager;
import gg.desolve.commons.redis.RedisManager;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class Commons extends JavaPlugin {

    @Getter
    public static Commons instance;

    @Getter
    public ConfigurationManager configurationManager;

    @Getter
    public RedisManager redisManager;


    @Override
    public void onEnable() {
        instance = this;

        configurationManager = new ConfigurationManager(this, "language.yml", "storage.yml");
        redisManager = new RedisManager();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public Config getConfig(String name) {
        return configurationManager.getConfig(name);
    }
}
