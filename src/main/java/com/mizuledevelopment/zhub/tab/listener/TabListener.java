package com.mizuledevelopment.zhub.tab.listener;

import com.mizuledevelopment.zhub.tab.Tab;
import com.mizuledevelopment.zhub.zHub;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class TabListener implements Listener {

    private final zHub plugin;
    private final Tab tab;

    public TabListener(final zHub plugin) {
        this.plugin = plugin;
        this.tab = new Tab(this.plugin);
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        if (this.plugin.getSettings().getBoolean("tab")) {
            event.getPlayer()
                    .setPlayerListHeaderFooter(this.tab.header(), this.tab.footer());
        }
    }
}
