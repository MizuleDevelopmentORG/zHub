package com.mizuledevelopment.zhub.command;

import cloud.commandframework.CommandManager;
import org.bukkit.command.CommandSender;

/**
 * A command, registered with Cloud's {@link CommandManager} and operates on a {@link org.bukkit.command.CommandSender}
 * sender.
 */
public abstract class BaseCommand {

    /**
     * Register this command with the {@link CommandManager}.
     *
     * @param commandManager The manager to register commands with. This assumes the manager
     *                       supports {@link CommandSender}s as a sender.
     */
    public abstract void register(final CommandManager<CommandSender> commandManager);
}
