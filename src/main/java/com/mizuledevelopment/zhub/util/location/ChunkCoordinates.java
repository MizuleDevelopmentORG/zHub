package com.mizuledevelopment.zhub.util.location;

/**
 * Represents a pair of X, Z chunk coordinates
 */
public record ChunkCoordinates(int x, int z) {

    @Override
    public String toString() {
        return "ChunkCoordinates{" + "x=" + this.x + ", z=" + this.z + '}';
    }
}
