package com.mizuledevelopment.zhub.util.command;

import org.bukkit.command.CommandSender;

public abstract class Command {

    public abstract void execute(CommandSender sender, String[] args);
    public abstract String getName();
    public abstract boolean allow();
    public abstract String getUsage();
}