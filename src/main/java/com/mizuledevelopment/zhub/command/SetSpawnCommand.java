package com.mizuledevelopment.zhub.command;

import com.mizuledevelopment.zhub.config.impl.ConfigFile;
import com.mizuledevelopment.zhub.util.color.Color;
import com.mizuledevelopment.zhub.util.command.Command;
import com.mizuledevelopment.zhub.zHub;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

public class SetSpawnCommand extends Command {

    private final ConfigFile config;
    private final ConfigFile messages;

    public SetSpawnCommand(final zHub plugin) {
        this.config = plugin.config("config");
        this.messages = plugin.config("messages");
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        if (!(sender instanceof final Player player)) {
            sender.sendMessage("player only bitch");
            return;
        }
        if (!(sender.hasPermission("zhub.command.setspawn"))) {
            sender.sendMessage(Color.translate(this.messages.getString("messages.no-permissions")));
        }

        final ConfigurationNode section = this.config.section("spawn.location");
        final var location = player.getLocation();
        try {
            section.node("worldId").set(location.getWorld().getUID().toString());
            section.node("world").set(location.getWorld().getName());
            section.node("x").set(location.getBlockX());
            section.node("y").set(location.getBlockY());
            section.node("z").set(location.getBlockZ());
            section.node("pitch").set(location.getPitch());
            section.node("yaw").set(location.getYaw());
            this.config.save();
        } catch (SerializationException e) {
            throw new RuntimeException(e);
        }
        player.sendMessage(Color.translate(this.messages.getString("messages.setspawn", "set spawn")));
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
        return this.messages.getString("messages.setspawn-usage", "/setspawn dumbass");
    }
}
