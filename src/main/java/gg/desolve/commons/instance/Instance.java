package gg.desolve.commons.instance;

import gg.desolve.commons.Commons;
import gg.desolve.commons.relevance.Message;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Data
public class Instance {

    private final String id;
    private final String name;
    private final String version;
    private long online;
    private final long booting;
    private long heartbeat;

    public Instance(String id, String name, String version, long online, long booting, long heartbeat) {
        this.id = id;
        this.name = name;
        this.version = version;
        this.online = online;
        this.booting = booting;
        this.heartbeat = heartbeat;
    }

    public void broadcast(String message) {
        Commons.getInstance().getRedisManager().publish("Broadcast", message);
    }

    public void announce(String message) {
        Bukkit.getOnlinePlayers().forEach(player -> Message.send(player, message));
    }

    public void announce(String message, String permission) {
        Bukkit.getOnlinePlayers().stream()
                .filter(player -> (player.hasPermission(permission) ||
                        Arrays.stream(permission.split("\\|")).anyMatch(player::hasPermission)))
                .forEach(player -> Message.send(player, message));
    }

    public void command(String command) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
    }

    public List<UUID> players() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getUniqueId).toList();
    }
}
