package com.mizuledevelopment.zhub.tab;

import com.mizuledevelopment.zhub.zHub;

public class TabHandler {

    public TabHandler(final zHub plugin) {
        new TabTask(plugin).runTaskTimerAsynchronously(plugin, 0, 20L * 5);
    }
}
