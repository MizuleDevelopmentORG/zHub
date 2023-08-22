package com.mizuledevelopment.zhub.util.item;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;

@Deprecated
public class ItemBuilder {

    private final ItemStack itemStack;

    public ItemBuilder(final ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public void setName(final String name) {
        final ItemMeta meta = this.itemStack.getItemMeta();
        meta.setDisplayName(name);
        this.itemStack.setItemMeta(meta);
    }

    public void addLoreLine(final String loreLine) {
        final ItemMeta meta = this.itemStack.getItemMeta();
        meta.getLore().add(loreLine);
        this.itemStack.setItemMeta(meta);
    }

    public void setLore(final List<String> lore) {
        final ItemMeta meta = this.itemStack.getItemMeta();
        meta.setLore(lore);
        this.itemStack.setItemMeta(meta);
    }

    public void setAmount(final int amount) {
        this.itemStack.setAmount(amount);
    }

    public void addEnchantment(final Enchantment enchantment, final int power) {
        this.itemStack.addUnsafeEnchantment(enchantment, power);
    }

    public void setEnchantments(final Map<Enchantment, Integer> enchantments) {
        enchantments.forEach(this.itemStack::addUnsafeEnchantment);
    }

    public void addFlag(final ItemFlag itemFlag) {
        this.itemStack.addItemFlags(itemFlag);
    }

    public void setFlags(final List<ItemFlag> itemFlags) {
        itemFlags.forEach(this.itemStack::addItemFlags);
    }

    public ItemStack build() {
        return this.itemStack;
    }
}