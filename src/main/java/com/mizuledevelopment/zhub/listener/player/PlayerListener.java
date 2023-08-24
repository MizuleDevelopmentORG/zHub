package com.mizuledevelopment.zhub.listener.player;

import com.mizuledevelopment.zhub.config.impl.ConfigFile;
import com.mizuledevelopment.zhub.util.items.Items;
import com.mizuledevelopment.zhub.util.text.MessageType;
import com.mizuledevelopment.zhub.util.text.TextUtil;
import com.mizuledevelopment.zhub.zHub;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.persistence.PersistentDataType;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;
import org.spongepowered.configurate.CommentedConfigurationNode;

import java.util.Objects;

public class PlayerListener implements Listener {

    private final zHub plugin;
    private final ConfigFile settings;
    private final ConfigFile config;

    public PlayerListener(zHub plugin) {
        this.plugin = plugin;
        this.config = plugin.config("config");
        this.settings = plugin.config("settings");
    }

    @EventHandler
    public void onJoin(PlayerSpawnLocationEvent event) {
        /*
        if (this.config.get("spawn") != null) {
            final Location location = (Location) this.config.get("spawn");
            if (location != null) {
                event.setSpawnLocation(location);
            }
        }
        */
        if (this.settings.getBoolean("selector-item")) {
            event.getPlayer().getInventory().setItem(this.config.getInt("selector.slot"), Items.SELECTOR(this.plugin));
        }
        if (this.settings.getBoolean("enderbutt-item")) {
            event.getPlayer().getInventory().setItem(this.config.getInt("enderbutt.slot"), Items.ENDERBUTT(this.plugin));
        }
        if (this.settings.getBoolean("pvp-item")) {
            event.getPlayer().getInventory().setItem(this.config.getInt("pvp.slot"), Items.PVP(this.plugin));
        }
        if (this.settings.getBoolean("hub-item")) {
            event.getPlayer().getInventory().setItem(this.config.getInt("hub.slot"), Items.HUB(this.plugin));
        }
    }

    @EventHandler
    public void onVoid(PlayerMoveEvent event) {
        if (!this.settings.getBoolean("void")) {
            if (event.getPlayer().getLocation().getBlockY() <= this.config.getInt("void")) {
                event.getPlayer().teleportAsync((Location) Objects.requireNonNull(this.config.get("spawn")));
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)
        || event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            String name = event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer()
                    .get(this.plugin.getNamespacedKey(), PersistentDataType.STRING);
            if (name != null) {
                if (name.equalsIgnoreCase("selector")) {
                    Inventory inventory = Bukkit.createInventory(event.getPlayer(),
                            this.config.getInt("inventory.selector.size"),
                            TextUtil.parse(this.config.getString("inventory.selector.title"), MessageType.from
                                    (this.config.getString("inventory.selector.title"))));
                    final CommentedConfigurationNode node = this.config.section("inventory.selector.items");
                    for (final CommentedConfigurationNode child : node.childrenMap().values()) {
                        final String name = child.node("name").getString("example");
                        final int slot = (child.node("slot").getInt(1)) - 1;
                        final boolean restricted = child.node("restricted").getBoolean();
                    }

                        event.getPlayer().openInventory(inventory);
                } else if (name.equalsIgnoreCase("hub")) {

                } else if (name.equalsIgnoreCase("pvp")) {

                }
            }
        }
    }
}
