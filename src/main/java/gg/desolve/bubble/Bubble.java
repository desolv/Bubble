package gg.desolve.bubble;

import fr.minuskube.inv.InventoryManager;
import gg.desolve.bubble.command.CommandDirector;
import gg.desolve.bubble.command.CommandManager;
import gg.desolve.bubble.config.Config;
import gg.desolve.bubble.config.ConfigurationManager;
import gg.desolve.bubble.instance.InstanceManager;
import gg.desolve.bubble.listener.ListenerDirector;
import gg.desolve.bubble.mongo.MongoManager;
import gg.desolve.bubble.reboot.RebootManager;
import gg.desolve.bubble.redis.RedisManager;
import gg.desolve.bubble.redis.SubscriberDirector;
import gg.desolve.bubble.relevance.Message;
import lombok.Getter;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;

public final class Bubble extends JavaPlugin {

    @Getter
    public static Bubble instance;

    @Getter
    public ConfigurationManager configurationManager;

    @Getter
    public RedisManager redisManager;

    @Getter
    public MongoManager mongoManager;

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
    public RebootManager rebootManager;

    @Getter
    private InventoryManager inventoryManager;

    @Getter
    private BukkitAudiences adventure;

    @Override
    public void onEnable() {
        instance = this;

        adventure = BukkitAudiences.create(this);
        configurationManager = new ConfigurationManager(this, "language.yml", "storage.yml");

        Message.setPrefix(getLanguageConfig().getString("server.prefix"));

        redisManager = new RedisManager(getStorageConfig().getString("redis.url"));
        mongoManager = new MongoManager(getStorageConfig().getString("mongo.url"), getStorageConfig().getString("mongo.database"));
        instanceManager = new InstanceManager();

        commandManager = new CommandManager(this, "bubble.*");
        commandDirector = new CommandDirector(commandManager);
        listenerDirector = new ListenerDirector();
        subscriberDirector = new SubscriberDirector();
        rebootManager = new RebootManager();

        inventoryManager = new InventoryManager(this);
        inventoryManager.init();
    }

    @Override
    public void onDisable() {
        instanceManager.remove();
        redisManager.close();
        mongoManager.close();
    }

    public Config getLanguageConfig() {
        return configurationManager.getConfig("language.yml");
    }

    public Config getStorageConfig() {
        return configurationManager.getConfig("storage.yml");
    }
}
