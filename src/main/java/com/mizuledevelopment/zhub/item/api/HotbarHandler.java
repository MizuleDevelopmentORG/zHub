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
