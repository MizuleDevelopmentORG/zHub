package com.mizuledevelopment.zhub.command;

import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import cloud.commandframework.meta.CommandMeta;
import org.bukkit.command.CommandSender;

public class zHubCommand extends BaseCommand {

    @Override
    public void register(final CommandManager<CommandSender> commandManager) {
        final Command.Builder<CommandSender> builder = commandManager.commandBuilder("zhub");

        commandManager.command(builder
            .literal("reload")
            .permission("zhub.reload")
            .meta(CommandMeta.DESCRIPTION, "Reload the plugin")
            .handler(context -> {
                context.getSender().sendMessage("Reloaded zHub!");
            })
        );

    }
}
