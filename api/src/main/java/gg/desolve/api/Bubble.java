package gg.desolve.api;

import gg.desolve.api.blueprint.Configuration;
import lombok.Getter;

@Getter
public class Bubble {

    public RedisManager redisManager;
    public Configuration configuration;

    public Bubble() {
        configuration = new Configuration("Bubble", "repository.yml");
        redisManager = new RedisManager(configuration.get("repository.yml", "redis.url"));
    }
}
