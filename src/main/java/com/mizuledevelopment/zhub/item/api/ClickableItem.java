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
package com.mizuledevelopment.zhub.item.api;

import com.mizuledevelopment.zhub.util.callback.ReturnCallable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ClickableItem {

    private final ClickHandler clickHandler;
    private final ReturnCallable<Player, ItemStack> itemStack;
    private final boolean droppable, moveable, placeable;

    public ClickableItem(
            final ClickHandler clickHandler, final ReturnCallable<Player, ItemStack> itemStack, final boolean droppable,
            final boolean moveable, final boolean placeable
    ) {
        this.clickHandler = clickHandler;
        this.itemStack = itemStack;
        this.droppable = droppable;
        this.moveable = moveable;
        this.placeable = placeable;
    }

    public ClickHandler clickHandler() {
        return this.clickHandler;
    }

    public ReturnCallable<Player, ItemStack> itemStack() {
        return this.itemStack;
    }

    public boolean droppable() {
        return this.droppable;
    }

    public boolean moveable() {
        return this.moveable;
    }

    public boolean placeable() {
        return this.placeable;
    }

}
