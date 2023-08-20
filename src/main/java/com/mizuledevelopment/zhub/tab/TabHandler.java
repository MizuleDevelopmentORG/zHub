package com.mizuledevelopment.zhub.tab;

import com.mizuledevelopment.zhub.tab.listener.TabListener;
import com.mizuledevelopment.zhub.zHub;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class TabHandler {

    private final zHub plugin;
    private final List<String> header;
    private final List<String> footer;

    public TabHandler(final zHub plugin) {
        this.plugin = plugin;
        this.header = new ArrayList<>(this.plugin.getTab().getStringList("tab.header"));
        this.footer = new ArrayList<>(this.plugin.getTab().getStringList("tab.footer"));
        Bukkit.getPluginManager().registerEvents(new TabListener(this), this.plugin);
    }

    public String header() {
        final StringBuilder s = new StringBuilder();
        for (final String string : this.header) {
            s.append(string);
        }
        return s.toString();
    }

    public String footer() {
        final StringBuilder s = new StringBuilder();
        for (final String string : this.footer) {
            s.append(string);
        }
        return s.toString();
    }
}
