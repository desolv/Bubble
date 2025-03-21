package gg.desolve.bubble.command.management;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import gg.desolve.bubble.Bubble;
import gg.desolve.bubble.instance.Instance;
import gg.desolve.bubble.inventory.instance.InstanceInventory;
import gg.desolve.bubble.relevance.Converter;
import gg.desolve.bubble.relevance.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("instance")
public class InstanceCommand extends BaseCommand {

    @HelpCommand
    @CommandPermission("bubble.command.instance|bubble.command.instance.help")
    @Syntax("[page]")
    public static void onHelp(CommandHelp help) {
        help.setPerPage(6);
        help.showHelp();
    }

    @Subcommand("simple")
    @CommandPermission("bubble.command.instance|bubble.command.instance.simple")
    @Description("Retrieve instance information")
    public static void onSimple(CommandSender sender) {
        Instance instance = Bubble.getInstance().getInstanceManager().getInstance();
        Message.send(sender,
                "<newline><aqua><bold>@" + Bubble.getInstance().getLanguageConfig().getString("server.name") + "'s Instance information</bold>" +
                "<newline><white>Id: <dark_gray>" + instance.getId() +
                "<newline><white>Version: <aqua>" + instance.getVersion() +
                "<newline><white>Booted: <aqua>" + Converter.time(System.currentTimeMillis() - instance.getBooting()) + " ago" +
                "<newline><white>Online: <aqua>" + (instance.getOnline() <= 0 ? "<red>None" : instance.getOnline()) );
    }

    @Subcommand("retrieve")
    @CommandPermission("bubble.command.instance|bubble.command.instance.retrieve")
    @Description("Manage instances on GUI")
    public static void onRetrieve(Player player) {
        InstanceInventory.INVENTORY.open(player);
    }
}
