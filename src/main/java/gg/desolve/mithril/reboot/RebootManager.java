package gg.desolve.mithril.reboot;

import gg.desolve.mithril.Mithril;
import gg.desolve.mithril.relevance.Converter;
import lombok.Data;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;

@Data
public class RebootManager {

    public Reboot reboot;

    public RebootManager() {
        String rebootTime = Mithril.getInstance().getLanguageConfig().getString("reboot.scheduled");
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
        List<String> intervals = Arrays.asList(Mithril.getInstance().getLanguageConfig().getString("reboot.timing").split("\\|"));

        long current = System.currentTimeMillis();
        long target = reboot.getAddedAt() + reboot.getDelay();

        intervals.stream()
                .map(Converter::duration)
                .filter(interval -> interval > 0)
                .forEach(interval -> {
                    if ((target - interval) > current)
                        schedule(interval, current, (target - interval));
                });

        Mithril.getInstance().getInstanceManager().broadcast(
                Mithril.getInstance().getLanguageConfig().getString("reboot.create")
                        .replace("server%", Mithril.getInstance().getInstanceManager().getInstance().getName())
                        + "&%$mithril.*|mithril.admin");
    }


    private void schedule(long interval, long current, long schedule) {
        reboot.setTask(
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (reboot.getDelay() <= 0L) return;

                        Mithril.getInstance().getInstanceManager().getInstance().announce(
                                Mithril.getInstance().getLanguageConfig().getString("reboot.message")
                                        .replace("remaining%", Converter.time(interval)));

                        if (interval == Converter.duration("1s"))
                            reboot.getRunnable().run();
                    }
        });

        reboot.getTask().runTaskLater(Mithril.getInstance(), (schedule - current) / 50);
    }

    public void cancel() {
        if (reboot.getTask() == null) return;
        reboot.getTask().cancel();

        Mithril.getInstance().getInstanceManager().broadcast(
                Mithril.getInstance().getLanguageConfig().getString("reboot.remove").replace("server%", Mithril.getInstance().getInstanceManager().getInstance().getName())
                        + "&%$mithril.*|mithril.admin");
    }
}
