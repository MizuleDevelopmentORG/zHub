package com.mizuledevelopment.zhub.tab.listener;

import com.mizuledevelopment.zhub.tab.TabHandler;
import com.mizuledevelopment.zhub.zHub;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class TabListener implements Listener {

    private final TabHandler tabHandler;

    public TabListener(final TabHandler tabHandler) {
        this.tabHandler = tabHandler;
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        event.getPlayer()
                .setPlayerListHeaderFooter(this.tabHandler.header(), this.tabHandler.footer());
    }
}
