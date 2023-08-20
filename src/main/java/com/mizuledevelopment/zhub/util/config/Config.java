package com.mizuledevelopment.zhub.util.config;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

@Deprecated(forRemoval = true)
public class Config {

    private final Plugin plugin;
    private final File file;
    private final YamlConfiguration configuration;
    private final String directory;

    public Config(final Plugin plugin, final File file, final YamlConfiguration configuration, final String directory) {
        this.plugin = plugin;
        this.file = file;
        this.configuration = configuration;
        this.directory = directory;
    }

    public void create() {
        if (!(this.file.exists())) {
            this.file.getParentFile().mkdir();
            this.plugin.saveResource(this.directory, false);
        }
        try {
            this.configuration.load(this.file);
        } catch (final IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public void save() {
        try {
            this.configuration.save(this.file);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }

        try {
            this.configuration.load(this.file);
        } catch (final IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public YamlConfiguration getConfiguration() {
        return this.configuration;
    }

    public File getFile() {
        return this.file;
    }

    public String getDirectory() {
        return this.directory;
    }
}