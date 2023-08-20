package com.mizuledevelopment.zhub.command;

import com.mizuledevelopment.zhub.util.color.Color;
import com.mizuledevelopment.zhub.util.command.Command;
import com.mizuledevelopment.zhub.zHub;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnCommand extends Command {

    private final zHub plugin;

    public SetSpawnCommand(final zHub plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        if (!(sender instanceof final Player player)) return;
        if (!(sender.hasPermission("zhub.command.setspawn"))) {
            sender.sendMessage(Color.translate(this.plugin.getMessages().getString("messages.no-permissions")));
        }

        this.plugin.getConfiguration().set("spawn", player.getLocation());
        this.plugin.getConfigConfiguration().save();
        player.sendMessage(Color.translate(this.plugin.getMessages().getString("messages.setspawn")));
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
        return this.plugin.getMessages().getString("messages.setspawn-usage");
    }
}
