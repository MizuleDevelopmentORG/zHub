package com.mizuledevelopment.zhub;

import com.mizuledevelopment.zhub.command.SetSpawnCommand;
import com.mizuledevelopment.zhub.listener.player.PlayerListener;
import com.mizuledevelopment.zhub.listener.server.ServerListener;
import com.mizuledevelopment.zhub.pvp.PvPManager;
import com.mizuledevelopment.zhub.scoreboard.ScoreboardHandler;
import com.mizuledevelopment.zhub.tab.TabHandler;
import com.mizuledevelopment.zhub.util.LicenseChecker;
import com.mizuledevelopment.zhub.util.color.Color;
import com.mizuledevelopment.zhub.util.command.manager.CommandManager;
import com.mizuledevelopment.zhub.util.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Arrays;

@SuppressWarnings("ALL")
public final class zHub extends JavaPlugin {

    private static zHub instance;

    private Config tabConfig;
    private Config settingsConfig;
    private Config configConfiguration;
    private Config messagesConfig;

    private PvPManager pvPManager;
    private TabHandler tabHandler;

    @Override
    public void onEnable() {
        instance = this;
        final long time = System.currentTimeMillis();
        new LicenseChecker();

        this.configuration();
        this.command();
        this.tabHandler = new TabHandler(this);
        new ScoreboardHandler();
        this.listener(Bukkit.getPluginManager());
        this.pvPManager = new PvPManager();

        Bukkit.getConsoleSender().sendMessage(Color.translate("&8[&bzHub&8] &7Successfully enabled. It took me &b" + (System.currentTimeMillis() - time) + " &7ms"));
    }

    private void configuration() {
        this.settingsConfig = new Config(this, new File(getDataFolder(), "settings.yml"), new YamlConfiguration(), "settings.yml");
        this.settingsConfig.create();

        this.tabConfig = new Config(this, new File(getDataFolder(), "tab.yml"), new YamlConfiguration(), "tab.yml");
        this.tabConfig.create();

        this.configConfiguration = new Config(this, new File(getDataFolder(), "config.yml"), new YamlConfiguration(), "config.yml");
        this.configConfiguration.create();

        this.messagesConfig = new Config(this, new File(getDataFolder(), "messages.yml"), new YamlConfiguration(), "messages.yml");
        this.messagesConfig.create();
    }

    private void listener(final @NotNull PluginManager pluginManager) {
        Arrays.asList(
                new ServerListener(this),
                new PlayerListener(this)
        ).forEach(listener -> pluginManager.registerEvents(listener, this));
    }

    private void command() {
        CommandManager commandManager = new CommandManager(this.getCommand("zhub"));
        commandManager.addSubCommand(new SetSpawnCommand(this));
    }

    public YamlConfiguration getSettings() {
        return this.settingsConfig.getConfiguration();
    }

    public YamlConfiguration getTab() {
        return this.tabConfig.getConfiguration();
    }

    public static zHub instance() {
        return instance;
    }

    public Config tabConfig() {
        return this.tabConfig;
    }

    public Config settingsConfig() {
        return this.settingsConfig;
    }

    public TabHandler tabHandler() {
        return this.tabHandler;
    }

    public YamlConfiguration getConfiguration() {
        return configConfiguration.getConfiguration();
    }

    public Config getConfigConfiguration() {
        return configConfiguration;
    }

    public YamlConfiguration getMessages(){
        return messagesConfig.getConfiguration();
    }
}
