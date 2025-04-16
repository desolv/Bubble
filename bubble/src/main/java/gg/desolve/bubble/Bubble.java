package gg.desolve.bubble;

import gg.desolve.bubble.redis.RedisManager;
import gg.desolve.bubble.toolkit.Configuration;
import gg.desolve.bubble.toolkit.Logger;
import lombok.Getter;

public class Bubble {

    @Getter
    public static Logger logger;

    @Getter
    public RedisManager redisManager;

    @Getter
    public Configuration configuration;

    public Bubble() {
        logger = new Logger();
        configuration = new Configuration("plugins/Bubble", "repository.yml");
        redisManager = new RedisManager(configuration.get("repository.yml", "redis.url").toString());

    }
}
