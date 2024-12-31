package gg.desolve.commons.command;

import co.aikar.commands.*;
import gg.desolve.commons.Commons;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;

public class CommandManager extends PaperCommandManager {

    private final String permission;

    public CommandManager(Plugin plugin, String permission, String location) {
        super(plugin);
        this.permission = permission;

        try {
            enableUnstableAPI("help");
            getLocales().loadYamlLanguageFile(location, Locales.ENGLISH);
        } catch (Exception e) {
            Commons.getInstance().getLogger().warning("An error occurred while initialising commands manager usages.");
            e.printStackTrace();
        }
    }

    @Override
    public boolean hasPermission(CommandIssuer issuer, String permission) {
        CommandSender sender = issuer.getIssuer();

        return sender instanceof ConsoleCommandSender ||
                sender instanceof Player && (issuer.hasPermission(this.permission) ||
                        Arrays.stream(permission.split("\\|")).anyMatch(issuer::hasPermission));
    }

}
