package com.mizuledevelopment.zhub.util.scoreboard;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;

public interface ScoreboardAdapter {

    Component getTitle(
            final @NotNull Player player
    );

    void getLines(final @NotNull LinkedList<? super Component> lines, final @NotNull Player player);

}
