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
package com.mizuledevelopment.zhub.util.scoreboard;

/*
 * This file is part of FastBoard, licensed under the MIT License.
 *
 * Copyright (c) 2019-2021 MrMicky
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

import com.mizuledevelopment.zhub.util.reflect.FastReflection;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Array;
import java.lang.reflect.Method;

/**
 * {@inheritDoc}
 * @since 0.0.35
 * @author MrMicky
 */
@ApiStatus.AvailableSince("0.0.35")
public class FastBoard extends FastBoardBase<Component> {

    private static final MethodHandle COMPONENT_METHOD;
    private static final Object EMPTY_COMPONENT;
    private static final boolean ADVENTURE_SUPPORT;

    static {
        ADVENTURE_SUPPORT = FastReflection
                .optionalClass("io.papermc.paper.adventure.PaperAdventure")
                .isPresent();
        final MethodHandles.Lookup lookup = MethodHandles.lookup();

        try {
            if (ADVENTURE_SUPPORT) {
                final Class<?> paperAdventure = Class.forName("io.papermc.paper.adventure.PaperAdventure");
                final Method method = paperAdventure.getDeclaredMethod("asVanilla", Component.class);
                COMPONENT_METHOD = lookup.unreflect(method);
                EMPTY_COMPONENT = COMPONENT_METHOD.invoke(Component.empty());
            } else {
                final Class<?> craftChatMessageClass = FastReflection.obcClass("util.CraftChatMessage");
                COMPONENT_METHOD = lookup.unreflect(craftChatMessageClass.getMethod("fromString", String.class));
                EMPTY_COMPONENT = Array.get(COMPONENT_METHOD.invoke(""), 0);
            }
        } catch (final Throwable t) {
            throw new ExceptionInInitializerError(t);
        }
    }

    /**
     * {@inheritDoc}
     */
    public FastBoard(final Player player) {
        super(player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void sendLineChange(final int score) throws Throwable {
        final Component line = getLineByScore(score);

        sendTeamPacket(score, FastBoardBase.TeamMode.UPDATE, line, null);
    }

    @Override
    protected Object toMinecraftComponent(final Component component) throws Throwable {
        if (component == null) {
            return EMPTY_COMPONENT;
        }

        return COMPONENT_METHOD.invoke(component);
    }

    @Override
    protected Component emptyLine() {
        return Component.empty();
    }
}
