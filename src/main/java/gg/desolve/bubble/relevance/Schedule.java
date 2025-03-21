package gg.desolve.bubble.relevance;

import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

@Data
public class Schedule {

    private final transient Runnable runnable;
    private final String identity;
    private final long delay;
    private transient BukkitTask task;
    private final Plugin plugin;

    public Schedule(String identity, Runnable runnable, long delay, Plugin plugin) {
        this.identity = identity;
        this.runnable = runnable;
        this.delay = delay;
        this.plugin = plugin;
    }

    public void start() {
        task = Bukkit.getScheduler().runTaskLaterAsynchronously(
                plugin,
                runnable,
                delay / 50
        );
    }

    public void cancel() {
        if (task != null) {
            task.cancel();
        }
    }

}
