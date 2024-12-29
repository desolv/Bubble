package gg.desolve.commons.instance;

import com.google.gson.Gson;
import gg.desolve.commons.Commons;
import gg.desolve.commons.relevance.Converter;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

@Getter
public class InstanceManager {

    public Instance instance;
    private static final Gson gson = new Gson();

    public InstanceManager() {
        instance = new Instance(
                Converter.generateId(),
                Commons.getInstance().getConfig("language.yml").getString("server_management.server-name"),
                Commons.getInstance().getServer().getVersion(),
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
        }.runTaskTimer(Commons.getInstance(), 0, 20 * 60);
    }


    public void create(Instance instance) {
        Commons.getInstance().getRedisManager().set("instance:" + instance.getId(), gson.toJson(instance));
        Commons.getInstance().getLogger().info("New instance @ " + instance.getName() + " #" + instance.getId() + ".");
    }

    public void save(Instance instance) {
        Commons.getInstance().getRedisManager().set("instance:" + instance.getId(), gson.toJson(instance), 300);
    }

    public void remove(Instance instance) {
        Commons.getInstance().getRedisManager().remove("instance:" + instance.getId());
        Commons.getInstance().getLogger().info("Removed instance @ " + instance.getName() + " #" + instance.getId() + ".");
    }

    public Instance retrieve(String instanceId) {
        return Commons.getInstance()
                .getRedisManager()
                .keys("instance:*")
                .stream()
                .map(i -> gson.fromJson(i, Instance.class))
                .filter(instance -> instance.getId().equals(instanceId))
                .findFirst()
                .get();
    }

    public void validate() {
        Commons.getInstance()
                .getRedisManager()
                .keys("instance:*")
                .stream()
                .map(i -> gson.fromJson(i, Instance.class))
                .filter(instance -> Converter.seconds(System.currentTimeMillis() - instance.getHeartbeat()) > 65)
                .forEach(this::remove);
    }

}
