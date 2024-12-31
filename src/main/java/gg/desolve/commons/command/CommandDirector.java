package gg.desolve.commons.command;

import co.aikar.commands.BaseCommand;
import gg.desolve.commons.command.management.InstanceCommand;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public class CommandDirector {

    private final CommandManager commandManager;
    private final List<BaseCommand> commands;

    public CommandDirector(CommandManager commandManager) {
        this.commandManager = commandManager;
        commands = commands();
    }

    private List<BaseCommand> commands() {
        List<BaseCommand> commandList = Arrays.asList(
                new InstanceCommand()
        );

        commandList.forEach(commandManager::registerCommand);
        return commandList;
    }

}

