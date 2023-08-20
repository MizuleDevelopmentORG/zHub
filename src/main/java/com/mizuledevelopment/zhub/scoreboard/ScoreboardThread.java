package com.mizuledevelopment.zhub.scoreboard;

import com.mizuledevelopment.zhub.util.scoreboard.FastBoard;
import com.mizuledevelopment.zhub.zHub;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public final class ScoreboardThread extends Thread {

    public ScoreboardThread() {
        super("Axel - Scoreboard Thread");
        this.setDaemon(true);
    }

    @Override
    public void run() {
        while (zHub.instance() != null && zHub.instance().isEnabled()) {
            for (final Player online : Bukkit.getOnlinePlayers()) {
                try {
                    final FastBoard board = ScoreboardHandler.scoreboards.get(online.getUniqueId());
                    if (board != null && ScoreboardHandler.getScoreGetter() != null && ScoreboardHandler.getTitleGetter() != null) {
                        board.updateTitle(ScoreboardHandler.getTitleGetter().getTitle(online));
                        final List<Component> list = board.localList.get();
                        if (!list.isEmpty()) {
                            list.clear();
                        }
                        ScoreboardHandler.getScoreGetter().getScores(board.localList.get(), online);
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
