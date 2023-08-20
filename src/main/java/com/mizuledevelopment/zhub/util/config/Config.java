package com.mizuledevelopment.zhub.util.config;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class Config {

    private final Plugin plugin;
    private final File file;
    private final YamlConfiguration configuration;
    private final String directory;

    public Config(Plugin plugin, File file, YamlConfiguration configuration, String directory) {
        this.plugin = plugin;
        this.file = file;
        this.configuration = configuration;
        this.directory = directory;
    }

    public void create(){
        if (!(this.file.exists())) {
            this.file.getParentFile().mkdir();
            plugin.saveResource(this.directory, false);
        }
        try {
            configuration.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(){
        try {
            this.configuration.save(this.file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            this.configuration.load(this.file);
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public YamlConfiguration getConfiguration() {
        return configuration;
    }

    public File getFile() {
        return file;
    }

    public String getDirectory() {
        return directory;
    }
}