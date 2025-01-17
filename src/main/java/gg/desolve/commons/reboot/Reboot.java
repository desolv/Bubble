package gg.desolve.commons.reboot;

import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

@Data
public class Reboot {

    private final UUID addedBy;
    private final long addedAt;
    private final UUID removedBy;
    private final long removedAt;
    private final Runnable runnable;
    private long delay;
    private BukkitRunnable task;
    private boolean status;

    public Reboot(UUID addedBy, long addedAt, UUID removedBy, long removedAt, long delay, boolean status) {
        this.addedBy = addedBy;
        this.addedAt = addedAt;
        this.removedBy = removedBy;
        this.removedAt = removedAt;
        this.runnable = Bukkit::shutdown;
        this.delay = delay;
        this.status = status;
    }
}
