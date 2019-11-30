package com.drazisil.messy;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

public final class Messy extends JavaPlugin {

    static final Logger logger = LogManager.getLogger();
    static Messy instance;
    static FileConfiguration config;
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

    static void setMultiBlockCount(int val) {
        config.set("multiBlockCount", val);
    }

    static int getMultiBlockCount() {
        int multiBlockCount = config.getInt("multiBlockCount");
        return multiBlockCount;
    }

    static boolean shouldBang() {
        int number = new Random().nextInt(1000000);
        return String.valueOf(number).contains("4");
    }

    static void bang(World world, Location location) {
        world.spawnEntity(new Location(world, location.getX(), location.getY() + 10, location.getZ()), EntityType.PRIMED_TNT);
    }
}
