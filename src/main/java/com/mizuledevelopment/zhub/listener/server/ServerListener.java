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
package com.mizuledevelopment.zhub.listener.server;

import com.mizuledevelopment.zhub.config.impl.ConfigFile;
import com.mizuledevelopment.zhub.util.text.MessageType;
import com.mizuledevelopment.zhub.util.text.TextUtil;
import com.mizuledevelopment.zhub.zHub;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class ServerListener implements Listener {

    private final ConfigFile settings;
    private final ConfigFile config;

    public ServerListener(zHub plugin) {
        this.config = plugin.config("config");
        this.settings = plugin.config("settings");
    }

    @EventHandler
    public void onWeather(WeatherChangeEvent event) {
        if (!this.settings.getBoolean("weather")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntity(EntityDamageEvent event) {
        if (!this.settings.getBoolean("damage")) {
            event.setDamage(0);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntity(EntityDamageByBlockEvent event) {
        if (!this.settings.getBoolean("damage")) {
            event.setDamage(0);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntity(EntityDamageByEntityEvent event) {
        if (!this.settings.getBoolean("pvp")) {
            event.setDamage(0);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (!this.settings.getBoolean("break")) {
            if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (!this.settings.getBoolean("place")) {
            if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (!this.settings.getBoolean("chat")) {
            if (!event.getPlayer().hasPermission("zhub.chat.bypass")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // cry
        if (event.getView().title().equals(TextUtil.parse(this.config.getString("inventory.selector.title"), MessageType.from(this.config.getString("inventory.selector.title"))))
        || event.getView().title().equals(TextUtil.parse(this.config.getString("inventory.hub.title"), MessageType.from(this.config.getString("inventory.hub.title"))))) {
            event.setCancelled(true);
        }
    }
}
