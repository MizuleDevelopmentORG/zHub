package com.mizuledevelopment.zhub.util.command.adapter;


import com.mizuledevelopment.zhub.util.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommandAdapter implements CommandExecutor {

    private final List<Command> commands;

    public CommandAdapter(final List<Command> commands) {
        this.commands = commands;
    }

    @Override
    public boolean onCommand(
            final @NotNull CommandSender commandSender, final org.bukkit.command.@NotNull Command command,
            final @NotNull String s,
            final @NotNull String[] args
    ) {

        if (args.length > 0) {
            for (final Command cmd : this.commands) {
                if (args[0].equalsIgnoreCase(cmd.getName())) {
                    cmd.execute(commandSender, args);
                }
            }
        } else {
            for (final Command cmd : this.commands) {
                if (cmd.allow()) {
                    commandSender.sendMessage(cmd.getUsage());
                }
            }
        }

        return true;
    }
}