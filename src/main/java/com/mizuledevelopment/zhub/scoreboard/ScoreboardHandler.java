package com.mizuledevelopment.zhub.scoreboard;

import com.mizuledevelopment.zhub.util.scoreboard.FastBoard;
import com.mizuledevelopment.zhub.util.scoreboard.ScoreGetter;
import com.mizuledevelopment.zhub.util.scoreboard.TitleGetter;
import com.mizuledevelopment.zhub.zHub;
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
    private static TitleGetter titleGetter = new DefaultTitleGetter();
    private static ScoreGetter scoreGetter = new DefaultScoreGetter();

    public ScoreboardHandler(final zHub zHub) {
    }

    public static void configure(final TitleGetter titleGetter, final ScoreGetter scoreGetter) {
        ScoreboardHandler.titleGetter = titleGetter;
        ScoreboardHandler.scoreGetter = scoreGetter;
    }

    public static TitleGetter getTitleGetter() {
        return titleGetter;
    }

    public static ScoreGetter getScoreGetter() {
        return scoreGetter;
    }

    public static Map<UUID, FastBoard> getScoreboards() {
        return scoreboards;
    }

    static class DefaultTitleGetter implements TitleGetter {
        @Override
        public Component getTitle(final @NotNull Player player) {
            return Component.text("Un-configured scoreboard", TextColor.color(0x9C79FF));
        }
    }

    static class DefaultScoreGetter implements ScoreGetter {
        public void getScores(final @NotNull LinkedList<? super Component> scores, final @NotNull Player player) {
            scores.add(Component.text("Hey there,", TextColor.color(0xB9B2FF))
                    .append(Component.space())
                    .append(player.displayName())
                    .append(Component.text("!", TextColor.color(0xB9B2FF)))
            );
        }
    }
}
