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
package com.mizuledevelopment.zhub.util.items;

import com.mizuledevelopment.zhub.config.impl.ConfigFile;
import com.mizuledevelopment.zhub.util.text.MessageType;
import com.mizuledevelopment.zhub.util.text.TextUtil;
import com.mizuledevelopment.zhub.zHub;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Items {

    public static ItemStack SELECTOR(zHub plugin){
        ConfigFile config = plugin.config("config");
        ItemStack itemStack = new ItemStack(Material.valueOf(config.getString("selector.item")));
        itemStack.setAmount(config.getInt("selector.amount"));
        config.getStringList("selector.enchantments")
                .forEach(enchantment -> itemStack.addUnsafeEnchantment(Objects.requireNonNull(Enchantment.getByName(enchantment.split(":")[0])),
                        Integer.parseInt(enchantment.split(":")[1])));
        ItemMeta meta = itemStack.getItemMeta();
        meta.getPersistentDataContainer().set(plugin.getNamespacedKey(), PersistentDataType.STRING, "selector");
        meta.displayName(TextUtil.parse(config.getString("selector.name"), MessageType.from(config.getString("selector.name"))));
        List<Component> components = new ArrayList<>();
        config.getStringList("selector.lore").forEach(line -> components.add(TextUtil.parse(line, MessageType.from(line))));
        meta.lore(components);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static ItemStack ENDERBUTT(zHub plugin){
        ConfigFile config = plugin.config("config");
        ItemStack itemStack = new ItemStack(Material.valueOf(config.getString("enderbutt.item")));
        itemStack.setAmount(config.getInt("enderbutt.amount"));
        config.getStringList("enderbutt.enchantments")
                .forEach(enchantment -> itemStack.addUnsafeEnchantment(Objects.requireNonNull(Enchantment.getByName(enchantment.split(":")[0])),
                        Integer.parseInt(enchantment.split(":")[1])));
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(TextUtil.parse(config.getString("enderbutt.name"), MessageType.from(config.getString("enderbutt.name"))));
        List<Component> components = new ArrayList<>();
        config.getStringList("enderbutt.lore").forEach(line -> components.add(TextUtil.parse(line, MessageType.from(line))));
        meta.lore(components);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static ItemStack PVP(zHub plugin){
        ConfigFile config = plugin.config("config");
        ItemStack itemStack = new ItemStack(Material.valueOf(config.getString("pvp.item")));
        itemStack.setAmount(config.getInt("pvp.amount"));
        config.getStringList("pvp.enchantments")
                .forEach(enchantment -> itemStack.addUnsafeEnchantment(Objects.requireNonNull(Enchantment.getByName(enchantment.split(":")[0])),
                        Integer.parseInt(enchantment.split(":")[1])));
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(TextUtil.parse(config.getString("pvp.name"), MessageType.from(config.getString("pvp.name"))));
        List<Component> components = new ArrayList<>();
        config.getStringList("pvp.lore").forEach(line -> components.add(TextUtil.parse(line, MessageType.from(line))));
        meta.lore(components);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static ItemStack HUB(zHub plugin) {
        ConfigFile config = plugin.config("config");
        ItemStack itemStack = new ItemStack(Material.valueOf(config.getString("hub.item")));
        itemStack.setAmount(config.getInt("hub.amount"));
        config.getStringList("hub.enchantments")
                .forEach(enchantment -> itemStack.addUnsafeEnchantment(Objects.requireNonNull(Enchantment.getByName(enchantment.split(":")[0])),
                        Integer.parseInt(enchantment.split(":")[1])));
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(TextUtil.parse(config.getString("hub.name"), MessageType.from(config.getString("hub.name"))));
        List<Component> components = new ArrayList<>();
        config.getStringList("hub.lore").forEach(line -> components.add(TextUtil.parse(line, MessageType.from(line))));
        meta.lore(components);
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
