package com.mizuledevelopment.util;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public final class PlaceholderUtil {

    private PlaceholderUtil() {
    }

    public static String replacePlaceholders(final OfflinePlayer player, final String string) {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            return PlaceholderAPI.setPlaceholders(player, string);
        } else {
            return string;
        }
    }
}
