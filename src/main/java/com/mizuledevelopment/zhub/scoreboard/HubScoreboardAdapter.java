package com.mizuledevelopment.zhub.scoreboard;

import com.mizuledevelopment.zhub.util.scoreboard.ScoreboardAdapter;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;

public class HubScoreboardAdapter implements ScoreboardAdapter {

    @Override
    public Component getTitle(final @NotNull Player player) {
        return Component.text("TITLE");
    }

    @Override
    public void getLines(final @NotNull LinkedList<? super Component> lines, final @NotNull Player player) {
        lines.add(Component.text("LINE 1"));
    }
}
