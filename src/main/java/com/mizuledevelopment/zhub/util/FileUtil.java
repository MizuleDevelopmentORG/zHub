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