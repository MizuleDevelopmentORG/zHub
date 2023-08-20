package com.mizuledevelopment.zhub.tab;

import com.mizuledevelopment.zhub.zHub;

import java.util.ArrayList;
import java.util.List;

public class Tab {

    private final zHub plugin;
    private final List<String> header;
    private final List<String> footer;

    public Tab(final zHub plugin) {
        this.plugin = plugin;
        this.header = new ArrayList<>(this.plugin.getTab().getStringList("tab.header"));
        this.footer = new ArrayList<>(this.plugin.getTab().getStringList("tab.footer"));
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
