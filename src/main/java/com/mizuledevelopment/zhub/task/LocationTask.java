package com.mizuledevelopment.zhub.task;

import com.mizuledevelopment.zhub.util.cuboid.Cuboid;
import com.mizuledevelopment.zhub.util.location.LazyLocation;
import com.mizuledevelopment.zhub.util.location.LocationUtil;
import com.mizuledevelopment.zhub.zHub;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class LocationTask extends BukkitRunnable {

    private final zHub lobby;
    private final LazyLocation location;
    private final Location bukkitLocation;
    private final int yLocation = 20;
    private final Cuboid cuboid;

    public LocationTask(final zHub lobby) {
        this.lobby = lobby;
        var config = zHub.instance().config("config").section("spawn.location");
        this.location = LocationUtil.parse(config);

        this.bukkitLocation = this.location.location();
        this.cuboid = new Cuboid(
            Bukkit.getWorld(this.location.worldName()),
            config.node("min", "x").getInt(),
            config.node("min", "y").getInt(),
            config.node("min", "z").getInt(),
            config.node("max", "x").getInt(),
            config.node("max", "y").getInt(),
            config.node("max", "z").getInt()
        );
    }

    @Override
    public void run() {
        final World spawnWorld = Bukkit.getWorld(UUID.fromString(this.location.world()));
        if (spawnWorld == null) {
            this.cancel();
            return;
        }
        for (final Player player : spawnWorld.getPlayers()) {
            final Location playerLocation = player.getLocation();

            if (!this.cuboid.contains(playerLocation)) {
                teleportPlayer(player);
                continue;
            }

            if (playerLocation.getBlockY() < 20) {
                teleportPlayer(player);
                continue;
            }
        }
    }

    public void teleportPlayer(final @NotNull Player player) {
        final World spawnWorld = Bukkit.getWorld(UUID.fromString(this.location.world()));
        if (spawnWorld == null) {
            this.cancel();
            return;
        }

        Bukkit.getScheduler().runTask(this.lobby, task -> {
            player.setFallDistance(0);
            if (this.bukkitLocation == null) {
                player.teleportAsync(spawnWorld.getSpawnLocation());
                task.cancel();
                return;
            }
            player.teleportAsync(this.bukkitLocation).thenAccept(result -> {
                if (result) {
                    // TODO: minimessage
                    player.sendMessage("<green>Successfully teleported to spawn.");
                } else {
                    player.sendMessage("<red>Failed to teleport to spawn.");
                }
            });
        });
    }
}
