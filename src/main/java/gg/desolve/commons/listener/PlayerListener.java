package gg.desolve.commons.listener;

import gg.desolve.commons.Commons;
import gg.desolve.commons.instance.InstanceManager;
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
        InstanceManager instanceManager = Commons.getInstance().getInstanceManager();

        Message.send(player, Commons.getInstance().getLanguageConfig().getString("server.welcome_motd")
                .replace("server%", Commons.getInstance().getLanguageConfig().getString("server.server_display")));

        Message.send(player, Commons.getInstance().getLanguageConfig().getString("instance.welcome_instance_lifespan")
                .replace("time%", Converter.time(System.currentTimeMillis() - instanceManager.getInstance().getBooting())),
                "commons.admin");

        Message.send(player, Commons.getInstance().getLanguageConfig().getString("instance.welcome_instance_schedule")
                .replace("time%", Converter.time(Commons.getInstance().getRebootManager().getReboot().getAddedAt()
                        + Commons.getInstance().getRebootManager().getReboot().getDelay() - System.currentTimeMillis())),
                "commons.admin");
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
