package gg.desolve.bubble.reboot;

import gg.desolve.bubble.Bubble;
import gg.desolve.bubble.relevance.Converter;
import lombok.Data;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;

@Data
public class RebootManager {

    public Reboot reboot;

    public RebootManager() {
        String rebootTime = Bubble.getInstance().getLanguageConfig().getString("reboot.scheduled");
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
        List<String> intervals = Arrays.asList(Bubble.getInstance().getLanguageConfig().getString("reboot.timing").split("\\|"));

        long current = System.currentTimeMillis();
        long target = reboot.getAddedAt() + reboot.getDelay();

        intervals.stream()
                .map(Converter::duration)
                .filter(interval -> interval > 0)
                .forEach(interval -> {
                    if ((target - interval) > current)
                        schedule(interval, current, (target - interval));
                });

        Bubble.getInstance().getInstanceManager().broadcast(
                Bubble.getInstance().getLanguageConfig().getString("reboot.create")
                        .replace("server%", Bubble.getInstance().getInstanceManager().getInstance().getName())
                        + "&%$bubble.*|bubble.admin");
    }


    private void schedule(long interval, long current, long schedule) {
        reboot.setTask(
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (reboot.getDelay() <= 0L) return;

                        Bubble.getInstance().getInstanceManager().getInstance().announce(
                                Bubble.getInstance().getLanguageConfig().getString("reboot.message")
                                        .replace("remaining%", Converter.time(interval)));

                        if (interval == Converter.duration("1s"))
                            reboot.getRunnable().run();
                    }
        });

        reboot.getTask().runTaskLater(Bubble.getInstance(), (schedule - current) / 50);
    }

    public void cancel() {
        if (reboot.getTask() == null) return;
        reboot.getTask().cancel();

        Bubble.getInstance().getInstanceManager().broadcast(
                Bubble.getInstance().getLanguageConfig().getString("reboot.remove").replace("server%", Bubble.getInstance().getInstanceManager().getInstance().getName())
                        + "&%$bubble.*|bubble.admin");
    }
}
