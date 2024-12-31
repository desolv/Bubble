package gg.desolve.commons;

import gg.desolve.commons.command.CommandDirector;
import gg.desolve.commons.command.CommandManager;
import gg.desolve.commons.config.Config;
import gg.desolve.commons.config.ConfigurationManager;
import gg.desolve.commons.instance.InstanceManager;
import gg.desolve.commons.redis.RedisManager;
import lombok.Getter;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;

public final class Commons extends JavaPlugin {

    @Getter
    public static Commons instance;

    @Getter
    public ConfigurationManager configurationManager;

    @Getter
    public RedisManager redisManager;

    @Getter
    public InstanceManager instanceManager;

    @Getter
    public CommandManager commandManager;

    @Getter
    private BukkitAudiences adventure;

    @Override
    public void onEnable() {
        instance = this;

        configurationManager = new ConfigurationManager(this, "language.yml", "storage.yml");
        instanceManager = new InstanceManager();
        redisManager = new RedisManager(getConfig("storage.yml").getString("redis.url"));
        instanceManager.create(instanceManager.getInstance());
        commandManager = new CommandManager(this, "commons.*", "language.yml");
        new CommandDirector(commandManager);
        adventure = BukkitAudiences.create(this);
    }

    @Override
    public void onDisable() {
        instanceManager.remove(instanceManager.getInstance());
        redisManager.close();
    }

    public Config getConfig(String name) {
        return configurationManager.getConfig(name);
    }
}
