package com.mizuledevelopment.zhub.util.scoreboard;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.LinkedList;

public interface ScoreGetter {

    void getScores(LinkedList<? super Component> scores, Player player);
}
