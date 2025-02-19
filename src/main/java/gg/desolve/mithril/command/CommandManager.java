package gg.desolve.mithril.command;

import co.aikar.commands.CommandIssuer;
import co.aikar.commands.Locales;
import co.aikar.commands.PaperCommandManager;
import gg.desolve.mithril.Mithril;
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

        try {
            enableUnstableAPI("help");
            getLocales().loadYamlLanguageFile("language.yml", Locales.ENGLISH);
        } catch (Exception e) {
            plugin.getLogger().warning("An error occurred while initialising commands manager usages.");
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
