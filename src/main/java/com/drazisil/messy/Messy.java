package com.drazisil.messy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Messy extends JavaPlugin {

    static final Logger logger = LogManager.getLogger();
    public static Messy instance;
    public static FileConfiguration config;
    static final String name = "Messy";

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new MessyListener(), this);
        config = this.getConfig();
        config.addDefault("multiBlockCount", 1);
        config.options().copyDefaults(true);
        saveConfig();

        // Register command manager
        this.getCommand("messy").setExecutor(new MessyCommands());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        saveConfig();
    }


}
