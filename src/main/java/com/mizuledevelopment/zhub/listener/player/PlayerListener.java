package com.mizuledevelopment.zhub.listener.player;

import com.mizuledevelopment.zhub.config.impl.ConfigFile;
import com.mizuledevelopment.zhub.zHub;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import java.util.Objects;

public class PlayerListener implements Listener {

    private final zHub plugin;
    private final ConfigFile settings;

    public PlayerListener(zHub plugin) {
        this.plugin = plugin;
        this.settings = plugin.config("settings");

    }

    @EventHandler
    public void onJoin(PlayerSpawnLocationEvent event) {
        final Location location = this.plugin.getConfiguration().getLocation("spawn"); // TODO: switch to spawn location from new config
        if (location != null) {
            event.setSpawnLocation(location);
        }
    }

    // TODO: improve this by changing it to a task
    @EventHandler
    public void onVoid(PlayerMoveEvent event) {
        if (!this.settings.getBoolean("void", false)) {
            if (event.getPlayer().getLocation().getBlockY() <= this.plugin.getConfiguration().getInt("void")) {
                event.getPlayer().teleportAsync(Objects.requireNonNull(this.plugin.getConfiguration().getLocation("spawn")));
            }
        }
    }
}
