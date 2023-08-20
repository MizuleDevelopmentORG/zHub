package com.mizuledevelopment.zhub.scoreboard;


import com.mizuledevelopment.zhub.util.scoreboard.FastBoard;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ScoreboardListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoinEvent(final PlayerJoinEvent event) {
        ScoreboardHandler.scoreboards.put(event.getPlayer().getUniqueId(), new FastBoard(event.getPlayer()));
    }

    @EventHandler
    public void onPlayerQuitEvent(final PlayerQuitEvent event) {
        ScoreboardHandler.scoreboards.remove(event.getPlayer().getUniqueId());
    }

}
