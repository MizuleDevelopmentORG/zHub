package com.mizuledevelopment.zhub.util.color;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class Color {

    public static String translate(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static List<String> translate(List<String> s) {
        List<String> strings = new ArrayList<>();
        s.forEach(string -> strings.add(Color.translate(string)));
        return strings;
    }
}