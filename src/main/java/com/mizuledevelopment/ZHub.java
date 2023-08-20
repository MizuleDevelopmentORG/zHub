package com.mizuledevelopment;

import com.mizuledevelopment.util.color.Color;
import com.mizuledevelopment.util.config.Config;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public final class ZHub extends JavaPlugin {

    private Config tabConfig;
    private Config settingsConfig;
    private static Chat chat = null;


    @Override
    public void onEnable() {
        long time = System.currentTimeMillis();

        this.setupChat();
        this.configuration();
        this.command();
        this.listener(Bukkit.getPluginManager());

        Bukkit.getConsoleSender().sendMessage(Color.translate("&8[&bzHub&8] &7Successfully enabled. It took me &b" + (System.currentTimeMillis() - time) + " &7ms"));
    }

    private void configuration(){
        settingsConfig = new Config(this, new File(getDataFolder(), "settings.yml"), new YamlConfiguration(), "settings.yml");
        settingsConfig.create();

        tabConfig = new Config(this, new File(getDataFolder(), "tab.yml"), new YamlConfiguration(), "tab.yml");
        tabConfig.create();
    }

    private void listener(@NotNull PluginManager pluginManager){

    }

    private void command(){

    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }

    public YamlConfiguration getSettings(){
        return this.settingsConfig.getConfiguration();
    }

    public YamlConfiguration getTab(){
        return this.tabConfig.getConfiguration();
    }
}
