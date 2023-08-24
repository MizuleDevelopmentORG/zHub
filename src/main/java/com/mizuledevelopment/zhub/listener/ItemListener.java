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
package com.mizuledevelopment.zhub.listener;

import com.mizuledevelopment.zhub.item.api.ClickableItem;
import com.mizuledevelopment.zhub.item.api.Hotbar;
import com.mizuledevelopment.zhub.item.api.HotbarHandler;
import com.mizuledevelopment.zhub.zHub;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

public class ItemListener implements Listener {

    private final zHub lobby;
    private final HotbarHandler handler;

    /**
     * Item Listeners.
     *
     * @param handler instance.
     */
    public ItemListener(final HotbarHandler handler) {
        this.lobby = zHub.instance();
        this.handler = handler;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onItemDrop(final PlayerDropItemEvent event) {
        final Player player = event.getPlayer();

        for (final Hotbar hotbar : this.handler.hotbars()) {
            for (final ClickableItem items : hotbar.cachedItems().values()) {
                // Check that the ItemStack is similar.
                if (items.itemStack().callback(player).isSimilar(event.getItemDrop().getItemStack())) {
                    if (items.droppable()) {
                        continue;
                    }
                    event.setCancelled(true);
                    this.handler.delayedUpdateInventory(player);
                }
            }
        }
    }

    @EventHandler(
            priority = EventPriority.MONITOR,
            ignoreCancelled = true
    )
    public void onSwap(final PlayerSwapHandItemsEvent event) {
        final Player player = event.getPlayer();
        boolean update = false;


        for (final Hotbar hotbar : this.handler.hotbars()) {
            for (final ClickableItem items : hotbar.cachedItems().values()) {
                // Current Item.
                if (items.itemStack().callback(player).isSimilar(event.getOffHandItem()) || items.itemStack().callback(player).isSimilar(event.getMainHandItem())) {
                    if (!items.moveable()) {
                        event.setCancelled(true);
                        update = true;
                    }
                }
            }
        }

        if (update) {
            this.handler.delayedUpdateInventory(player);
        }
    }

    @EventHandler(
            priority = EventPriority.MONITOR,
            ignoreCancelled = true
    )
    public void onInventoryClick(final InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();

        boolean update = false;

        for (final Hotbar hotbar : this.handler.hotbars()) {
            for (final ClickableItem items : hotbar.cachedItems().values()) {
                // Current Item.
                if (items.itemStack().callback(player).isSimilar(event.getCurrentItem())) {
                    if (!items.moveable()) {
                        event.setResult(Event.Result.DENY);
                        event.setCancelled(true);
                        update = true;
                    }
                }
                // Cursor.
                if (items.itemStack().callback(player).isSimilar(event.getCursor())) {
                    if (!items.moveable()) {
                        event.setResult(Event.Result.DENY);
                        event.setCancelled(true);
                        update = true;
                    }
                }
                // Number.
                if (event.getHotbarButton() != -1
                        && items.itemStack().callback(player).isSimilar(player.getInventory().getItem(event.getHotbarButton()))) {
                    if (!items.moveable()) {
                        event.setResult(Event.Result.DENY);
                        event.setCancelled(true);
                        update = true;
                    }
                }
            }
        }

        if (update) {
            this.handler.delayedUpdateInventory(player);
        }
    }

    @EventHandler
    public void onBlockPlace(final BlockPlaceEvent event) {
        final Player player = event.getPlayer();

        final ItemStack mainHand = player.getInventory().getItemInMainHand();
        if (mainHand == null || mainHand.getType() == Material.AIR) {
            return;
        }

        for (final Hotbar hotbar : this.handler.hotbars()) {
            for (final ClickableItem items : hotbar.cachedItems().values()) {
                if (items.itemStack().callback(player).isSimilar(mainHand)) {
                    event.setCancelled(true);
                    player.updateInventory();
                    return;
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClick(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final ItemStack mainHand = player.getInventory().getItemInMainHand();


        if (mainHand == null
                || mainHand.getType() == Material.AIR
                || (!event.getAction().isRightClick())) {
            return;
        }

        for (final Hotbar hotbar : this.handler.hotbars()) {
            for (final ClickableItem items : hotbar.cachedItems().values()) {
                final ItemStack itemStack = items.itemStack().callback(player);
                if (itemStack.isSimilar(mainHand)) {
                    if (itemStack.getType() == Material.WRITTEN_BOOK || itemStack.getType() == Material.WRITABLE_BOOK) {
                        if (items.clickHandler() == null) {
                            continue;
                        } else {
                            items.clickHandler().click(player);
                        }
                        event.setCancelled(true);
                        event.setUseItemInHand(Event.Result.DENY);
                        event.setUseInteractedBlock(Event.Result.DENY);
                        player.updateInventory();
                        continue;
                    }
                    event.setCancelled(true);
                    event.setUseItemInHand(Event.Result.DENY);
                    event.setUseInteractedBlock(Event.Result.DENY);
                    player.updateInventory();
                    if (items.clickHandler() == null) {
                        continue;
                    }
                    items.clickHandler().click(player);
                    return;
                }
            }
        }
    }

}
