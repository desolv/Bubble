package gg.desolve.commons.command.management;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import gg.desolve.commons.Commons;
import gg.desolve.commons.relevance.Converter;
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

    @Subcommand("retrieve")
    @CommandPermission("commons.command.instance|commons.command.instance.retrieve")
    @Description("Retrieve all instances")
    public static void onRetrieve(CommandSender sender) {
        List<String> instances = Commons.getInstance()
                .getInstanceManager()
                .retrieve()
                .stream()
                .map(instance -> {
                    String hoverText = ("<hover:show_text:'<dark_gray>#id%" +
                            "<newline><green>version%" +
                            "<newline><red>Heartbeat of heartbeat% seconds" +
                            "<newline><yellow>Currently online% online players" +
                            "<newline><white>Click to reload instances'>")
                            .replace("version%", instance.getVersion())
                            .replace("heartbeat%", String.valueOf(Converter.seconds(System.currentTimeMillis() - instance.getHeartbeat())))
                            .replace("online%", String.valueOf(instance.getOnline()))
                            .replace("id%", instance.getId());

                    return "<click:run_command:/instance retrieve>" + hoverText + "<aqua>@" + instance.getName();
                })
                .collect(Collectors.toList());

        Message.send(sender, "prefix% Currently hosting " + instances.size() + " instances: " + String.join("<white>, <white>", instances));
    }



}
