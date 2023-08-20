package com.mizuledevelopment.tab.listener;

import com.mizuledevelopment.zHub;
import com.mizuledevelopment.tab.Tab;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class TabListener implements Listener {

    private final zHub plugin;
    private final Tab tab;

    public TabListener(zHub plugin) {
        this.plugin = plugin;
        this.tab = new Tab(this.plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (this.plugin.getSettings().getBoolean("tab")) {
            event.getPlayer()
                    .setPlayerListHeaderFooter(this.tab.header(), this.tab.footer());
        }
    }
}
