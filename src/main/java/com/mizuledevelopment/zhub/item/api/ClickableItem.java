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
