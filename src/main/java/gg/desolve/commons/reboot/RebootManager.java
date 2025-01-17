package gg.desolve.commons.reboot;

import gg.desolve.commons.Commons;
import gg.desolve.commons.relevance.Converter;
import lombok.Data;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

@Data
public class RebootManager {

    public Reboot reboot;

    public RebootManager() {
        String rebootTime = Commons.getInstance().getLanguageConfig().getString("reboot.reboot_time");
        long duration = Converter.duration(rebootTime);

        reboot = new Reboot(
                null,
                System.currentTimeMillis(),
                null,
                0L,
                duration,
                duration > 0
        );

        if (duration > 0) start();
    }


    public void start() {
        List<String> intervals = Commons.getInstance().getLanguageConfig().getConfig().getStringList("reboot.reboot_timing");

        long current = System.currentTimeMillis();
        long target = reboot.getAddedAt() + reboot.getDelay();

        intervals.stream()
                .map(Converter::duration)
                .filter(interval -> interval > 0)
                .forEach(interval -> {
                    if ((target - interval) > current)
                        schedule(interval, current, (target - interval));
                });

        Commons.getInstance().getInstanceManager().broadcast(
                Commons.getInstance().getLanguageConfig().getString("reboot.reboot_create")
                        .replace("server%", Commons.getInstance().getInstanceManager().getInstance().getName())
                        + "&%$commons.*|commons.admin");
    }


    private void schedule(long interval, long current, long schedule) {
        reboot.setTask(
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (reboot.getDelay() <= 0L) return;

                        Commons.getInstance().getInstanceManager().getInstance().announce(
                                Commons.getInstance().getLanguageConfig().getString("reboot.reboot_message")
                                        .replace("remaining%", Converter.time(interval)));

                        if (interval == Converter.duration("1s"))
                            reboot.getRunnable().run();
                    }
        });

        reboot.getTask().runTaskLater(Commons.getInstance(), (schedule - current) / 50);
    }

    public void cancel() {
        if (reboot.getTask() == null) return;
        reboot.getTask().cancel();

        Commons.getInstance().getInstanceManager().broadcast(
                Commons.getInstance().getLanguageConfig().getString("reboot.reboot_remove")
                        .replace("server%", Commons.getInstance().getInstanceManager().getInstance().getName())
                        + "&%$commons.*|commons.admin");
    }
}
