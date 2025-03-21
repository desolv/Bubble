package gg.desolve.bubble.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.InvalidCommandArgument;
import gg.desolve.bubble.Bubble;
import gg.desolve.bubble.command.administration.RebootCommand;
import gg.desolve.bubble.command.management.InstanceCommand;
import gg.desolve.bubble.instance.Instance;
import gg.desolve.bubble.relevance.Duration;
import gg.desolve.bubble.relevance.Message;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
                new RebootCommand()
        );

        commandList.forEach(commandManager::registerCommand);
        return commandList;
    }

    private void contexts() {
        commandManager.getCommandContexts().registerContext(
                Instance.class, i -> {
                    String popName = i.popFirstArg();
                    Instance instance = Bubble.getInstance().getInstanceManager().retrieve(popName);
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
        commandManager.getCommandCompletions().registerAsyncCompletion("instances", s ->
                Bubble.getInstance().getInstanceManager().retrieve().stream().map(Instance::getName).toList());

        commandManager.getCommandCompletions().registerAsyncCompletion("durations", s ->
                        Arrays.asList(Bubble.getInstance().getLanguageConfig().getString("server.timing").split("\\|")));
    }
}

