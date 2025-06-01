package gg.desolve.api;

import gg.desolve.api.blueprint.Configuration;
import lombok.Getter;

@Getter
public class Bubble {

    public RedisManager redisManager;
    public Configuration configuration;

    public Bubble() {
        configuration = new Configuration("plugins/Bubble", "repository.yml");
        redisManager = new RedisManager(configuration.getRoot("repository.yml").node("redis.url").toString());
    }
}
