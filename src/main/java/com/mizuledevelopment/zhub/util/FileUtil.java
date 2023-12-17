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
package com.mizuledevelopment.zhub.util;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

public final class FileUtil {

    private FileUtil() {
    }

    public static Path copyFromJar(
            final @NotNull Class<?> clazz,
            final @NotNull String pathFile,
            final @NotNull Path output
    ) throws IOException {
        if (Files.exists(output)) {
            return output;
        } else {
            try (final InputStream in = Objects.requireNonNull(clazz.getClassLoader().getResourceAsStream(pathFile),
                    "path does not exist in jar: " + pathFile)) {
                Files.copy(in, output, StandardCopyOption.REPLACE_EXISTING);
                return output;
            }
        }
    }
}
