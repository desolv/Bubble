package gg.desolve.commons.command;

import co.aikar.commands.CommandIssuer;
import co.aikar.commands.PaperCommandManager;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;

public class CommandManager extends PaperCommandManager {

    private final String permission;

    public CommandManager(Plugin plugin, String permission) {
        super(plugin);
        this.permission = permission;
    }

    @Override
    public boolean hasPermission(CommandIssuer issuer, String permission) {
        CommandSender sender = issuer.getIssuer();

        return sender instanceof ConsoleCommandSender ||
                sender instanceof Player && (issuer.hasPermission(this.permission) ||
                        Arrays.stream(permission.split("\\|")).anyMatch(issuer::hasPermission));
    }

}
