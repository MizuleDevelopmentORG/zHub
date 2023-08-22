package com.mizuledevelopment.zhub.util.items;

import com.mizuledevelopment.zhub.util.item.ItemBuilder;
import com.mizuledevelopment.zhub.zHub;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Items {

    public static ItemStack SELECTOR(zHub plugin){
        ItemBuilder itemBuilder = new ItemBuilder(new ItemStack(Material.valueOf(plugin.getConfiguration().getString("selector.item"))));
        return null;
    }

    public static ItemStack ENDERBUTT(zHub plugin){
        return null;
    }

    public static ItemStack PVP(zHub plugin){
        return null;
    }
}
