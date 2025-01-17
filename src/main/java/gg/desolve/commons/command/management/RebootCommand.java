package gg.desolve.commons.command.management;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import gg.desolve.commons.Commons;
import gg.desolve.commons.reboot.Reboot;
import gg.desolve.commons.relevance.Converter;
import gg.desolve.commons.relevance.Duration;
import gg.desolve.commons.relevance.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("reboot")
public class RebootCommand extends BaseCommand {

    @HelpCommand
    @CommandPermission("commons.command.reboot|commons.command.reboot.help")
    @Syntax("[page]")
    public static void onHelp(CommandHelp help) {
        help.setPerPage(6);
        help.showHelp();
    }

    @Subcommand("status")
    @CommandPermission("commons.command.reboot|commons.command.reboot.status")
    @Description("Get current reboot status")
    public static void onStatus(CommandSender sender) {
        Reboot reboot = Commons.getInstance().getRebootManager().getReboot();

        if (reboot.isStatus()) {
            Message.send(sender, "prefix% <green>A reboot is scheduled to initialise <gray>in remaining%."
                    .replace("remaining%", Converter.time((reboot.getAddedAt() + reboot.getDelay()) - System.currentTimeMillis())));
            return;
        }

        Message.send(sender, "prefix% <red>A reboot is not scheduled.");
    }

    @Subcommand("invalidate")
    @CommandPermission("commons.command.reboot|commons.command.reboot.invalidate")
    @Description("Invalidate current reboot")
    public static void onInvalidate(CommandSender sender) {
        if (!Commons.getInstance().getRebootManager().getReboot().isStatus()) {
            Message.send(sender, "prefix% <red>No reboot is running momentarily.");
            return;
        }

        Commons.getInstance().getRebootManager().cancel();
        Commons.getInstance().getRebootManager().setReboot(
                new Reboot(
                        null,
                        0L,
                        sender instanceof Player ? ((Player) sender).getUniqueId() : null,
                        System.currentTimeMillis(),
                        0L,
                        false
                ));

        Message.send(sender, "prefix% <green>Removed current reboot.");
    }

    @Subcommand("initialise")
    @CommandCompletion("@durations")
    @CommandPermission("commons.command.reboot|commons.command.reboot.initialise")
    @Syntax("<duration>")
    @Description("Initialise a new reboot")
    public static void onInitialise(CommandSender sender, Duration duration) {
        if (Commons.getInstance().getRebootManager().getReboot().isStatus()) {
            Message.send(sender, "prefix% <red>A reboot is currently initialised, remove it before initialising a new one.");
            return;
        }

        Reboot reboot = new Reboot(
                sender instanceof Player ? ((Player) sender).getUniqueId() : null,
                System.currentTimeMillis(),
                null,
                0L,
                duration.getDuration(),
                true
        );

        Commons.getInstance().getRebootManager().setReboot(reboot);
        Commons.getInstance().getRebootManager().start();

        Message.send(sender, "prefix% <green>Initialised a new reboot <gray>for remaining%."
                .replace("remaining%", Converter.time((reboot.getAddedAt() + reboot.getDelay()) - System.currentTimeMillis())));

    }

}
