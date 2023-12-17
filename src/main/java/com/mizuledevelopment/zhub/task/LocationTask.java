/*
 * This file is part of zHub, licensed under the GPLv3 License.
 *
 * Copyright (c) 2023 Mizule Development
 * Copyright (c) 2023 contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mizuledevelopment.zhub.task;

import com.mizuledevelopment.zhub.util.PlayerUtil;
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
                player.teleportAsync(LocationUtil.center(spawnWorld.getSpawnLocation()));
                task.cancel();
                return;
            }
            player.teleportAsync(LocationUtil.center(this.bukkitLocation)).thenAccept(result -> {
                if (result) {
                    PlayerUtil.sendMessage(player, "<green>Successfully teleported to spawn.");
                } else {
                    PlayerUtil.sendMessage(player, "<red>Failed to teleport to spawn.");
                }
            });
        });
    }
}
