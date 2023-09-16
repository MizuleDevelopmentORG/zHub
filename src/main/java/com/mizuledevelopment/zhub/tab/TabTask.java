package com.mizuledevelopment.zhub.tab;

import com.mizuledevelopment.zhub.config.impl.ConfigFile;
import com.mizuledevelopment.zhub.util.cuboid.Cuboid;
import com.mizuledevelopment.zhub.util.location.LazyLocation;
import com.mizuledevelopment.zhub.util.text.MessageType;
import com.mizuledevelopment.zhub.util.text.TextUtil;
import com.mizuledevelopment.zhub.zHub;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class TabTask extends BukkitRunnable {

    private final zHub lobby;
    private final ConfigFile config;

    public TabTask(final zHub lobby) {
        this.lobby = lobby;
        this.config = lobby.config("tab");
    }

    @Override
    public void run() {
        final List<String> header = this.config.getStringList("header");
        final List<String> footer = this.config.getStringList("footer");
        for (final Player player : Bukkit.getOnlinePlayers()) {
            player.sendPlayerListHeaderAndFooter(
                Component.join(JoinConfiguration.newlines(), this.parse(player, header)),
                Component.join(JoinConfiguration.newlines(), this.parse(player, footer))
            );
        }
    }

    private List<Component> parse(final Player player, final List<String> lines) {
        final List<Component> components = new ArrayList<>();

        for (final String line : lines) {
            components.add(TextUtil.parse(line, MessageType.from(line), player));
        }

        return components;
    }
}
