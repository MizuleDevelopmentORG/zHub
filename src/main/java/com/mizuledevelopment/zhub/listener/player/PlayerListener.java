package com.mizuledevelopment.zhub.listener.player;

import com.mizuledevelopment.zhub.zHub;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
        final Location location = this.plugin.getConfiguration().getLocation("spawn");
        if (location != null) {
            event.setSpawnLocation(location);
        }
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
