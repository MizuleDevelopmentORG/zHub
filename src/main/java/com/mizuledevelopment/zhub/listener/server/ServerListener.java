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
        if (event.getView().title().equals(TextUtil.parse(this.config.getString("inventory.selector.title"), MessageType.from(this.config.getString("inventory.selector.title"))))
        || event.getView().title().equals(TextUtil.parse(this.config.getString("inventory.hub.title"), MessageType.from(this.config.getString("inventory.hub.title"))))) {
            event.setCancelled(true);
        }
    }
}
