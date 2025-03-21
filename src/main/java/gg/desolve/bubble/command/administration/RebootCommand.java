package gg.desolve.bubble.command.administration;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import gg.desolve.bubble.Bubble;
import gg.desolve.bubble.config.Config;
import gg.desolve.bubble.reboot.Reboot;
import gg.desolve.bubble.relevance.Converter;
import gg.desolve.bubble.relevance.Duration;
import gg.desolve.bubble.relevance.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("reboot")
public class RebootCommand extends BaseCommand {

    @HelpCommand
    @CommandPermission("bubble.command.reboot|bubble.command.reboot.help")
    @Syntax("[page]")
    public static void onHelp(CommandHelp help) {
        help.setPerPage(6);
        help.showHelp();
    }

    @Subcommand("status")
    @CommandPermission("bubble.command.reboot|bubble.command.reboot.status")
    @Description("Get current reboot status")
    public static void onStatus(CommandSender sender) {
        Config languageConfig = Bubble.getInstance().getLanguageConfig();
        Reboot reboot = Bubble.getInstance().getRebootManager().getReboot();

        if (reboot.isStatus()) {
            Message.send(sender, "prefix% <green>A reboot is scheduled to initialise <gray>in remaining%."
                    .replace("remaining%",
                            Converter.time((reboot.getAddedAt() + reboot.getDelay()) - System.currentTimeMillis())));
            return;
        }

        Message.send(sender, "prefix% <red>A reboot is not scheduled it was removed <gray>by username%."
                        .replace("username%", reboot.getRemovedBy() != null ? String.valueOf(reboot.getRemovedBy()) : "Console" + "."));
    }

    @Subcommand("invalidate")
    @CommandPermission("bubble.command.reboot|bubble.command.reboot.invalidate")
    @Description("Invalidate current reboot")
    public static void onInvalidate(CommandSender sender) {
        Config languageConfig = Bubble.getInstance().getLanguageConfig();

        if (!Bubble.getInstance().getRebootManager().getReboot().isStatus()) {
            Message.send(sender, "prefix% <red>No reboot is running momentarily.");
            return;
        }

        Bubble.getInstance().getRebootManager().cancel();
        Bubble.getInstance().getRebootManager().setReboot(
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
    @CommandPermission("bubble.command.reboot|bubble.command.reboot.initialise")
    @Syntax("(duration)")
    @Description("Initialise a new reboot")
    public static void onInitialise(CommandSender sender, Duration duration) {
        Config languageConfig = Bubble.getInstance().getLanguageConfig();

        if (Bubble.getInstance().getRebootManager().getReboot().isStatus()) {
            Message.send(sender, "prefix% <red>A reboot is currently initialised, remove it before initialising a new one.");
            return;
        }

        Reboot reboot = new Reboot(
                sender instanceof Player ? ((Player) sender).getUniqueId() : null,
                System.currentTimeMillis(),
                null,
                0L,
                duration.duration(),
                true
        );

        Bubble.getInstance().getRebootManager().setReboot(reboot);
        Bubble.getInstance().getRebootManager().start();

        Message.send(sender, "prefix% <green>Initialised a new reboot <gray>for remaining%."
                .replace("remaining%",
                        Converter.time((reboot.getAddedAt() + reboot.getDelay()) - System.currentTimeMillis())));

    }
}
