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

import com.mizuledevelopment.zhub.util.scoreboard.FastBoard;
import com.mizuledevelopment.zhub.util.scoreboard.ScoreboardAdapter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class ScoreboardHandler {

    public ScoreboardHandler() {
        new ScoreboardThread().start();
    }

    public static final Map<UUID, FastBoard> scoreboards = new ConcurrentHashMap<>();
    private static ScoreboardAdapter adapter = new DefaultScoreboardAdapter();

    public static void configure(final ScoreboardAdapter adapter) {
        ScoreboardHandler.adapter = adapter;
    }

    public static ScoreboardAdapter adapter() {
        return adapter;
    }

    public static Map<UUID, FastBoard> getScoreboards() {
        return scoreboards;
    }

    static class DefaultScoreboardAdapter implements ScoreboardAdapter {
        @Override
        public Component getTitle(final @NotNull Player player) {
            return Component.text("Un-configured scoreboard", TextColor.color(0x9C79FF));
        }

        @Override
        public void getLines(final @NotNull LinkedList<? super Component> lines, final @NotNull Player player) {
            lines.add(Component.text("Hey there,", TextColor.color(0xB9B2FF))
                    .append(Component.space())
                    .append(player.displayName())
                    .append(Component.text("!", TextColor.color(0xB9B2FF)))
            );
        }
    }
}
