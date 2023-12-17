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
package com.mizuledevelopment.zhub.util.text;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public enum MessageType {
    GSON(GsonComponentSerializer.gson()),
    LEGACY(LegacyComponentSerializer.legacyAmpersand()),
    LEGACY_SECTION(LegacyComponentSerializer.legacySection()),
    PLAIN(PlainTextComponentSerializer.plainText()),
    MINIMESSAGE(MiniMessage.miniMessage());

    private final ComponentSerializer<?, ?, ?> serializer;

    MessageType(final ComponentSerializer<?, ?, ?> serializer) {
        this.serializer = serializer;
    }

    /**
     * If the input starts with a curly brace and ends with a curly brace, it's a
     * GSON message. If it contains a section character, it's a legacy message. If
     * it matches the legacy pattern, it's a legacy message. If it matches the RGB
     * pattern, it's a legacy message. Otherwise, it's a minimessage
     *
     * @param input The input string to check
     * @return The {@link MessageType} enum
     */
    public static MessageType from(final String input) {
        if (input != null && !input.isBlank() && input.charAt(0) == '{' && input.charAt(input.length() - 1) == '}')
            return MessageType.GSON;
        else if (input != null && !input.isBlank() && input.charAt(0) == '&')
            return MessageType.LEGACY;
        else if (input != null && !input.isBlank() && input.contains(LegacyComponentSerializer.SECTION_CHAR + ""))
            return MessageType.LEGACY_SECTION;
        else
            return MessageType.MINIMESSAGE;
    }

    public ComponentSerializer<?, ?, ?> getSerializer() {
        return this.serializer;
    }

    @Override
    public String toString() {
        return "MessageType{" +
                "serializer=" + this.serializer +
                '}';
    }
}
