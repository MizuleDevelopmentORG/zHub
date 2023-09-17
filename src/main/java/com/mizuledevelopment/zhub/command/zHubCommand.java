/*
 * This file is part of zHub, licensed under the GPLv3 License.
 *
 * Copyright (c) 2023 Mizule Development
 * Copyright (c) 2023 contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mizuledevelopment.zhub.command;

import cloud.commandframework.Command;
import cloud.commandframework.CommandHelpHandler;
import cloud.commandframework.CommandManager;
import cloud.commandframework.arguments.CommandArgument;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.context.CommandContext;
import cloud.commandframework.meta.CommandMeta;
import com.mizuledevelopment.zhub.config.impl.ConfigFile;
import com.mizuledevelopment.zhub.zHub;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.function.BiFunction;

public class zHubCommand extends BaseCommand {

    private final CommandArgument<CommandSender, String> helpQueryArgument;

    public zHubCommand() {
        this.helpQueryArgument = createHelpQueryArgument(zHub.instance());
    }

    private static CommandArgument<CommandSender, String> createHelpQueryArgument(final zHub athena) {
        final var commandHelpHandler = athena.commandManager().createCommandHelpHandler();
        final BiFunction<CommandContext<CommandSender>, String, List<String>> suggestions = (context, input) ->
            commandHelpHandler.queryRootIndex(context.getSender()).getEntries().stream()
                .map(CommandHelpHandler.VerboseHelpEntry::getSyntaxString)
                .toList();
        return StringArgument.<CommandSender>builder("query")
            .greedy()
            .withSuggestionsProvider(suggestions)
            .asOptional()
            .build();
    }

    @Override
    public void register(final CommandManager<CommandSender> commandManager) {
        final Command.Builder<CommandSender> builder = commandManager.commandBuilder("zhub");

        commandManager.command(builder
            .meta(CommandMeta.DESCRIPTION, "zHub command")
            .handler(ctx -> {
                ctx.getSender().sendMessage("zHub v" + zHub.instance().getDescription().getVersion());
            }));


        commandManager.command(builder
            .literal("reload")
            .permission("zhub.command.reload")
            .meta(CommandMeta.DESCRIPTION, "Reload the plugin")
            .handler(context -> {
                zHub.instance().configs().forEach(ConfigFile::reload);
                context.getSender().sendMessage("Reloaded zHub!");
            })
        );
        commandManager.command(builder
            .literal("help")
            .permission("zhub.command.help")
            .meta(CommandMeta.DESCRIPTION, "Help command")
            .argument(this.helpQueryArgument)
            .handler(ctx -> {
                final CommandSender sender = ctx.getSender();
                zHub.instance().minecraftHelp().queryCommands(ctx.getOptional(this.helpQueryArgument).orElse(""), sender);
            })
        );


    }
}
