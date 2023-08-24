package com.mizuledevelopment.zhub;

import com.mizuledevelopment.zhub.command.SetSpawnCommand;
import com.mizuledevelopment.zhub.config.impl.ConfigFile;
import com.mizuledevelopment.zhub.item.api.HotbarHandler;
import com.mizuledevelopment.zhub.listener.player.PlayerListener;
import com.mizuledevelopment.zhub.listener.server.ServerListener;
import com.mizuledevelopment.zhub.pvp.PvPManager;
import com.mizuledevelopment.zhub.scoreboard.HubScoreboardAdapter;
import com.mizuledevelopment.zhub.scoreboard.ScoreboardHandler;
import com.mizuledevelopment.zhub.scoreboard.ScoreboardListener;
import com.mizuledevelopment.zhub.tab.TabHandler;
import com.mizuledevelopment.zhub.task.LocationTask;
import com.mizuledevelopment.zhub.util.color.Color;
import com.mizuledevelopment.zhub.util.command.manager.CommandManager;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class zHub extends JavaPlugin {

    private static zHub instance;
    private final Map<String, ConfigFile> configs = new HashMap<>();
    private PvPManager pvpManager;
    private TabHandler tabHandler;
    private final NamespacedKey namespacedKey = new NamespacedKey(this, "hub");
    private HotbarHandler hotbarHandler;

    @Override
    public void onEnable() {
        YamlConfiguration configuration = new YamlConfiguration();
        instance = this;
        final long time = System.currentTimeMillis();
        saveDefaultConfig();
        /*

         */
//        if (!new LicenseChecker(getDescription().getName()).check()) {
//            getServer().getPluginManager().disablePlugin(this);
//            return;
//        }
        this.command();
        this.tabHandler = new TabHandler(this);
        this.listener(Bukkit.getPluginManager());
        this.pvpManager = new PvPManager();
        this.hotbarHandler = new HotbarHandler(this);
        ScoreboardHandler.configure(new HubScoreboardAdapter(this));
        new ScoreboardHandler();
        new LocationTask(this).runTaskTimerAsynchronously(this, 0, 20);

        Bukkit.getConsoleSender().sendMessage(Color.translate("&8[&bzHub&8] &7Successfully enabled. It took me &b" + (System.currentTimeMillis() - time) + " &7ms"));
    }

    private void listener(final @NotNull PluginManager pluginManager) {
        Arrays.asList(
                new ServerListener(this),
                new PlayerListener(this),
                new ScoreboardListener()
        ).forEach(listener -> pluginManager.registerEvents(listener, this));
    }

    private void command() {
        CommandManager commandManager = new CommandManager(this.getCommand("zhub"));
        commandManager.addSubCommand(new SetSpawnCommand(this));
    }

    public static zHub instance() {
        return instance;
    }

    public TabHandler tabHandler() {
        return this.tabHandler;
    }

    public ConfigFile config(final String name) {
        return this.configs.computeIfAbsent(name, cfg -> {
            if (!cfg.endsWith(".yml")) cfg = cfg + ".yml";
            return new ConfigFile(cfg, getDataFolder().toPath(), zHub.class);
        });
    }

    public List<ConfigFile> configs() {
        return new ArrayList<>(this.configs.values());
    }

    public NamespacedKey getNamespacedKey() {
        return namespacedKey;
    }

    public PvPManager getPvpManager() {
        return pvpManager;
    }

    public HotbarHandler hotbarHandler() {
        return this.hotbarHandler;
    }
}
