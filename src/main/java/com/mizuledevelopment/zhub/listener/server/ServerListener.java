package com.mizuledevelopment.zhub.listener.server;

import com.mizuledevelopment.zhub.zHub;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class ServerListener implements Listener {

    private final zHub plugin;

    public ServerListener(zHub plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onWeather(WeatherChangeEvent event) {
        if (!this.plugin.getSettings().getBoolean("weather")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntity(EntityDamageEvent event) {
        if (!this.plugin.getSettings().getBoolean("damage")) {
            event.setDamage(0);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntity(EntityDamageByBlockEvent event) {
        if (!this.plugin.getSettings().getBoolean("damage")) {
            event.setDamage(0);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntity(EntityDamageByEntityEvent event) {
        if (!this.plugin.getSettings().getBoolean("pvp")) {
            event.setDamage(0);
            event.setCancelled(true);
        }
    }
}
