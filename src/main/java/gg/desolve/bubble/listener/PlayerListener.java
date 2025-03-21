package gg.desolve.bubble.listener;

import gg.desolve.bubble.Bubble;
import gg.desolve.bubble.reboot.Reboot;
import gg.desolve.bubble.relevance.Converter;
import gg.desolve.bubble.relevance.Message;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    @EventHandler(
            priority = EventPriority.HIGHEST
    )
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);

        Player player = event.getPlayer();
        Reboot reboot = Bubble.getInstance().getRebootManager().getReboot();

        Message.send(player, Bubble.getInstance().getLanguageConfig().getString("server.welcome_motd")
                .replace("server%", Bubble.getInstance().getLanguageConfig().getString("server.display")));

        Message.send(player, Bubble.getInstance().getLanguageConfig().getString("instance.welcome_lifespan")
                .replace("time%", Converter.time(System.currentTimeMillis() - Bubble.getInstance().getInstanceManager().getInstance().getBooting())),
                "bubble.*|bubble.admin");

        if (reboot.isStatus())
            Message.send(player, Bubble.getInstance().getLanguageConfig().getString("instance.welcome_scheduled")
                            .replace("time%",
                                    Converter.time(reboot.getAddedAt() + reboot.getDelay() - System.currentTimeMillis())),
                    "bubble.*|bubble.admin");
        else Message.send(player, Bubble.getInstance().getLanguageConfig().getString("instance.welcome_unscheduled"));
    }

    @EventHandler(
            priority = EventPriority.HIGHEST
    )
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
    }

    @EventHandler(
            priority = EventPriority.HIGHEST
    )
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        event.setFormat(event.getPlayer().getName() + ": " + event.getMessage());
    }

}
