package gg.desolve.commons;

import fr.minuskube.inv.InventoryManager;
import gg.desolve.commons.command.CommandDirector;
import gg.desolve.commons.command.CommandManager;
import gg.desolve.commons.config.Config;
import gg.desolve.commons.config.ConfigurationManager;
import gg.desolve.commons.mongo.MongoManager;
import gg.desolve.commons.instance.InstanceManager;
import gg.desolve.commons.listener.ListenerDirector;
import gg.desolve.commons.reboot.RebootManager;
import gg.desolve.commons.redis.RedisManager;
import gg.desolve.commons.redis.SubscriberDirector;
import gg.desolve.commons.scope.ScopeManager;
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
    public ScopeManager scopeManager;

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

        instanceManager = new InstanceManager();
        redisManager = new RedisManager(getStorageConfig().getString("redis.url"));
        mongoManager = new MongoManager(getStorageConfig().getString("mongo.url"), getStorageConfig().getString("mongo.database"));
        instanceManager.create();

        scopeManager = new ScopeManager();
        commandManager = new CommandManager("commons.*");
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
