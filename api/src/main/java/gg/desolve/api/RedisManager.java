package gg.desolve.api;

import com.mongodb.Function;
import gg.desolve.api.blueprint.Logger;
import lombok.Getter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
public class RedisManager {

    private static JedisPool jedisPool;
    private static final ExecutorService executors = Executors.newSingleThreadExecutor();

    public RedisManager(String host) {
        try {
            long start = System.currentTimeMillis();

            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(50);
            config.setMaxIdle(25);
            config.setMinIdle(5);
            config.setTestOnBorrow(true);

            jedisPool = new JedisPool(config, host);
            long end = System.currentTimeMillis() - start;

            Logger.info("Deployed to redis in " + end + "ms.");
        } catch (Exception e) {
            Logger.warning("There was a problem connecting to Redis.");
            e.printStackTrace();
        }
    }

    public static <T> T withJedis(Function<Jedis, T> function) {
        try (Jedis jedis = jedisPool.getResource()) {
            return function.apply(jedis);
        } catch (Exception e) {
            Logger.warning("An error occurred during Redis query.");
            e.printStackTrace();
            return null;
        }
    }
}
