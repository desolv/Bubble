package gg.desolve.mithril.command.management;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import gg.desolve.mithril.Mithril;
import gg.desolve.mithril.instance.Instance;
import gg.desolve.mithril.inventory.instance.InstanceInventory;
import gg.desolve.mithril.relevance.Converter;
import gg.desolve.mithril.relevance.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("instance")
public class InstanceCommand extends BaseCommand {

    @HelpCommand
    @CommandPermission("mithril.command.instance|mithril.command.instance.help")
    @Syntax("[page]")
    public static void onHelp(CommandHelp help) {
        help.setPerPage(6);
        help.showHelp();
    }

    @Subcommand("simple")
    @CommandPermission("mithril.command.instance|mithril.command.instance.simple")
    @Description("Retrieve instance information")
    public static void onSimple(CommandSender sender) {
        Instance instance = Mithril.getInstance().getInstanceManager().getInstance();
        Message.send(sender,
                "<newline><aqua><bold>@" + Mithril.getInstance().getLanguageConfig().getString("server.server_name") + "'s Instance information</bold>" +
                "<newline><white>Id: <dark_gray>" + instance.getId() +
                "<newline><white>Version: <aqua>" + instance.getVersion() +
                "<newline><white>Booting: <aqua>" + Converter.time(System.currentTimeMillis() - instance.getBooting()) +
                "<newline><white>Online: <aqua>" + (instance.getOnline() <= 0 ? "<red>None" : instance.getOnline()) );
    }

    @Subcommand("retrieve")
    @CommandPermission("mithril.command.instance|mithril.command.instance.retrieve")
    @Description("Manage instances on GUI")
    public static void onRetrieve(Player player) {
        InstanceInventory.INVENTORY.open(player);
    }
}
