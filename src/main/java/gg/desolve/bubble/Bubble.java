package gg.desolve.bubble;

import gg.desolve.bubble.command.CommandManager;
import gg.desolve.bubble.config.Configuration;
import gg.desolve.bubble.mongo.MongoManager;
import gg.desolve.bubble.redis.RedisManager;
import gg.desolve.bubble.relevance.Message;
import lombok.Getter;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Bubble extends JavaPlugin {

    @Getter
    public static Bubble instance;

    @Getter
    public Configuration configuration;

    @Getter
    public RedisManager redisManager;

    @Getter
    public MongoManager mongoManager;

    @Getter
    public CommandManager commandManager;

    @Getter
    private BukkitAudiences adventure;

    @Override
    public void onEnable() {
        instance = this;

        configuration = new Configuration(this, "language.yml", "storage.yml");

        redisManager = new RedisManager(getStorageConfig().getString("redis.url"));
        mongoManager = new MongoManager(getStorageConfig().getString("mongo.url"), getStorageConfig().getString("mongo.database"));

        commandManager = new CommandManager(this, "bubble.*");

        adventure = BukkitAudiences.create(this);
        Message.setBadge(getLanguageConfig().getString("server.badge"));
    }

    @Override
    public void onDisable() {
        redisManager.close();
        mongoManager.close();
    }

    public FileConfiguration getLanguageConfig() {
        return configuration.getConfig("language.yml");
    }

    public FileConfiguration getStorageConfig() {
        return configuration.getConfig("storage.yml");
    }
}
