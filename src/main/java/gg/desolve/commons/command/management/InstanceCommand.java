package gg.desolve.commons.command.management;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import gg.desolve.commons.Commons;
import gg.desolve.commons.instance.Instance;
import gg.desolve.commons.inventory.instance.InstanceInventory;
import gg.desolve.commons.relevance.Converter;
import gg.desolve.commons.relevance.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("instance")
public class InstanceCommand extends BaseCommand {

    @HelpCommand
    @CommandPermission("commons.command.instance|commons.command.instance.help")
    @Syntax("[page]")
    public static void onHelp(CommandHelp help) {
        help.setPerPage(6);
        help.showHelp();
    }

    @Subcommand("simple")
    @CommandPermission("commons.command.instance|commons.command.instance.simple")
    @Description("Retrieve instance information")
    public static void onSimple(CommandSender sender) {
        Instance instance = Commons.getInstance().getInstanceManager().getInstance();
        Message.send(sender,
                "<newline><aqua><bold>@" + Commons.getInstance().getLanguageConfig().getString("server.server_name") + "'s Instance information</bold>" +
                "<newline><white>Id: <dark_gray>" + instance.getId() +
                "<newline><white>Version: <aqua>" + instance.getVersion() +
                "<newline><white>Booting: <aqua>" + Converter.time(System.currentTimeMillis() - instance.getBooting()) +
                "<newline><white>Online: <aqua>" + (instance.getOnline() <= 0 ? "<red>None" : instance.getOnline()) );
    }

    @Subcommand("retrieve")
    @CommandPermission("commons.command.instance|commons.command.instance.retrieve")
    @Description("Retrieve instances on GUI")
    public static void onRetrieve(Player player) {
        InstanceInventory.INVENTORY.open(player);
    }
}
