package com.mizuledevelopment.zhub.util;

import com.mizuledevelopment.zhub.zHub;
import org.bukkit.NamespacedKey;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Keys {

    private static final Map<String, NamespacedKey> KEYS = new HashMap<>();

    private Keys() {
    }

    public static NamespacedKey getKey(final String key) {
        return KEYS.computeIfAbsent(key, k -> new NamespacedKey(zHub.instance(), k));
    }
}
