package com.mizuledevelopment.zhub.config.util;

import com.mizuledevelopment.zhub.config.IConfig;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class ConfigurateUtil {
    private ConfigurateUtil() {
    }

    public static Set<String> getRootNodeKeys(final IConfig<? extends ConfigurationNode, ? extends ConfigurationNode> config) {
        return getKeys(config.handle());
    }

    public static Set<String> getKeys(final ConfigurationNode node) {
        if (node == null || !node.isMap()) {
            return Collections.emptySet();
        }

        final Set<String> keys = new LinkedHashSet<>();
        for (final Object obj : node.childrenMap().keySet()) {
            keys.add(String.valueOf(obj));
        }
        return keys;
    }

    public static Map<String, ConfigurationNode> getMap(final ConfigurationNode node) {
        if (node == null || !node.isMap()) {
            return Collections.emptyMap();
        }

        final Map<String, ConfigurationNode> map = new LinkedHashMap<>();
        for (final Map.Entry<Object, ? extends ConfigurationNode> entry : node.childrenMap().entrySet()) {
            map.put(String.valueOf(entry.getKey()), entry.getValue());
        }
        return map;
    }

    public static Map<String, Object> getRawMap(final IConfig<? extends ConfigurationNode, ? extends ConfigurationNode> config, final String key) {
        if (config == null || key == null) {
            return Collections.emptyMap();
        }
        return getRawMap(config.section(key));
    }

    public static Map<String, Object> getRawMap(final ConfigurationNode node) {
        if (node == null || !node.isMap()) {
            return Collections.emptyMap();
        }

        final Map<String, Object> map = new LinkedHashMap<>();
        for (final Map.Entry<Object, ? extends ConfigurationNode> entry : node.childrenMap().entrySet()) {
            map.put(String.valueOf(entry.getKey()), entry.getValue().raw());
        }
        return map;
    }

    public static List<Map<?, ?>> getMapList(final ConfigurationNode node) {
        List<?> list = null;
        try {
            list = node.getList(Object.class);
        } catch (final SerializationException ignored) {
        }
        final List<Map<?, ?>> result = new ArrayList<>();

        if (list == null) {
            return result;
        }

        for (final Object object : list) {
            if (object instanceof Map) {
                result.add((Map<?, ?>) object);
            }
        }
        return result;
    }

    public static BigDecimal toBigDecimal(final String input, final BigDecimal def) {
        if (input == null || input.isEmpty()) {
            return def;
        }

        try {
            return new BigDecimal(input, MathContext.DECIMAL128);
        } catch (final NumberFormatException | ArithmeticException e) {
            return def;
        }
    }

    public static boolean isDouble(final ConfigurationNode node) {
        return node != null && node.raw() instanceof Double;
    }

    public static boolean isInt(final ConfigurationNode node) {
        return node != null && node.raw() instanceof Integer;
    }

    public static boolean isString(final ConfigurationNode node) {
        return node != null && node.raw() instanceof String;
    }
}
