package com.drazisil.messy;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class Messy extends JavaPlugin {

    static final Logger logger = LogManager.getLogger();
    static Messy instance;
    static FileConfiguration config;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new MessyListener(), this);
        config = this.getConfig();
        config.addDefault("multiBlockCount", 1);
        config.options().copyDefaults(true);
        saveConfig();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        saveConfig();
    }

    static void updateTimes(int newVal) {
        config.set("multiBlockCount", newVal);
    }

    static int getMultiBlockCount() {
        int multiBlockCount = config.getInt("multiBlockCount");
        logger.info("multiBlockCount: " + multiBlockCount);
        return multiBlockCount;
    }
}
