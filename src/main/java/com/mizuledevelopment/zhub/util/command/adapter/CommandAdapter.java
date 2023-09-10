package com.mizuledevelopment.zhub.util.command.adapter;


import com.mizuledevelopment.zhub.util.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CommandAdapter implements TabExecutor {

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

    @Override
    public @Nullable List<String> onTabComplete(
        @NotNull
        final CommandSender sender, final org.bukkit.command.@NotNull Command command,
        @NotNull
        final String label,
        final @NotNull String[] args
    ) {
        if (args.length == 0) {
            return this.commands.stream().map(Command::getName).toList();
        }

        return List.of();
    }
}
