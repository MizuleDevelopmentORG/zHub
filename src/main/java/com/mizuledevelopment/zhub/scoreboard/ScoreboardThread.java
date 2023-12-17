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
import com.mizuledevelopment.zhub.zHub;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

// TODO: MAKE THIS A RUNNABLE THREAD THING
public final class ScoreboardThread extends Thread {

    public ScoreboardThread() {
        super("zHub - Scoreboard Thread");
        this.setDaemon(true);
    }

    @Override
    public void run() {
        while (zHub.instance() != null && zHub.instance().isEnabled()) {
            for (final Player online : Bukkit.getOnlinePlayers()) {
                try {
                    final FastBoard board = ScoreboardHandler.scoreboards.get(online.getUniqueId());
                    final ScoreboardAdapter adapter = ScoreboardHandler.adapter();
                    if (board != null && adapter != null) {
                        board.updateTitle(adapter.getTitle(online));
                        final List<Component> list = board.localList.get();
                        if (!list.isEmpty()) {
                            list.clear();
                        }
                        adapter.getLines(board.localList.get(), online);
                        board.updateLines(list);
                    }
                } catch (final Exception e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                Thread.sleep(2 * 50L);
            } catch (final InterruptedException e2) {
                throw new RuntimeException(e2);
            }
        }
    }
}
