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
package com.mizuledevelopment.zhub.config.util;

import org.spongepowered.configurate.serialize.TypeSerializer;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConfigurateSerializers {

    public static final Map<Class<?>, TypeSerializer> serializerMap = new ConcurrentHashMap<>();

    public static Map<Class<?>, TypeSerializer> getSerializerMap() {
        return serializerMap;
    }

    public static <T> void register(final Class<? super T> clazz, final TypeSerializer<? super T> serializer) {
        serializerMap.put(clazz, serializer);
    }

    public static TypeSerializerCollection getCollection() {
        final var builder = TypeSerializerCollection.builder();
        for (final var thing : serializerMap.entrySet()) {
            builder.register(thing.getKey(), thing.getValue());
        }
        return builder.build();
    }
}
