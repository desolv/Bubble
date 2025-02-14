package gg.desolve.mithril.command;

import co.aikar.commands.CommandIssuer;
import co.aikar.commands.Locales;
import co.aikar.commands.PaperCommandManager;
import gg.desolve.mithril.Mithril;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class CommandManager extends PaperCommandManager {

    private final String permission;

    public CommandManager(String permission) {
        super(Mithril.getInstance());
        this.permission = permission;

        try {
            enableUnstableAPI("help");
            getLocales().loadYamlLanguageFile("language.yml", Locales.ENGLISH);
        } catch (Exception e) {
            Mithril.getInstance().getLogger().warning("An error occurred while initialising commands manager usages.");
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
