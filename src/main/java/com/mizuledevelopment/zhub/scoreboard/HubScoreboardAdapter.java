package com.mizuledevelopment.zhub.scoreboard;

import com.mizuledevelopment.zhub.config.impl.ConfigFile;
import com.mizuledevelopment.zhub.util.scoreboard.ScoreboardAdapter;
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
                    player,
                    Placeholder.component("player", player.name())
            ));
        }
    }
}
