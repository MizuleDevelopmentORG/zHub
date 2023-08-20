package com.mizuledevelopment.tab;

import com.mizuledevelopment.zHub;

import java.util.ArrayList;
import java.util.List;

public class Tab {

    private final zHub plugin;
    private final List<String> header;
    private final List<String> footer;

    public Tab(zHub plugin) {
        this.plugin = plugin;
        this.header = new ArrayList<>(this.plugin.getTab().getStringList("tab.header"));
        this.footer = new ArrayList<>(this.plugin.getTab().getStringList("tab.footer"));
    }

    public String header() {
        String s = null;
        for (String string : header) {
            if (s == null) {
                s = string;
            } else {
                s = s + string;
            }
        }
        return s;
    }

    public String footer() {
        String s = null;
        for (String string : footer) {
            if (s == null){
                s = string;
            } else {
                s = s + string;
            }
        }
        return s;
    }
}
