/*
 * This file is part of Lobby, licensed under the MIT License.
 *
 * Copyright (c) 2023 powercas_gamer
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.mizuledevelopment.zhub.item;

import broccolai.corn.paper.item.PaperItemBuilder;
import broccolai.corn.paper.item.special.BookBuilder;
import com.mizuledevelopment.zhub.zHub;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import com.mizuledevelopment.zhub.item.api.ClickableItem;
import com.mizuledevelopment.zhub.item.api.Hotbar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static net.kyori.adventure.text.Component.*;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextColor.*;

public class HubItems extends Hotbar {

    private final Map<UUID, Long> lastClick = new HashMap<>();
    private final ConfigurationSection config;

    /**
     * Hotbar.
     *
     * @param id    of hotbar.
     * @param lobby instance.
     */
    public HubItems(final String id, final zHub lobby) {
        super(id, lobby);
        this.config = zHub.instance().getConfig().getConfigurationSection("items");
        initItems();
    }

    @Override
    public Map<Integer, ClickableItem> itemsToApply(final @NotNull Player player) {
        final Map<Integer, ClickableItem> items = new HashMap<>();
        final boolean hasTag = player.getPersistentDataContainer().getOrDefault(NamespacedKey.fromString("lobby:hide"), PersistentDataType.BOOLEAN, Boolean.TRUE);

        if (this.config.getBoolean("compass")) items.put(4, getCachedItem("compass"));
        if (this.config.getBoolean("info")) items.put(0, getCachedItem("book"));
        if (this.config.getBoolean("enderbutt")) items.put(7, getCachedItem("enderbutt"));
        if (this.config.getBoolean("visibility")) {
            if (hasTag) {
                items.put(8, getCachedItem("visibility:true"));
            } else {
                items.put(8, getCachedItem("visibility:false"));
            }
        }
        return items;
    }

    /**
     * Add cached items.
     */
    private void initItems() {
        addCachedItem("compass", new ClickableItem(
                player -> {
                    // new ServerSelectorMenu().openMenu(player);
                },
                player -> PaperItemBuilder.ofType(Material.COMPASS)
                        .name(disableItalics(text("sᴇʀᴠᴇʀ sᴇʟᴇᴄᴛᴏʀ", color(0x4d1214))))
                        .build(),
                false,
                false,
                false
        ));
        addCachedItem("book", new ClickableItem(
                player -> {
                    for (final String line : Lobby.instance().getConfig().getStringList("server-info.message")) {
                        player.sendRichMessage(line);
                    }
                },
                player -> BookBuilder.of(new ItemStack(Material.WRITTEN_BOOK))
                        .author(text("MineOrbit"))
                        .name(text("sᴇʀᴠᴇʀ ɪɴғᴏʀᴍᴀᴛɪᴏɴ", color(0xff3b41)))
                        .pages(List.of(Component.join(JoinConfiguration.newlines(), List.of(
                                text("Welcome to MineOrbit!"),
                                text("The Best Server!")
                        ))))
                        .generation(BookMeta.Generation.ORIGINAL)
                        .build(),
                false,
                false,
                false
        ));
        addCachedItem("visibility:true", new ClickableItem(
                player -> {
                    if (!canUseHide(player)) return;
                    player.getPersistentDataContainer().set(NamespacedKey.fromString("lobby:hide"), PersistentDataType.BOOLEAN, Boolean.FALSE);
                    player.sendRichMessage("<white>Player Visibility: <red>Disabled");
                    VisibilityService.update(player);
                    HubItems.this.applyToPlayer(player, true);
                    HubItems.this.lastClick.put(player.getUniqueId(), System.currentTimeMillis());
                },
                player -> {
                    final boolean hasTag = player.getPersistentDataContainer().getOrDefault(NamespacedKey.fromString("lobby:hide"), PersistentDataType.BOOLEAN, Boolean.FALSE);
                    return PaperItemBuilder.ofType(Material.RED_DYE)
                            .name(disableItalics(text("ʜɪᴅᴇ ᴘʟᴀʏᴇʀs", color(0xff3b41))))
                            .build();
                },
                false,
                false,
                false
        ));
        addCachedItem("visibility:false", new ClickableItem(
                player -> {
                    if (!canUseHide(player)) return;
                    player.getPersistentDataContainer().set(NamespacedKey.fromString("lobby:hide"), PersistentDataType.BOOLEAN, Boolean.TRUE);
                    player.sendRichMessage("<white>Player Visibility: <green>Enabled");
                    VisibilityService.update(player);
                    HubItems.this.applyToPlayer(player, true);
                    HubItems.this.lastClick.put(player.getUniqueId(), System.currentTimeMillis());
                },
                player -> {
                    return PaperItemBuilder.ofType(Material.LIME_DYE)
                            .name(disableItalics(text("sʜᴏᴡ ᴘʟᴀʏᴇʀs", color(0xff3b41))))
                            .build();
                },
                false,
                false,
                false
        ));
        addCachedItem("pvp", new ClickableItem(
                null,
                player -> PaperItemBuilder.ofType(Material.DIAMOND_SWORD)
                        .name(disableItalics(text("ᴘᴠᴘ sᴡᴏʀᴅ", color(0xff3b41))))
                        .build(),
                false,
                false,
                false
        ));
        addCachedItem("enderbutt", new ClickableItem(
                player -> {
                    if (player.getVehicle() != null) player.getVehicle().remove();
                    player.getWorld().spawn(player.getLocation().add(0.5, 0.5, 0.5), EnderPearl.class, enderPearl -> {
                        enderPearl.addPassenger(player);
                        enderPearl.setVelocity(player.getLocation().getDirection().multiply(5.5F));
                    }, CreatureSpawnEvent.SpawnReason.CUSTOM);
                    player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                },
                player -> PaperItemBuilder.ofType(Material.ENDER_PEARL)
                        .name(text("ᴇɴᴅᴇʀ ʙᴜᴛᴛ", color(0xff3b41)))
                        .build(),
                false,
                false,
                false
        ));
    }

    private boolean canUseHide(final Player p) {
        if (this.lastClick.get(p.getUniqueId()) == null) {
            return true;
        }
        if (this.lastClick.get(p.getUniqueId()) + 5000 <= System.currentTimeMillis())
            return true;

        p.sendMessage(text()
                        .append(text("Please wait", RED))
                        .append(Component.space())
                        .append(text(TimeUtil.formatHHMMSS((this.lastClick.get(p.getUniqueId()) + 5000 - System.currentTimeMillis()), true), RED))
                        .append(text(" to toggle player visibility!", RED))
        );
        return false;
    }
}
