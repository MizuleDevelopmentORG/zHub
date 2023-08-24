package com.mizuledevelopment.zhub.command;

import com.mizuledevelopment.zhub.config.impl.ConfigFile;
import com.mizuledevelopment.zhub.util.color.Color;
import com.mizuledevelopment.zhub.util.command.Command;
import com.mizuledevelopment.zhub.zHub;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnCommand extends Command {

    private final ConfigFile config;
    private final ConfigFile messages;

    public SetSpawnCommand(final zHub plugin) {
        this.config = plugin.config("config");
        this.messages = plugin.config("messages");
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        if (!(sender instanceof final Player player)) return;
        if (!(sender.hasPermission("zhub.command.setspawn"))) {
            sender.sendMessage(Color.translate(this.messages.getString("messages.no-permissions")));
        }

        this.config.set("spawn", "set");
        this.config.save();
        player.sendMessage(Color.translate(this.messages.getString("messages.setspawn")));
    }

    @Override
    public String getName() {
        return "setspawn";
    }

    @Override
    public boolean allow() {
        return true;
    }

    @Override
    public String getUsage() {
        return this.messages.getString("messages.setspawn-usage");
    }
}
