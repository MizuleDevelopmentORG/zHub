package com.mizuledevelopment.zhub.util.location;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationNode;

/**
 * Utilities for {@link Location}s
 */
public final class LocationUtil {

    private LocationUtil() {
    }

    /**
     * Returns a {@link ChunkCoordinates} object for the given location
     *
     * @param location Location
     * @return ChunkCoordinates for the given Location
     */
    public static ChunkCoordinates getChunkCoordinates(final Location location) {
        final int cx = location.getBlockX() >> 4;
        final int cz = location.getBlockZ() >> 4;
        return new ChunkCoordinates(cx, cz);
    }

    /**
     * Returns a {@link ChunkCoordinates} object for the given x and z coordinates
     *
     * @param x X coordinate
     * @param z Z coordinate
     * @return ChunkCoordinates for the given Location
     */
    public static ChunkCoordinates getChunkCoordinates(final int x, final int z) {
        return new ChunkCoordinates(x >> 4, z >> 4);
    }

    public static @NotNull Location center(
        @NotNull
        final Location location
    ) {
        return location.clone().add(0.5, 0, 0.5);
    }

    public static @NotNull LazyLocation parse(final ConfigurationNode node) {
        return new LazyLocation(
            node.node("worldId").getString(),
            node.node("world").getString(),
            node.node("x").getDouble(),
            node.node("y").getDouble(),
            node.node("z").getDouble(),
            node.node("yaw").getFloat(),
            node.node("pitch").getFloat()
        );
    }
}
