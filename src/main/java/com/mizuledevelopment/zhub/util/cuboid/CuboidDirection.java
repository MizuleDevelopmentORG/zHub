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
package com.mizuledevelopment.zhub.util.cuboid;

import org.bukkit.block.BlockFace;

public enum CuboidDirection {
    NORTH, EAST, SOUTH, WEST, UP, DOWN, HORIZONTAL, VERTICAL, BOTH, UNKNOWN;

    public CuboidDirection opposite() {
        switch (this) {
            case NORTH -> {
                return CuboidDirection.SOUTH;
            }
            case EAST -> {
                return CuboidDirection.WEST;
            }
            case SOUTH -> {
                return CuboidDirection.NORTH;
            }
            case WEST -> {
                return CuboidDirection.EAST;
            }
            case HORIZONTAL -> {
                return CuboidDirection.VERTICAL;
            }
            case VERTICAL -> {
                return CuboidDirection.HORIZONTAL;
            }
            case UP -> {
                return CuboidDirection.DOWN;
            }
            case DOWN -> {
                return CuboidDirection.UP;
            }
            case BOTH -> {
                return CuboidDirection.BOTH;
            }
            default -> {
                return CuboidDirection.UNKNOWN;
            }
        }
    }

    public BlockFace toBukkitDirection() {
        switch (this) {
            case NORTH -> {
                return BlockFace.NORTH;
            }
            case EAST -> {
                return BlockFace.EAST;
            }
            case SOUTH -> {
                return BlockFace.SOUTH;
            }
            case WEST -> {
                return BlockFace.WEST;
            }
            case UP -> {
                return BlockFace.UP;
            }
            case DOWN -> {
                return BlockFace.DOWN;
            }
            default -> {
                return null;
            }
        }
    }
}
