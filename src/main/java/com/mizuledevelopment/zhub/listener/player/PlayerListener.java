/*
 * This file is part of zHub, licensed under the GPLv3 License.
 *
 * Copyright (c) 2023 Mizule Development
 * Copyright (c) 2023 contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mizuledevelopment.zhub.listener.player;

import com.mizuledevelopment.zhub.config.impl.ConfigFile;
import com.mizuledevelopment.zhub.util.items.Items;
import com.mizuledevelopment.zhub.util.location.LazyLocation;
import com.mizuledevelopment.zhub.util.location.LocationUtil;
import com.mizuledevelopment.zhub.util.text.MessageType;
import com.mizuledevelopment.zhub.util.text.TextUtil;
import com.mizuledevelopment.zhub.zHub;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;
import org.spongepowered.configurate.CommentedConfigurationNode;

import java.util.ArrayList;
import java.util.Objects;

public class PlayerListener implements Listener {

    private final zHub plugin;
    private final ConfigFile settings;
    private final ConfigFile config;

    public PlayerListener(final zHub plugin) {
        this.plugin = plugin;
        this.config = plugin.config("config");
        this.settings = plugin.config("settings");
    }

    @EventHandler
    public void onInitialSpawn(final PlayerSpawnLocationEvent event) {
        final LazyLocation lazyLocation = LocationUtil.parse(this.config.section("spawn.location"));
        final Location location = lazyLocation.location();

        if (location == null) {
            this.plugin.getSLF4JLogger().error("Could not find spawn location.");
            return;
        }
        event.setSpawnLocation(location);
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        event.joinMessage(null);
        if (this.settings.getBoolean("join-message")) {
            for (final String string : this.config.getStringList("join.message")) {
                player.sendMessage(TextUtil.parse(
                    string,
                    MessageType.from(string),
                    player,
                    Placeholder.component("player", player.name())
                ));
            }
        }

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
    public void onQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        event.quitMessage(null);
        if (this.settings.getBoolean("quit-message")) {
            for (final String string : this.config.getStringList("quit.message")) {
                player.sendMessage(TextUtil.parse(
                    string,
                    MessageType.from(string),
                    player,
                    Placeholder.component("player", player.name())
                ));
            }
        }
    }

    @EventHandler
    public void onInteract(final PlayerInteractEvent event) {
        // BIG cry
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK
            || event.getAction() == Action.RIGHT_CLICK_AIR) {
            final String name = event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer()
                .get(this.plugin.getNamespacedKey(), PersistentDataType.STRING);
            if (name != null) {
                if (name.equalsIgnoreCase("selector")) {
                    final Inventory inventory = Bukkit.createInventory(event.getPlayer(),
                        this.config.getInt("inventory.selector.size"),
                        TextUtil.parse(this.config.getString("inventory.selector.title"), MessageType.from
                            (this.config.getString("inventory.selector.title"))));
                    final CommentedConfigurationNode node = this.config.section("inventory.selector.items");
                    for (final CommentedConfigurationNode child : node.childrenMap().values()) {
                        final ItemStack itemStack = new ItemStack(Material.valueOf(child.node("item").getString()));
                        itemStack.setAmount(child.node("amount").getInt());
                        child.node("enchantments").childrenList().forEach(enchantment -> {
                            itemStack.addUnsafeEnchantment(Objects.requireNonNull(Enchantment.getByName(Objects.requireNonNull(enchantment.getString()).split(":")[0])),
                                Integer.parseInt(enchantment.getString().split(":")[1]));
                        });
                        final ItemMeta meta = itemStack.getItemMeta();
                        meta.displayName(TextUtil.parse(child.node("name").getString(), MessageType.from(Objects.requireNonNull(child.node("name").getString()))));
                        final ArrayList<Component> components = new ArrayList<>();
                        child.node("lore").childrenList().forEach(line -> {
                            components.add(TextUtil.parse(line.getString(), MessageType.from(Objects.requireNonNull(line.getString()))));
                        });
                        meta.lore(components);
                        itemStack.setItemMeta(meta);
                        inventory.setItem(child.node("slot").getInt(), itemStack);
                    }
                    event.getPlayer().openInventory(inventory);
                } else if (name.equalsIgnoreCase("hub")) {
                    final Inventory inventory = Bukkit.createInventory(event.getPlayer(),
                        this.config.getInt("inventory.hub.size"),
                        TextUtil.parse(this.config.getString("inventory.hub.title"), MessageType.from
                            (this.config.getString("inventory.hub.title"))));
                    final CommentedConfigurationNode node = this.config.section("inventory.hub.items");
                    for (final CommentedConfigurationNode child : node.childrenMap().values()) {
                        final ItemStack itemStack = new ItemStack(Material.valueOf(child.node("item").getString()));
                        itemStack.setAmount(child.node("amount").getInt());
                        child.node("enchantments").childrenList().forEach(enchantment -> {
                            itemStack.addUnsafeEnchantment(Objects.requireNonNull(Enchantment.getByName(Objects.requireNonNull(enchantment.getString()).split(":")[0])),
                                Integer.parseInt(enchantment.getString().split(":")[1]));
                        });
                        final ItemMeta meta = itemStack.getItemMeta();
                        meta.displayName(TextUtil.parse(child.node("name").getString(), MessageType.from(Objects.requireNonNull(child.node("name").getString()))));
                        final ArrayList<Component> components = new ArrayList<>();
                        child.node("lore").childrenList().forEach(line -> {
                            components.add(TextUtil.parse(line.getString(), MessageType.from(Objects.requireNonNull(line.getString()))));
                        });
                        meta.lore(components);
                        itemStack.setItemMeta(meta);
                        inventory.setItem(child.node("slot").getInt(), itemStack);
                    }
                    event.getPlayer().openInventory(inventory);
                } else if (name.equalsIgnoreCase("pvp")) {
                    if (this.plugin.getPvpManager().players.contains(event.getPlayer().getUniqueId())) {
                        this.plugin.getPvpManager().players.remove(event.getPlayer().getUniqueId());
                    } else {
                        this.plugin.getPvpManager().players.add(event.getPlayer().getUniqueId());
                    }
                }
            }
        }
    }
}
