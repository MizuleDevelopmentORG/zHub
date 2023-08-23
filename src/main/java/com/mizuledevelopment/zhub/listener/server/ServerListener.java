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

    private final zHub plugin;
    private final ConfigFile settings;

    public ServerListener(final zHub plugin) {
        this.plugin = plugin;
        this.settings = plugin.config("settings");
    }

    @EventHandler
    public void onWeather(final WeatherChangeEvent event) {
        if (!this.settings.getBoolean("weather", false)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntity(final EntityDamageEvent event) {
        if (!this.settings.getBoolean("damage", false)) {
            event.setDamage(0);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntity(final EntityDamageByBlockEvent event) {
        if (!this.settings.getBoolean("damage", false)) {
            event.setDamage(0);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntity(final EntityDamageByEntityEvent event) {
        if (!this.settings.getBoolean("pvp", false)) {
            event.setDamage(0);
            event.setCancelled(true);
        }
    }
}
