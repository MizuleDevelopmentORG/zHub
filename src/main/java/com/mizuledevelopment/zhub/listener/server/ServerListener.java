package com.mizuledevelopment.zhub.listener.server;

import com.mizuledevelopment.zhub.config.impl.ConfigFile;
import com.mizuledevelopment.zhub.zHub;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class ServerListener implements Listener {

    private final ConfigFile settings;

    public ServerListener(zHub plugin) {
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
}
