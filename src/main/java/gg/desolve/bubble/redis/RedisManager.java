package gg.desolve.bubble.redis;

import gg.desolve.bubble.Bubble;
import lombok.Getter;
import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class RedisManager {

    @Getter
    private static JedisPool pools;
    private static final ExecutorService executors = Executors.newSingleThreadExecutor();

    public RedisManager(String host) {
        try {
            long start = System.currentTimeMillis();

            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(50);
            config.setMaxIdle(25);
            config.setMinIdle(5);
            config.setTestOnBorrow(true);

            pools = new JedisPool(config, host);

            Bubble.getInstance().getLogger().info("Connected to Redis in " + (System.currentTimeMillis() - start) + "ms.");
        } catch (Exception e) {
            Bubble.getInstance().getLogger().warning("There was a problem connecting to Redis.");
            e.printStackTrace();
        }
    }

    public String get(String key) {
        try (Jedis jedis = pools.getResource()) {
            return jedis.get(key);
        } catch (Exception e) {
            Bubble.getInstance().getLogger().warning("An error occurred while retrieving " + key + ".");
            e.printStackTrace();
        }
        return null;
    }

    public Collection<String> keys(String key) {
        try (Jedis jedis = pools.getResource()) {
            return jedis.keys(key).stream()
                    .map(jedis::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            Bubble.getInstance().getLogger().warning("An error occurred while retrieving keys " + key + ".");
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void set(String key, String content) {
        try (Jedis jedis = pools.getResource()) {
            jedis.set(key, content);
        } catch (Exception e) {
            Bubble.getInstance().getLogger().warning("An error occurred while setting " + key + ".");
            e.printStackTrace();
        }
    }

    public void set(String key, String content, long expire) {
        try (Jedis jedis = pools.getResource()) {
            jedis.set(key, content);
            jedis.expire(key, expire);
        } catch (Exception e) {
            Bubble.getInstance().getLogger().warning("An error occurred while setting " + key + ".");
            e.printStackTrace();
        }
    }

    public void remove(String key) {
        try (Jedis jedis = pools.getResource()) {
            jedis.del(key);
        } catch (Exception e) {
            Bubble.getInstance().getLogger().warning("An error occurred while removing " + key + ".");
            e.printStackTrace();
        }
    }

    public void publish(String key, String content) {
        try (Jedis jedis = pools.getResource()) {
            jedis.publish(key, content);
        } catch (Exception e) {
            Bubble.getInstance().getLogger().warning("An error occurred while publishing " + key + ".");
            e.printStackTrace();
        }
    }

    public static void subscribe(JedisPubSub listener, String channel) {
        executors.execute(() -> {
            try (Jedis jedis = pools.getResource()) {
                jedis.subscribe(listener, channel);
            } catch (Exception e) {
                Bubble.getInstance().getLogger().warning("An error occurred while subscribing on " + channel + ".");
                e.printStackTrace();
            }
        });
    }

    public static void pipelineWrite(Map<String, String> dataMap) {
        try (Jedis jedis = pools.getResource()) {
            Pipeline pipeline = jedis.pipelined();
            dataMap.forEach(pipeline::set);
            pipeline.sync();
        } catch (Exception e) {
            Bubble.getInstance().getLogger().warning("An error occurred while pipelining.");
            e.printStackTrace();
        }
    }

    public void flush() {
        try (Jedis jedis = pools.getResource()) {
            jedis.flushAll();
            Bubble.getInstance().getLogger().info("Redis data has been flushed.");
        } catch (Exception e) {
            Bubble.getInstance().getLogger().warning("An error occurred while flushing Redis data.");
            e.printStackTrace();
        }
    }

    public static void close() {
        pools.close();
        executors.shutdown();
    }
}
