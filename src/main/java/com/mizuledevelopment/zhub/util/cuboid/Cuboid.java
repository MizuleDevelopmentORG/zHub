package com.mizuledevelopment.zhub.util.cuboid;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Cuboid implements Iterable<Block>, Cloneable, ConfigurationSerializable {
    private static final int CHUNK_SIZE = 16;
    protected final String worldName;
    protected int x1;
    protected int y1;
    protected int z1;
    protected final int x2;
    protected int y2;
    protected final int z2;

    public Cuboid(final Map<String, Object> map) {
        this.worldName = (String) map.get("worldName");
        this.x1 = (Integer) map.get("x1");
        this.y1 = (Integer) map.get("y1");
        this.z1 = (Integer) map.get("z1");
        this.x2 = (Integer) map.get("x2");
        this.y2 = (Integer) map.get("y2");
        this.z2 = (Integer) map.get("z2");
    }

    public Cuboid(final World world, final int x1, final int y1, final int z1, final int x2, final int y2, final int z2) {
        this(((World) Preconditions.checkNotNull((Object) world)).getName(), x1, y1, z1, x2, y2, z2);
    }

    private Cuboid(@NotNull final String worldName, final int x1, final int y1, final int z1, final int x2, final int y2, final int z2) {
        Preconditions.checkNotNull((Object) worldName, "World name cannot be null");
        this.worldName = worldName;
        this.x1 = Math.min(x1, x2);
        this.y1 = Math.min(y1, y2);
        this.z1 = Math.min(z1, z2);
        this.x2 = Math.max(x1, x2);
        this.y2 = Math.max(y1, y2);
        this.z2 = Math.max(z1, z2);
    }

    public Cuboid(final Location first, final Location second) {
        Preconditions.checkNotNull(first, "Location 1 cannot be null");
        Preconditions.checkNotNull(second, "Location 2 cannot be null");
        Preconditions.checkArgument(first.getWorld().equals(second.getWorld()), "Locations must be on the same world");
        this.worldName = first.getWorld().getName();
        this.x1 = Math.min(first.getBlockX(), second.getBlockX());
        this.y1 = Math.min(first.getBlockY(), second.getBlockY());
        this.z1 = Math.min(first.getBlockZ(), second.getBlockZ());
        this.x2 = Math.max(first.getBlockX(), second.getBlockX());
        this.y2 = Math.max(first.getBlockY(), second.getBlockY());
        this.z2 = Math.max(first.getBlockZ(), second.getBlockZ());
    }

    public Cuboid(final Location location) {
        this(location, location);
    }

    public Cuboid(final Cuboid other) {
        this(other.getWorld().getName(), other.x1, other.y1, other.z1, other.x2, other.y2, other.z2);
    }

    // public Cuboid(final Document document) {
    //     this.worldName = document.getString("worldName");
    //     this.x1 = document.getInteger("x1");
    //     this.y1 = document.getInteger("y1");
    //     this.z1 = document.getInteger("z1");
    //     this.x2 = document.getInteger("x2");
    //     this.y2 = document.getInteger("y2");
    //     this.z2 = document.getInteger("z2");
    // }

    public Cuboid(final JsonObject document) {
        this.worldName = document.get("worldName").getAsString();
        this.x1 = document.get("x1").getAsInt();
        this.y1 = document.get("y1").getAsInt();
        this.z1 = document.get("z1").getAsInt();
        this.x2 = document.get("x2").getAsInt();
        this.y2 = document.get("y2").getAsInt();
        this.z2 = document.get("z2").getAsInt();
    }

    public Map<String, Object> serialize() {
        final LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("worldName", this.worldName);
        map.put("x1", this.x1);
        map.put("y1", this.y1);
        map.put("z1", this.z1);
        map.put("x2", this.x2);
        map.put("y2", this.y2);
        map.put("z2", this.z2);
        return map;
    }

    // public Document toBson() {
    //     final Document document = new Document();
    //     document.put("worldName", this.worldName);
    //     document.put("x1", this.x1);
    //     document.put("y1", this.y1);
    //     document.put("z1", this.z1);
    //     document.put("x2", this.x2);
    //     document.put("y2", this.y2);
    //     document.put("z2", this.z2);
    //     return document;
    // }

    // public JsonObject toJson() {
    //     return new JsonChain()
    //         .addProperty("worldName", this.worldName)
    //         .addProperty("x1", this.x1)
    //         .addProperty("y1", this.y1)
    //         .addProperty("z1", this.z1)
    //         .addProperty("x2", this.x2)
    //         .addProperty("y2", this.y2)
    //         .addProperty("z2", this.z2)
    //         .get();
    // }

    public boolean hasBothPositionsSet() {
        return this.getMinimumPoint() != null && this.getMaximumPoint() != null;
    }

    public int getMinimumX() {
        return Math.min(this.x1, this.x2);
    }

    public int getMinimumZ() {
        return Math.min(this.z1, this.z2);
    }

    public int getMaximumX() {
        return Math.max(this.x1, this.x2);
    }

    public int getMaximumZ() {
        return Math.max(this.z1, this.z2);
    }

    public List<Vector> edges() {
        final Location v1 = this.getMinimumPoint();
        final Location v2 = this.getMaximumPoint();
        final int minX = v1.getBlockX();
        final int maxX = v2.getBlockX();
        final int minZ = v1.getBlockZ();
        final int startX = minZ + 1;
        final int maxZ = v2.getBlockZ();
        int capacity = (maxX - minX) * 4 + (maxZ - minZ) * 4;
        final List<Vector> result = new ArrayList<>(capacity += 4);
        if (capacity <= 0) {
            return result;
        }
        final int minY = v1.getBlockY();
        final int maxY = v1.getBlockY();
        for (int x = minX; x <= maxX; ++x) {
            result.add(new Vector(x, minY, minZ));
            result.add(new Vector(x, minY, maxZ));
            result.add(new Vector(x, maxY, minZ));
            result.add(new Vector(x, maxY, maxZ));
        }
        for (int z = startX; z < maxZ; ++z) {
            result.add(new Vector(minX, minY, z));
            result.add(new Vector(minX, maxY, z));
            result.add(new Vector(maxX, minY, z));
            result.add(new Vector(maxX, maxY, z));
        }
        return result;
    }

    public Set<Player> getPlayers() {
        final Set<Player> players = new HashSet<>();
        for (final Player player : Bukkit.getOnlinePlayers()) {
            if (!this.contains(player)) continue;
            players.add(player);
        }
        return players;
    }

    public Location getLowerNE() {
        return new Location(this.getWorld(), this.x1, this.y1, this.z1);
    }

    public Location getUpperSW() {
        return new Location(this.getWorld(), this.x2, this.y2, this.z2);
    }

    public Location getCenter() {
        final int x1 = this.x2 + 1;
        final int y1 = this.y2 + 1;
        final int z1 = this.z2 + 1;
        return new Location(this.getWorld(), (double) this.x1 + (double) (x1 - this.x1) / 2.0,
            (double) this.y1 + (double) (y1 - this.y1) / 2.0, (double) this.z1 + (double) (z1 - this.z1) / 2.0);
    }

    public String getWorldName() {
        return this.worldName;
    }

    public World getWorld() {
        return Bukkit.getWorld(this.worldName);
    }

    public int getSizeX() {
        return this.x2 - this.x1 + 1;
    }

    public int getSizeY() {
        return this.y2 - this.y1 + 1;
    }

    public int getSizeZ() {
        return this.z2 - this.z1 + 1;
    }

    public int getX1() {
        return this.x1;
    }

    public void setX1(final int x1) {
        this.x1 = x1;
    }

    public int getY1() {
        return this.y1;
    }

    public void setY1(final int y1) {
        this.y1 = y1;
    }

    public int getZ1() {
        return this.z1;
    }

    public void setZ1(final int z1) {
        this.z1 = z1;
    }

    public int getX2() {
        return this.x2;
    }

    public int getY2() {
        return this.y2;
    }

    public void setY2(final int y2) {
        this.y2 = y2;
    }

    public int getZ2() {
        return this.z2;
    }

    public Location[] getCornerLocations() {
        final Location[] result = new Location[8];
        final Block[] cornerBlocks = this.getCornerBlocks();
        for (int i = 0; i < cornerBlocks.length; ++i) {
            result[i] = cornerBlocks[i].getLocation();
        }
        return result;
    }

    public Block[] getCornerBlocks() {
        final Block[] result = new Block[8];
        final World world = this.getWorld();
        result[0] = world.getBlockAt(this.x1, this.y1, this.z1);
        result[1] = world.getBlockAt(this.x1, this.y1, this.z2);
        result[2] = world.getBlockAt(this.x1, this.y2, this.z1);
        result[3] = world.getBlockAt(this.x1, this.y2, this.z2);
        result[4] = world.getBlockAt(this.x2, this.y1, this.z1);
        result[5] = world.getBlockAt(this.x2, this.y1, this.z2);
        result[6] = world.getBlockAt(this.x2, this.y2, this.z1);
        result[7] = world.getBlockAt(this.x2, this.y2, this.z2);
        return result;
    }

    public Cuboid shift(final CuboidDirection direction, final int amount) throws IllegalArgumentException {
        return this.expand(direction, amount).expand(direction.opposite(), -amount);
    }

    public Cuboid inset(final CuboidDirection direction, final int amount) throws IllegalArgumentException {
        return this.outset(direction, -amount);
    }

    public Cuboid expand(final CuboidDirection direction, final int amount) throws IllegalArgumentException {
        switch (direction) {
            case NORTH -> {
                return new Cuboid(this.worldName, this.x1 - amount, this.y1, this.z1, this.x2, this.y2, this.z2);
            }
            case SOUTH -> {
                return new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x2 + amount, this.y2, this.z2);
            }
            case EAST -> {
                return new Cuboid(this.worldName, this.x1, this.y1, this.z1 - amount, this.x2, this.y2, this.z2);
            }
            case WEST -> {
                return new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x2, this.y2, this.z2 + amount);
            }
            case DOWN -> {
                return new Cuboid(this.worldName, this.x1, this.y1 - amount, this.z1, this.x2, this.y2, this.z2);
            }
            case UP -> {
                return new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x2, this.y2 + amount, this.z2);
            }
        }
        throw new IllegalArgumentException("Invalid direction " + direction);
    }

    public Cuboid outset(final CuboidDirection direction, final int amount) throws IllegalArgumentException {
        switch (direction) {
            case HORIZONTAL -> {
                return this.expand(CuboidDirection.NORTH, amount).expand(CuboidDirection.SOUTH, amount).expand(CuboidDirection.EAST, amount).expand(CuboidDirection.WEST, amount);
            }
            case VERTICAL -> {
                return this.expand(CuboidDirection.DOWN, amount).expand(CuboidDirection.UP, amount);
            }
            case BOTH -> {
                return this.outset(CuboidDirection.HORIZONTAL, amount).outset(CuboidDirection.VERTICAL, amount);
            }
        }
        throw new IllegalArgumentException("Invalid direction " + direction);
    }

    public boolean contains(final Cuboid cuboid) {
        return this.contains(cuboid.getMinimumPoint()) || this.contains(cuboid.getMaximumPoint());
    }

    public boolean contains(final Entity entity) {
        return this.contains(entity.getLocation());
    }

    public boolean contains(final World world, final int x, final int z) {
        return (world == null || this.getWorld().equals(world)) && x >= this.x1 && x <= this.x2 && z >= this.z1 && z <= this.z2;
    }

    public boolean contains(final int x, final int y, final int z) {
        return x >= this.x1 && x <= this.x2 && y >= this.y1 && y <= this.y2 && z >= this.z1 && z <= this.z2;
    }

    public boolean contains(final Block block) {
        return this.contains(block.getLocation());
    }

    public boolean contains(final Location location) {
        if (location == null || this.worldName == null) {
            return false;
        }
        final World world = location.getWorld();
        return world != null && this.worldName.equals(location.getWorld().getName()) && this.contains(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public int getVolume() {
        return this.getSizeX() * this.getSizeY() * this.getSizeZ();
    }

    public int getArea() {
        final Location min = this.getMinimumPoint();
        final Location max = this.getMaximumPoint();
        return (max.getBlockX() - min.getBlockX() + 1) * (max.getBlockZ() - min.getBlockZ() + 1);
    }

    public byte getAverageLightLevel() {
        long total = 0;
        int count = 0;
        for (final Block block : this) {
            if (!block.isEmpty()) continue;
            total += block.getLightLevel();
            ++count;
        }
        return count > 0 ? (byte) (total / (long) count) : 0;
    }

    public Location getMinimumPoint() {
        return new Location(this.getWorld(), Math.min(this.x1, this.x2), Math.min(this.y1, this.y2), Math.min(this.z1, this.z2));
    }

    public Location getMaximumPoint() {
        return new Location(this.getWorld(), Math.max(this.x1, this.x2), Math.max(this.y1, this.y2), Math.max(this.z1, this.z2));
    }

    public int getWidth() {
        return this.getMaximumPoint().getBlockX() - this.getMinimumPoint().getBlockX();
    }

    public int getHeight() {
        return this.getMaximumPoint().getBlockY() - this.getMinimumPoint().getBlockY();
    }

    public int getLength() {
        return this.getMaximumPoint().getBlockZ() - this.getMinimumPoint().getBlockZ();
    }

    public Cuboid contract() {
        return this.contract(CuboidDirection.DOWN).contract(CuboidDirection.SOUTH).contract(CuboidDirection.EAST).contract(CuboidDirection.UP).contract(CuboidDirection.NORTH).contract(CuboidDirection.WEST);
    }

    public Cuboid contract(final CuboidDirection direction) {
        Cuboid face = this.getFace(direction.opposite());
        switch (direction) {
            case DOWN -> {
                while (face.containsOnly(Material.AIR) && face.y1 > this.y1) {
                    face = face.shift(CuboidDirection.DOWN, 1);
                }
                return new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x2, face.y2, this.z2);
            }
            case UP -> {
                while (face.containsOnly(Material.AIR) && face.y2 < this.y2) {
                    face = face.shift(CuboidDirection.UP, 1);
                }
                return new Cuboid(this.worldName, this.x1, face.y1, this.z1, this.x2, this.y2, this.z2);
            }
            case NORTH -> {
                while (face.containsOnly(Material.AIR) && face.x1 > this.x1) {
                    face = face.shift(CuboidDirection.NORTH, 1);
                }
                return new Cuboid(this.worldName, this.x1, this.y1, this.z1, face.x2, this.y2, this.z2);
            }
            case SOUTH -> {
                while (face.containsOnly(Material.AIR) && face.x2 < this.x2) {
                    face = face.shift(CuboidDirection.SOUTH, 1);
                }
                return new Cuboid(this.worldName, face.x1, this.y1, this.z1, this.x2, this.y2, this.z2);
            }
            case EAST -> {
                while (face.containsOnly(Material.AIR) && face.z1 > this.z1) {
                    face = face.shift(CuboidDirection.EAST, 1);
                }
                return new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x2, this.y2, face.z2);
            }
            case WEST -> {
                while (face.containsOnly(Material.AIR) && face.z2 < this.z2) {
                    face = face.shift(CuboidDirection.WEST, 1);
                }
                return new Cuboid(this.worldName, this.x1, this.y1, face.z1, this.x2, this.y2, this.z2);
            }
        }
        throw new IllegalArgumentException("Invalid direction " + direction);
    }

    public Cuboid getFace(final CuboidDirection direction) {
        switch (direction) {
            case DOWN -> {
                return new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x2, this.y1, this.z2);
            }
            case UP -> {
                return new Cuboid(this.worldName, this.x1, this.y2, this.z1, this.x2, this.y2, this.z2);
            }
            case NORTH -> {
                return new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x1, this.y2, this.z2);
            }
            case SOUTH -> {
                return new Cuboid(this.worldName, this.x2, this.y1, this.z1, this.x2, this.y2, this.z2);
            }
            case EAST -> {
                return new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x2, this.y2, this.z1);
            }
            case WEST -> {
                return new Cuboid(this.worldName, this.x1, this.y1, this.z2, this.x2, this.y2, this.z2);
            }
        }
        throw new IllegalArgumentException("Invalid direction " + direction);
    }

    public boolean containsOnly(final Material material) {
        for (final Block block : this) {
            if (block.getType() == material) continue;
            return false;
        }
        return true;
    }

    public Cuboid getBoundingCuboid(final Cuboid other) {
        if (other == null) {
            return this;
        }
        final int xMin = Math.min(this.x1, other.x1);
        final int yMin = Math.min(this.y1, other.y1);
        final int zMin = Math.min(this.z1, other.z1);
        final int xMax = Math.max(this.x2, other.x2);
        final int yMax = Math.max(this.y2, other.y2);
        final int zMax = Math.max(this.z2, other.z2);
        return new Cuboid(this.worldName, xMin, yMin, zMin, xMax, yMax, zMax);
    }

    public Block getRelativeBlock(final int x, final int y, final int z) {
        return this.getWorld().getBlockAt(this.x1 + x, this.y1 + y, this.z1 + z);
    }

    public Block getRelativeBlock(final World world, final int x, final int y, final int z) {
        return world.getBlockAt(this.x1 + x, this.y1 + y, this.z1 + z);
    }

    public List<Chunk> getChunks() {
        final World world = this.getWorld();
        final int x1 = this.x1 & -16;
        final int x2 = this.x2 & -16;
        final int z1 = this.z1 & -16;
        final int z2 = this.z2 & -16;
        final ArrayList<Chunk> result = new ArrayList<>(x2 - x1 + 16 + (z2 - z1) * 16);
        for (int x = x1; x <= x2; x += 16) {
            for (int z = z1; z <= z2; z += 16) {
                result.add(world.getChunkAt(x >> 4, z >> 4));
            }
        }
        return result;
    }

    @Override
    public Iterator<Block> iterator() {
        return new CuboidBlockIterator(this.getWorld(), this.x1, this.y1, this.z1, this.x2, this.y2, this.z2);
    }

    public Iterator<Location> locationIterator() {
        return new CuboidLocationIterator(this.getWorld(), this.x1, this.y1, this.z1, this.x2, this.y2, this.z2);
    }

    public Cuboid clone() {
        try {
            return (Cuboid) super.clone();
        } catch (final CloneNotSupportedException ex) {
            throw new RuntimeException("This could never happen", ex);
        }
    }

    public String toString() {
        return "Cuboid: " + this.worldName + ',' + this.x1 + ',' + this.y1 + ',' + this.z1 + "=>" + this.x2 + ',' + this.y2 + ',' + this.z2;
    }

}
