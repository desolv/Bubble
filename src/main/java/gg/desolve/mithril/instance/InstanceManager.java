package gg.desolve.mithril.instance;

import com.google.gson.Gson;
import gg.desolve.mithril.Mithril;
import gg.desolve.mithril.relevance.Converter;
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
                Mithril.getInstance().getLanguageConfig().getString("server.server_name"),
                Mithril.getInstance().getServer().getVersion(),
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
        }.runTaskTimer(Mithril.getInstance(), 0, 20 * 60);

        create(instance);
    }

    public void create(Instance instance) {
        Mithril.getInstance().getRedisManager().set("instance:" + instance.getId(), gson.toJson(instance));
        Mithril.getInstance().getLogger().info("New instance @ " + instance.getName() + " #" + instance.getId() + ".");

        broadcast(Mithril.getInstance().getLanguageConfig().getString("instance.instance_create")
                .replace("server%", instance.getName())
                .replace("id%", instance.getId()) + "&%$mithril.*|mithril.admin");
    }

    public void save(Instance instance) {
        Mithril.getInstance().getRedisManager().set("instance:" + instance.getId(), gson.toJson(instance), 300);
    }

    public void remove() {
        remove(instance);
    }

    public void remove(Instance instance) {
        Mithril.getInstance().getRedisManager().remove("instance:" + instance.getId());
        Mithril.getInstance().getLogger().info("Removed instance @ " + instance.getName() + " #" + instance.getId() + ".");

        broadcast(Mithril.getInstance().getLanguageConfig().getString("instance.instance_remove")
                .replace("server%", instance.getName())
                .replace("id%", instance.getId()) + "&%$mithril.*|mithril.admin");
    }

    public Instance retrieve(String instanceId) {
        return Mithril.getInstance().getRedisManager().keys("instance:*")
                .stream()
                .map(i -> gson.fromJson(i, Instance.class))
                .filter(instance -> (instance.getId().equals(instanceId) || instance.getName().equals(instanceId))
                        && Converter.seconds(System.currentTimeMillis() - instance.getHeartbeat()) < 65)
                .findFirst()
                .orElse(null);
    }

    public List<Instance> retrieve() {
        return Mithril.getInstance().getRedisManager().keys("instance:*")
                .stream()
                .map(i -> gson.fromJson(i, Instance.class))
                .filter(instance -> Converter.seconds(System.currentTimeMillis() - instance.getHeartbeat()) < 65)
                .toList();
    }

    public void validate() {
        Mithril.getInstance().getRedisManager().keys("instance:*")
                .stream()
                .map(i -> gson.fromJson(i, Instance.class))
                .filter(instance -> Converter.seconds(System.currentTimeMillis() - instance.getHeartbeat()) > 65)
                .forEach(this::remove);
    }

    public void broadcast(String message) {
        Mithril.getInstance().getRedisManager().publish("Broadcast", message);
    }

}
