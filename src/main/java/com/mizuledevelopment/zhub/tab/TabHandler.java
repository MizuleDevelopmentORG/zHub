package com.mizuledevelopment.zhub.tab;

import com.mizuledevelopment.zhub.config.impl.ConfigFile;
import com.mizuledevelopment.zhub.tab.listener.TabListener;
import com.mizuledevelopment.zhub.util.text.MessageType;
import com.mizuledevelopment.zhub.util.text.TextUtil;
import com.mizuledevelopment.zhub.zHub;
import it.unimi.dsi.fastutil.io.TextIO;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class TabHandler {

    private final List<Component> header = new ArrayList<>();
    private final List<Component> footer = new ArrayList<>();

    public TabHandler(final zHub plugin) {
        ConfigFile tab = plugin.config("tab");
        tab.getStringList("tab.header").forEach(line -> {
            header.add(TextUtil.parse(line, MessageType.from(line)));
        });
        tab.getStringList("tab.footer").forEach(line -> {
            footer.add(TextUtil.parse(line, MessageType.from(line)));
        });
        Bukkit.getPluginManager().registerEvents(new TabListener(this), plugin);
    }

    public Component header() {
        final StringBuilder string = new StringBuilder();
        for (Component component : header) {
            string.append(component.toString());
        }
        return Component.text(string.toString());
    }

    public Component footer() {
        final StringBuilder string = new StringBuilder();
        for (Component component : footer) {
            string.append(component.toString());
        }
        return Component.text(string.toString());
    }
}
