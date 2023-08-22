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