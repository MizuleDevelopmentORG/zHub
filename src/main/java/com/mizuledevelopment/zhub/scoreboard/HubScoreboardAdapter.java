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
package com.mizuledevelopment.zhub.scoreboard;

import com.mizuledevelopment.zhub.config.impl.ConfigFile;
import com.mizuledevelopment.zhub.util.scoreboard.ScoreboardAdapter;
import com.mizuledevelopment.zhub.util.text.MessageType;
import com.mizuledevelopment.zhub.util.text.TextUtil;
import com.mizuledevelopment.zhub.zHub;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class HubScoreboardAdapter implements ScoreboardAdapter {

    final ConfigFile config;

    public HubScoreboardAdapter(final zHub plugin) {
        this.config = plugin.config("scoreboard");
    }

    @Override
    public Component getTitle(final @NotNull Player player) {
        final String title = this.config.getString("title", "<red>Title");
        return TextUtil.parse(
                title,
                MessageType.from(title),
                player,
                Placeholder.component("player", player.name())
        );
    }

    @Override
    public void getLines(final @NotNull LinkedList<? super Component> lines, final @NotNull Player player) {
        final List<String> configLines = this.config.getStringList("lines", new ArrayList<>());
        for (final String line : configLines) {
            lines.add(TextUtil.parse(
                    line,
                    MessageType.from(line),
                    player,
                    Placeholder.component("player", player.name())
            ));
        }
    }
}
