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
