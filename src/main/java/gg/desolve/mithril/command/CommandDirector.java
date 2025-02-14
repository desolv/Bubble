package gg.desolve.mithril.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.InvalidCommandArgument;
import gg.desolve.mithril.Mithril;
import gg.desolve.mithril.command.administration.RebootCommand;
import gg.desolve.mithril.command.management.InstanceCommand;
import gg.desolve.mithril.command.management.ScopeCommand;
import gg.desolve.mithril.instance.Instance;
import gg.desolve.mithril.relevance.Duration;
import gg.desolve.mithril.relevance.Message;
import gg.desolve.mithril.scope.Scope;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public class CommandDirector {

    private final CommandManager commandManager;
    private final List<BaseCommand> commands;

    public CommandDirector(CommandManager commandManager) {
        this.commandManager = commandManager;
        contexts();
        completions();
        commands = commands();
    }

    private List<BaseCommand> commands() {
        List<BaseCommand> commandList = Arrays.asList(
                new InstanceCommand(),
                new ScopeCommand(),
                new RebootCommand()
        );

        commandList.forEach(commandManager::registerCommand);
        return commandList;
    }

    private void contexts() {
        commandManager.getCommandContexts().registerContext(
                Scope.class, s -> {
                    String popName = s.popFirstArg();
                    Scope scope = Mithril.getInstance().getScopeManager().retrieve(popName);
                    return Optional.ofNullable(scope).orElseThrow(() ->
                            new InvalidCommandArgument(Message.translate("<red>Scope matching <yellow>" + popName + " <red>not found."), false));
                });

        commandManager.getCommandContexts().registerContext(
                Instance.class, i -> {
                    String popName = i.popFirstArg();
                    Instance instance = Mithril.getInstance().getInstanceManager().retrieve(popName);
                    return Optional.ofNullable(instance).orElseThrow(() ->
                            new InvalidCommandArgument(Message.translate("<red>Instance matching <yellow>" + popName + " <red>not found."), false));
                });

        commandManager.getCommandContexts().registerContext(
                Duration.class, d -> {
                    String popName = d.popFirstArg();
                    Duration duration = Duration.duration(popName);
                    return Optional.ofNullable(duration).orElseThrow(() ->
                            new InvalidCommandArgument(Message.translate("<red>Duration matching <yellow>" + popName + " <red>not found."), false));
                });
    }


    private void completions() {
        commandManager.getCommandCompletions().registerAsyncCompletion("scopes", s ->
                Stream.concat(
                        Stream.of("global"),
                        Mithril.getInstance().getScopeManager().retrieve().stream().map(Scope::getName)
                ).collect(Collectors.toList()));

        commandManager.getCommandCompletions().registerAsyncCompletion("instances", s ->
                Mithril.getInstance().getInstanceManager().retrieve().stream().map(Instance::getName).toList());

        commandManager.getCommandCompletions().registerAsyncCompletion("durations", s ->
                Mithril.getInstance().getLanguageConfig().getConfig().getStringList("durations"));
    }
}

