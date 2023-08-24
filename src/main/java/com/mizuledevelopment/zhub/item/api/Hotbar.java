/*
 * This file is part of Lobby, licensed under the MIT License.
 *
 * Copyright (c) 2023 powercas_gamer
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
