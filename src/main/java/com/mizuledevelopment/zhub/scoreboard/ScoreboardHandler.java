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
