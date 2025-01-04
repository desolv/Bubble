package gg.desolve.commons;

import gg.desolve.commons.command.CommandDirector;
import gg.desolve.commons.command.CommandManager;
import gg.desolve.commons.config.Config;
import gg.desolve.commons.config.ConfigurationManager;
import gg.desolve.commons.instance.Instance;
import gg.desolve.commons.instance.InstanceManager;
import gg.desolve.commons.listener.ListenerDirector;
import gg.desolve.commons.redis.RedisManager;
import gg.desolve.commons.redis.subscribe.SubscriberDirector;
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
    public CommandDirector commandDirector;

    @Getter
    public ListenerDirector listenerDirector;

    @Getter
    public SubscriberDirector subscriberDirector;

    @Getter
    private BukkitAudiences adventure;

    @Override
    public void onEnable() {
        instance = this;

        configurationManager = new ConfigurationManager(this, "language.yml", "storage.yml");
        instanceManager = new InstanceManager();
        redisManager = new RedisManager(configurationManager.getConfig("storage.yml").getString("redis.url"));
        instanceManager.create(instanceManager.getInstance());
        commandManager = new CommandManager(this, "commons.*", "language.yml");
        commandDirector = new CommandDirector(commandManager);
        listenerDirector = new ListenerDirector();
        subscriberDirector = new SubscriberDirector();
        adventure = BukkitAudiences.create(this);
    }

    @Override
    public void onDisable() {
        Instance instance = instanceManager.getInstance();
        instance.setDisabling(true);
        instanceManager.remove(instance);

        redisManager.close();
    }

    public Config getLanguageConfig() {
        return configurationManager.getConfig("language.yml");
    }
}
