package com.mizuledevelopment.zhub.listener.player;

import com.destroystokyo.paper.event.player.PlayerInitialSpawnEvent;
import com.mizuledevelopment.zhub.zHub;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import java.util.Objects;

public class PlayerListener implements Listener {

    private final zHub plugin;

    public PlayerListener(zHub plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerSpawnLocationEvent event) {
        event.setSpawnLocation(Objects.requireNonNull(this.plugin.getConfiguration().getLocation("spawn")));
    }

    @EventHandler
    public void onVoid(PlayerMoveEvent event) {
        if (!this.plugin.getSettings().getBoolean("void")) {
            if (event.getPlayer().getLocation().getBlockY() <= this.plugin.getConfiguration().getInt("void")) {
                event.getPlayer().teleportAsync(Objects.requireNonNull(this.plugin.getConfiguration().getLocation("spawn")));
            }
        }
    }
}
