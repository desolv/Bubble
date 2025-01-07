package gg.desolve.commons.redis;

import gg.desolve.commons.Commons;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

public class RedisManager {

    private JedisPool jedisPool;

    public RedisManager(String url) {
        try {
            JedisPoolConfig poolConfig = new JedisPoolConfig();
            poolConfig.setMaxTotal(30);
            poolConfig.setMaxIdle(15);
            poolConfig.setBlockWhenExhausted(true);

            jedisPool = new JedisPool(poolConfig, url);

            String timing = String.valueOf(System.currentTimeMillis() - Commons.getInstance().getInstanceManager().getInstance().getBooting());
            Commons.getInstance().getLogger().info("Merged Redis @ " + timing + "ms.");
        } catch (Exception e) {
            Commons.getInstance().getLogger().warning("There was a problem connecting to Redis.");
            e.printStackTrace();
        }
    }

    public String get(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.get(key);
        } catch (Exception e) {
            Commons.getInstance().getLogger().warning("An error occurred while retrieving " + key + ".");
            e.printStackTrace();
        }
        return null;
    }

    public Collection<String> keys(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.keys(key).stream()
                    .map(jedis::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            Commons.getInstance().getLogger().warning("An error occurred while retrieving keys " + key + ".");
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void set(String key, String content) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(key, content);
        } catch (Exception e) {
            Commons.getInstance().getLogger().warning("An error occurred while setting " + key + ".");
            e.printStackTrace();
        }
    }

    public void set(String key, String content, long expire) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(key, content);
            jedis.expire(key, expire);
        } catch (Exception e) {
            Commons.getInstance().getLogger().warning("An error occurred while setting " + key + ".");
            e.printStackTrace();
        }
    }

    public void remove(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.del(key);
        } catch (Exception e) {
            Commons.getInstance().getLogger().warning("An error occurred while removing " + key + ".");
            e.printStackTrace();
        }
    }

    public void publish(String key, String content) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.publish(key, content);
        } catch (Exception e) {
            Commons.getInstance().getLogger().warning("An error occurred while publishing " + key + ".");
            e.printStackTrace();
        }
    }

    public void subscribe(JedisPubSub subscriber, String key) {
        new Thread(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.subscribe(subscriber, key);
            } catch (Exception e) {
                Commons.getInstance().getLogger().warning("An error occurred while subscribing " + key + ".");
                e.printStackTrace();
            }
        }).start();
    }

    public void flush() {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.flushAll();
            Commons.getInstance().getLogger().info("Redis data has been flushed.");
        } catch (Exception e) {
            Commons.getInstance().getLogger().warning("An error occurred while flushing Redis data.");
            e.printStackTrace();
        }
    }

    public void close() {
        jedisPool.getResource().close();
    }
}
