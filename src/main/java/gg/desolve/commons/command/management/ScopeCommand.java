package gg.desolve.commons.command.management;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import gg.desolve.commons.Commons;
import gg.desolve.commons.instance.Instance;
import gg.desolve.commons.relevance.Message;
import gg.desolve.commons.scope.Scope;
import org.bukkit.command.CommandSender;

@CommandAlias("scope")
public class ScopeCommand extends BaseCommand {

    @HelpCommand
    @CommandPermission("commons.command.scope|commons.command.scope.help")
    @Syntax("[page]")
    public static void onHelp(CommandHelp help) {
        help.setPerPage(6);
        help.showHelp();
    }

    @Subcommand("create")
    @CommandPermission("commons.command.scope|commons.command.scope.create")
    @Syntax("<name>")
    @Description("Create new scope of instances")
    public static void onCreate(CommandSender sender, String name) {
        if (name.equalsIgnoreCase("global")) {
            Message.send(sender, "<red>Unable to create scope named <yellow>global<green>.");
            return;
        }

        if (Commons.getInstance().getScopeManager().retrieve(name) != null) {
            Message.send(sender, "<red>Scope named <yellow>" + name + " <red>is present.");
            return;
        }

        Commons.getInstance().getScopeManager().create(name);
        Message.send(sender, "<green>Create new scope named <yellow>" + name + "<green>.");
    }

    @Subcommand("delete")
    @CommandCompletion("@scopes")
    @CommandPermission("commons.command.scope|commons.command.scope.delete")
    @Syntax("<scope>")
    @Description("Delete scope of instances")
    public static void onDelete(CommandSender sender, Scope scope) {
        Commons.getInstance().getScopeManager().delete(scope);
        Message.send(sender, "<green>Deleted scope named <yellow>scope% <green>with <gray>instances% <green>instances."
                .replace("scope%", scope.getName())
                .replace("instances%", String.valueOf(scope.getInstances().size())));
    }

    @Subcommand("add")
    @CommandCompletion("@scopes @instances")
    @CommandPermission("commons.command.scope|commons.command.scope.add")
    @Syntax("<scope> <instance>")
    @Description("Add an instance for scope")
    public static void onAdd(CommandSender sender, Scope scope, Instance instance) {
        if (scope.getInstances().contains(instance.getName())) {
            Message.send(sender, "<red>Instance <yellow>instance% <red>is present for <yellow>scope% <red>scope."
                    .replace("instance%", instance.getName())
                    .replace("scope%", scope.getName()));
            return;
        }

        Commons.getInstance().getScopeManager().add(scope, instance.getName());
        Message.send(sender, "<green>Added instance <yellow>instance% <green>for <yellow>scope% <green>scope."
                .replace("instance%", instance.getName())
                .replace("scope%", scope.getName()));
    }

    @Subcommand("remove")
    @CommandCompletion("@scopes @instances")
    @CommandPermission("commons.command.scope|commons.command.scope.remove")
    @Syntax("<scope> <instance>")
    @Description("Remove an instance from scope")
    public static void onRemove(CommandSender sender, Scope scope, Instance instance) {
        if (!scope.getInstances().contains(instance.getName())) {
            Message.send(sender, "<red>Instance <yellow>instance% <red>is not present for <yellow>scope% <red>scope."
                    .replace("instance%", instance.getName())
                    .replace("scope%", scope.getName()));
            return;
        }

        Commons.getInstance().getScopeManager().remove(scope, instance.getName());
        Message.send(sender, "<green>Removed instance <yellow>instance% <green>from <yellow>scope% <green>scope."
                .replace("instance%", instance.getName())
                .replace("scope%", scope.getName()));
    }
}
