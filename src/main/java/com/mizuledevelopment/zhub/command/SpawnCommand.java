package com.mizuledevelopment.zhub.command;

import cloud.commandframework.CommandManager;
import cloud.commandframework.meta.CommandMeta;
import com.mizuledevelopment.zhub.config.impl.ConfigFile;
import com.mizuledevelopment.zhub.util.color.Color;
import com.mizuledevelopment.zhub.util.location.LazyLocation;
import com.mizuledevelopment.zhub.util.location.LocationUtil;
import com.mizuledevelopment.zhub.zHub;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

public class SpawnCommand extends BaseCommand {

    private final ConfigFile config;
    private final ConfigFile messages;

    public SpawnCommand(final zHub plugin) {
        this.config = plugin.config("config");
        this.messages = plugin.config("messages");
    }

    @Override
    public void register(final CommandManager<CommandSender> commandManager) {
        commandManager.command(commandManager.commandBuilder("spawn")
            .permission("zhub.command.spawn")
            .meta(CommandMeta.DESCRIPTION, "Teleport to spawn")
            .senderType(Player.class)
            .handler(context -> {
                final Player player = (Player) context.getSender();
                final ConfigurationNode section = this.config.section("spawn.location");
                final LazyLocation location = LocationUtil.parse(section);

                if (location.isPresent()) {
                    player.teleportAsync(location.location());
                    player.sendMessage(Color.translate(this.messages.getString("messages.spawn", "spawn")));
                } else {
                    player.sendMessage(Color.translate(this.messages.getString("messages.no-spawn", "no spawn")));
                }
            })
        );
    }
}
