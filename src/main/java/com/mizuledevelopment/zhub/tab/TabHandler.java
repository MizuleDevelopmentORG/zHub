package com.mizuledevelopment.zhub.tab;

import com.mizuledevelopment.zhub.tab.listener.TabListener;
import com.mizuledevelopment.zhub.zHub;
import net.kyori.adventure.text.Component;
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

    // TODO: I'll improve this later, I'm just lazy right now.
    public Component header() {
        final StringBuilder s = new StringBuilder();
        for (final String string : this.header) {
            s.append(string);
        }
        return Component.text(s.toString());
    }

    public Component footer() {
        final StringBuilder s = new StringBuilder();
        for (final String string : this.footer) {
            s.append(string);
        }
        return Component.text(s.toString());
    }
}
