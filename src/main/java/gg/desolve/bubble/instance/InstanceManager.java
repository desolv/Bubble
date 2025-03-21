package gg.desolve.bubble.instance;

import com.google.gson.Gson;
import gg.desolve.bubble.Bubble;
import gg.desolve.bubble.relevance.Converter;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

@Getter
public class InstanceManager {

    public Instance instance;
    private static final Gson gson = new Gson();

    public InstanceManager() {
        instance = new Instance(
                Converter.generateId(),
                Bubble.getInstance().getLanguageConfig().getString("server.name"),
                Bubble.getInstance().getServer().getVersion(),
                0,
                System.currentTimeMillis(),
                System.currentTimeMillis()
        );

        new BukkitRunnable() {
            @Override
            public void run() {
                validate();

                instance.setHeartbeat(System.currentTimeMillis());
                instance.setOnline(Bukkit.getOnlinePlayers().size());
                save(instance);
            }
        }.runTaskTimer(Bubble.getInstance(), 0, 20 * 60);

        create(instance);
    }

    public void create(Instance instance) {
        Bubble.getInstance().getRedisManager().set("instance:" + instance.getId(), gson.toJson(instance));
        Bubble.getInstance().getLogger().info("New instance @ " + instance.getName() + " #" + instance.getId() + ".");

        broadcast(Bubble.getInstance().getLanguageConfig().getString("instance.create")
                .replace("server%", instance.getName())
                .replace("id%", instance.getId()) + "&%$bubble.*|bubble.admin");
    }

    public void save(Instance instance) {
        Bubble.getInstance().getRedisManager().set("instance:" + instance.getId(), gson.toJson(instance), 300);
    }

    public void remove() {
        remove(instance);
    }

    public void remove(Instance instance) {
        Bubble.getInstance().getRedisManager().remove("instance:" + instance.getId());
        Bubble.getInstance().getLogger().info("Removed instance @ " + instance.getName() + " #" + instance.getId() + ".");

        broadcast(Bubble.getInstance().getLanguageConfig().getString("instance.remove")
                .replace("server%", instance.getName())
                .replace("id%", instance.getId()) + "&%$bubble.*|bubble.admin");
    }

    public Instance retrieve(String instanceId) {
        String json = Bubble.getInstance().getRedisManager().get("instance:" + instanceId);
        if (json == null) return null;

        Instance instance = gson.fromJson(json, Instance.class);
        return (Converter.seconds(System.currentTimeMillis() - instance.getHeartbeat()) < 65) ? instance : null;
    }

    public List<Instance> retrieve() {
        return Bubble.getInstance().getRedisManager().keys("instance:*")
                .stream()
                .map(i -> gson.fromJson(i, Instance.class))
                .filter(instance -> Converter.seconds(System.currentTimeMillis() - instance.getHeartbeat()) < 65)
                .toList();
    }

    public void validate() {
        Bubble.getInstance().getRedisManager().keys("instance:*")
                .stream()
                .map(i -> gson.fromJson(i, Instance.class))
                .filter(instance -> Converter.seconds(System.currentTimeMillis() - instance.getHeartbeat()) > 65)
                .forEach(this::remove);
    }

    public void broadcast(String message) {
        Bubble.getInstance().getRedisManager().publish("Broadcast", message);
    }

}
