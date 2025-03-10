package gg.desolve.mithril.command.administration;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import gg.desolve.mithril.Mithril;
import gg.desolve.mithril.config.Config;
import gg.desolve.mithril.reboot.Reboot;
import gg.desolve.mithril.relevance.Converter;
import gg.desolve.mithril.relevance.Duration;
import gg.desolve.mithril.relevance.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("reboot")
public class RebootCommand extends BaseCommand {

    @HelpCommand
    @CommandPermission("mithril.command.reboot|mithril.command.reboot.help")
    @Syntax("[page]")
    public static void onHelp(CommandHelp help) {
        help.setPerPage(6);
        help.showHelp();
    }

    @Subcommand("status")
    @CommandPermission("mithril.command.reboot|mithril.command.reboot.status")
    @Description("Get current reboot status")
    public static void onStatus(CommandSender sender) {
        Config languageConfig = Mithril.getInstance().getLanguageConfig();
        Reboot reboot = Mithril.getInstance().getRebootManager().getReboot();

        if (reboot.isStatus()) {
            Message.send(sender, languageConfig.getString("reboot-command.status.scheduled")
                    .replace("remaining%",
                            Converter.time((reboot.getAddedAt() + reboot.getDelay()) - System.currentTimeMillis())));
            return;
        }

        Message.send(sender, languageConfig.getString("reboot-command.status.unscheduled")
                        .replace("username%", reboot.getRemovedBy() != null ? String.valueOf(reboot.getRemovedBy()) : "Console" + "."));
    }

    @Subcommand("invalidate")
    @CommandPermission("mithril.command.reboot|mithril.command.reboot.invalidate")
    @Description("Invalidate current reboot")
    public static void onInvalidate(CommandSender sender) {
        Config languageConfig = Mithril.getInstance().getLanguageConfig();

        if (!Mithril.getInstance().getRebootManager().getReboot().isStatus()) {
            Message.send(sender, languageConfig.getString("reboot-command.invalidate.unscheduled"));
            return;
        }

        Mithril.getInstance().getRebootManager().cancel();
        Mithril.getInstance().getRebootManager().setReboot(
                new Reboot(
                        null,
                        0L,
                        sender instanceof Player ? ((Player) sender).getUniqueId() : null,
                        System.currentTimeMillis(),
                        0L,
                        false
                ));

        Message.send(sender, languageConfig.getString("reboot-command.invalidate.removed"));
    }

    @Subcommand("initialise")
    @CommandCompletion("@durations")
    @CommandPermission("mithril.command.reboot|mithril.command.reboot.initialise")
    @Syntax("(duration)")
    @Description("Initialise a new reboot")
    public static void onInitialise(CommandSender sender, Duration duration) {
        Config languageConfig = Mithril.getInstance().getLanguageConfig();

        if (Mithril.getInstance().getRebootManager().getReboot().isStatus()) {
            Message.send(sender, languageConfig.getString("reboot-command.initialise.scheduled"));
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

        Mithril.getInstance().getRebootManager().setReboot(reboot);
        Mithril.getInstance().getRebootManager().start();

        Message.send(sender, languageConfig.getString("reboot-command.initialise.created")
                .replace("remaining%",
                        Converter.time((reboot.getAddedAt() + reboot.getDelay()) - System.currentTimeMillis())));

    }
}
