package gg.desolve.commons.listener;

import gg.desolve.commons.Commons;
import gg.desolve.commons.reboot.Reboot;
import gg.desolve.commons.relevance.Converter;
import gg.desolve.commons.relevance.Message;
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
        Reboot reboot = Commons.getInstance().getRebootManager().getReboot();

        Message.send(player, Commons.getInstance().getLanguageConfig().getString("server.welcome_motd")
                .replace("server%", Commons.getInstance().getLanguageConfig().getString("server.server_display")));

        Message.send(player, Commons.getInstance().getLanguageConfig().getString("instance.welcome_instance_lifespan")
                .replace("time%", Converter.time(System.currentTimeMillis() - Commons.getInstance().getInstanceManager().getInstance().getBooting())),
                "commons.admin");

        if (reboot.isStatus())
            Message.send(player, Commons.getInstance().getLanguageConfig().getString("instance.welcome_instance_schedule")
                            .replace("time%", Converter.time(reboot.getAddedAt() + reboot.getDelay() - System.currentTimeMillis())),
                    "commons.admin");
        else Message.send(player, Commons.getInstance().getLanguageConfig().getString("instance.welcome_instance_not_schedule"));
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
