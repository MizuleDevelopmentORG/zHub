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
