package gg.desolve.commons.command.management;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import gg.desolve.commons.Commons;
import gg.desolve.commons.relevance.Converter;
import gg.desolve.commons.instance.Instance;
import gg.desolve.commons.relevance.Message;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.stream.Collectors;

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
                "<newline><white>Online: <aqua>" + instance.getOnline());
    }

    @Subcommand("retrieve")
    @CommandPermission("commons.command.instance|commons.command.instance.retrieve")
    @Description("Retrieve all instances")
    public static void onRetrieve(CommandSender sender) {
        List<String> instances = Commons.getInstance().getInstanceManager().retrieve()
                .stream()
                .map(instance -> "<click:run_command:/instance retrieve>" +
                        ("<hover:show_text:'<dark_gray>#id%" + "<newline><green>version%" +
                        "<newline><red>Heartbeat of heartbeat% seconds" + "<newline><yellow>Currently online% online players'>")
                            .replace("version%", instance.getVersion())
                            .replace("heartbeat%", String.valueOf(Converter.seconds(System.currentTimeMillis() - instance.getHeartbeat())))
                            .replace("online%", String.valueOf(instance.getOnline()))
                            .replace("id%", instance.getId()) + "<aqua>@" + instance.getName())
                .collect(Collectors.toList());

        Message.send(sender, "prefix% Currently hosting " + instances.size() + " instances: " + String.join("<white>, <white>", instances));
    }



}
