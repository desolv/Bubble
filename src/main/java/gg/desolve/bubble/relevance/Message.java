package gg.desolve.bubble.relevance;

import gg.desolve.bubble.Bubble;
import lombok.Setter;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class Message {

    @Setter
    private static String badge;

    public static String translate(String message) {
        return ChatColor.translateAlternateColorCodes('&',
                LegacyComponentSerializer.legacyAmpersand().serialize(
                        MiniMessage.miniMessage().deserialize(message)
                ));
    }

    public static void send(Player player, String message) {
        if (message.isEmpty() || player == null) return;

        Bubble.getInstance().getAdventure().player(player)
                .sendMessage(MiniMessage.miniMessage().deserialize(message.replace("badge%", badge)));
    }

    public static void send(Player player, String message, String permission) {
        if (message.isEmpty() || player == null) return;

        if ((player.hasPermission(permission) ||
                Arrays.stream(permission.split("\\|")).anyMatch(player::hasPermission)))
            send(player, message);
    }

    public static void send(CommandSender sender, String message) {
        if (sender instanceof Player) {
            send((Player) sender, message);
            return;
        }

        message = message.replace("<newline>", "\n");
        sender.sendMessage(MiniMessage.miniMessage().stripTags(message));
    }

}
