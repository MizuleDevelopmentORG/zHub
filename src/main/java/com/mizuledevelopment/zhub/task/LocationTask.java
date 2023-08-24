package com.mizuledevelopment.zhub.task;

import com.mizuledevelopment.zhub.zHub;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class LocationTask extends BukkitRunnable {

    private final zHub lobby;
    // private final LazyLocation location;
    // private final Location bukkitLocation;
    private final int yLocation = 20;
    // private final Cuboid cuboid;

    public LocationTask(final zHub lobby) {
        this.lobby = lobby;
        // var config = Lobby.instance().getConfig().getConfigurationSection("spawn.location");
        // if (config == null) {
        //     config = Lobby.instance().getConfig().createSection("spawn.location");
        // }
    }

    @Override
    public void run() {
        // final World spawnWorld = Bukkit.getWorld(UUID.fromString(this.location.world()));
        // if (spawnWorld == null) {
        //     this.cancel();
        //     return;
        // }
        // for (final Player player : spawnWorld.getPlayers()) {
            // if (LPUtil.getOptionalMeta(player.getUniqueId(), Constants.META.MODMODE, Functions.BOOLEAN).orElse(false)) {
            //     continue;
            // }
            // final Location playerLocation = player.getLocation();
            //
            // if (!this.cuboid.contains(playerLocation)) {
            //     teleportPlayer(player);
            //     continue;
            // }
            //
            // if (playerLocation.getBlockY() < 20) {
            //     teleportPlayer(player);
            //     continue;
            // }
        // }
    }

    public void teleportPlayer(final @NotNull Player player) {
        // final World spawnWorld = Bukkit.getWorld(UUID.fromString(this.location.world()));
        // if (spawnWorld == null) {
        //     this.cancel();
        //     return;
        // }
        // player.setFallDistance(0);
        // if (this.bukkitLocation == null) {
        //     player.teleportAsync(spawnWorld.getSpawnLocation());
        //     return;
        // }
        // player.teleportAsync(this.bukkitLocation).thenAccept(result -> {
        //     if (result) {
        //         player.sendRichMessage("<green>Successfully teleported to spawn.");
        //     } else {
        //         player.sendRichMessage("<red>Failed to teleport to spawn.");
        //     }
        // });
    }
}
