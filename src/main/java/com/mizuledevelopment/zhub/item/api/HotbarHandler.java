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
package com.mizuledevelopment.zhub.item.api;

import com.mizuledevelopment.zhub.listener.ItemListener;
import com.mizuledevelopment.zhub.zHub;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class HotbarHandler {

    private final zHub lobby;
    private final Set<Hotbar> hotbars = ConcurrentHashMap.newKeySet();

    public HotbarHandler(final zHub lobby) {
        this.lobby = lobby;
        Bukkit.getPluginManager().registerEvents(new ItemListener(this), lobby);
    }


    /**
     * Add hotbar to Lobby.
     *
     * @param hotbar to add.
     */
    public void addHotbar(final Hotbar hotbar) {
        this.hotbars.add(hotbar);
    }

    /**
     * Remove hotbar from Lobby.
     *
     * @param hotbar to remove.
     */
    public void removeHotbar(final Hotbar hotbar) {
        this.hotbars.remove(hotbar);
    }

    /**
     * Delayed Inventory Update.
     *
     * @param player to update inventory of.
     */
    public void delayedUpdateInventory(final Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
//                player.updateInventory();
            }
        }.runTaskLater(this.lobby, 2);
    }

    public Set<Hotbar> hotbars() {
        return this.hotbars;
    }

    public @Nullable Hotbar getHotbar(
            final @NotNull String id
    ) {
        return this.hotbars.stream().filter(hotbar -> hotbar.id().equals(id)).findFirst().orElse(null);
    }

    public @NotNull Optional<@Nullable Hotbar> hotbar(
            final @NotNull String id
    ) {
        return this.hotbars.stream().filter(hotbar -> hotbar.id().equals(id)).findFirst();
    }
}
