package com.mizuledevelopment.util.item;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ItemBuilder {

    private final ItemStack itemStack;

    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public void setName(String name) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(name);
        itemStack.setItemMeta(meta);
    }

    public void addLoreLine(String loreLine) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.getLore().add(loreLine);
        itemStack.setItemMeta(meta);
    }

    public void setLore(List<String> lore) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
    }

    public void setAmount(int amount) {
        this.itemStack.setAmount(amount);
    }

    public void addEnchantment(Enchantment enchantment, int power) {
        this.itemStack.addUnsafeEnchantment(enchantment, power);
    }

    public void setEnchantments(Map<Enchantment, Integer> enchantments) {
        enchantments.forEach(this.itemStack::addUnsafeEnchantment);
    }

    public void addFlag(ItemFlag itemFlag) {
        this.itemStack.addItemFlags(itemFlag);
    }

    public void setFlags(List<ItemFlag> itemFlags) {
        itemFlags.forEach(this.itemStack::addItemFlags);
    }

    public ItemStack build(){ return this.itemStack; }
}