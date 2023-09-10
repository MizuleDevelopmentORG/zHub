package com.mizuledevelopment.zhub.util.location;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Represents a {@link Location} but doesn't parse the location until it is requested via {@link LazyLocation#location()}.
 */
public class LazyLocation {
    private final double x;
    private final double y;
    private final double z;
    private final float yaw;
    private final float pitch;
    private String world;
    private String worldName;

    public LazyLocation(
        final String worldId, final String worldName, final double x, final double y, final double z, final float yaw,
        final float pitch
    ) {
        this.world = worldId;
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public static @NotNull LazyLocation fromLocation(
        @NotNull
        final Location location
    ) {
        //noinspection ConstantConditions
        return new LazyLocation(location.getWorld().getUID().toString(), location.getWorld().getName(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    public String world() {
        return this.world;
    }

    public String worldName() {
        return this.worldName;
    }

    public double x() {
        return this.x;
    }

    public double y() {
        return this.y;
    }

    public double z() {
        return this.z;
    }

    public float yaw() {
        return this.yaw;
    }

    public float pitch() {
        return this.pitch;
    }

    public @Nullable Location location() {
        if (this.world == null || this.world.isEmpty()) {
            return null;
        }

        World world = null;

        try {
            final UUID worldId = UUID.fromString(this.world);
            world = Bukkit.getWorld(worldId);
        } catch (final IllegalArgumentException ignored) {
        }

        if (world == null) {
            world = Bukkit.getWorld(this.world);
        }

        if (world == null && this.worldName != null && !this.worldName.isBlank()) {
            world = Bukkit.getWorld(this.worldName);
        }

        if (world == null) {
            return null;
        }

        this.world = world.getUID().toString();
        this.worldName = world.getName();

        return new Location(world, this.x, this.y, this.z, this.yaw, this.pitch);
    }
}
