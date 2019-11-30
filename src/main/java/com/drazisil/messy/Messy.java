package com.drazisil.messy;

import org.bukkit.plugin.java.JavaPlugin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class Messy extends JavaPlugin {

    private static int times = 1;
    static final Logger logger = LogManager.getLogger();
    static Messy instance;

    @Override
    public void onEnable() {
            getServer().getPluginManager().registerEvents(new MyListener(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    static void updateTimes(int newVal) {
        times = newVal;
    }

    static int getTimes() {
        logger.info("multiBlockCount: " + times);
        return times;
    }
}
