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

import com.mizuledevelopment.zhub.zHub;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Hotbar {

    private final zHub lobby;

    private final String id;
    private final Map<String, ClickableItem> cachedItems = new ConcurrentHashMap<>();

    /**
     * Hotbar.
     *
     * @param id    of hotbar.
     * @param lobby instance.
     */
    public Hotbar(final String id, final zHub lobby) {
        this.lobby = lobby;
        this.id = id;
    }

    /**
     * Add cached item to hotbar.
     *
     * @param id   of item.
     * @param item to add.
     */
    public void addCachedItem(final String id, final ClickableItem item) {
        this.cachedItems.put(id, item);
    }

    /**
     * Remove cached item from hotbar.
     *
     * @param id of item.
     */
    public void removeCachedItem(final String id) {
        this.cachedItems.remove(id);
    }

    /**
     * Get cached item from hotbar.
     *
     * @param id of item.
     * @return item.
     */
    public ClickableItem getCachedItem(final String id) {
        return this.cachedItems.get(id);
    }

    /**
     * Apply clickable item to player.
     *
     * @param player to apply to.
     * @param item   to give.
     * @param slot   to place item.
     */
    public void applyToPlayer(final Player player, final ClickableItem item, final int slot) {
        player.getInventory().setItem(slot, null);
        player.getInventory().setItem(slot, item.itemStack().callback(player));
        this.lobby.hotbarHandler().delayedUpdateInventory(player);
    }

    /**
     * Apply hotbar items to player.
     *
     * @param player to apply hotbar to.
     * @param clear  whether to clear.
     */
    public void applyToPlayer(final Player player, final boolean clear) {
        if (clear) {
            for (int i = 0; i < 8; i++) {
                player.getInventory().setItem(i, null);
            }
        }
        final Map<Integer, ClickableItem> itemsToApply = itemsToApply(player);
        for (final Map.Entry<Integer, ClickableItem> entry : itemsToApply.entrySet()) {
            player.getInventory().setItem(entry.getKey(), entry.getValue().itemStack().callback(player));
        }
        this.lobby.hotbarHandler().delayedUpdateInventory(player);
    }

    /**
     * Get a mapping of items to apply to a player.
     *
     * @param player to apply to.
     * @return mapping of items with slots.
     */
    public abstract Map<Integer, ClickableItem> itemsToApply(
            final @NotNull Player player
    );

    public String id() {
        return this.id;
    }

    public Map<String, ClickableItem> cachedItems() {
        return this.cachedItems;
    }

}
