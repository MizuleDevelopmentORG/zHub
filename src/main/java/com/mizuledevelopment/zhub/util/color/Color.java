package com.mizuledevelopment.zhub.util.color;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

@Deprecated(forRemoval = true)
public class Color {

    public static String translate(final String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static List<String> translate(final List<String> s) {
        final List<String> strings = new ArrayList<>();
        s.forEach(string -> strings.add(Color.translate(string)));
        return strings;
    }
}