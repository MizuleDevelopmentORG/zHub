package com.mizuledevelopment.zhub.util.command.manager;

import com.mizuledevelopment.zhub.util.command.Command;
import com.mizuledevelopment.zhub.util.command.adapter.CommandAdapter;
import org.bukkit.command.PluginCommand;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {

    private final PluginCommand pluginCommand;
    private final List<Command> commands = new ArrayList<>();

    public CommandManager(final PluginCommand pluginCommand) {
        this.pluginCommand = pluginCommand;
    }

    public void addSubCommand(final Command command) {
        this.commands.add(command);
    }

    public void setCommand(final List<Command> commandList) {
        this.commands.addAll(commandList);
    }

    public void registerCommands() {
        this.pluginCommand.setExecutor(new CommandAdapter(this.commands));
    }

    public List<Command> getRegisteredSubCommands() {
        return this.commands;
    }
}