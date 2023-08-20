package com.mizuledevelopment.zhub.util.scoreboard;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;

public interface ScoreGetter {

    void getScores(final @NotNull LinkedList<? super Component> scores, final @NotNull Player player);
}
