package com.drazisil.messy.util;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;

import java.util.Random;

public class Utilities {
    /**
     * Set the value of val to the FileConfiguration object
     *
     * @param config A FileConfiguration object
     * @param val The new value to set
     */
    public static void setMultiBlockCount(FileConfiguration config, int val) {
        config.set("multiBlockCount", val);
    }

    /**
     * Return the value of multiBlockCount from the FileConfiguration object
     *
     * @param config a FileConfiguration object
     * @return int
     */
    public static int getMultiBlockCount(FileConfiguration config) {
        int multiBlockCount = config.getInt("multiBlockCount");
        return multiBlockCount;
    }

    public static void setBangMax(FileConfiguration config, int val) {
        config.set("bangMax", val);
    }

    /**
     * Return the value ofbangMax from the FileConfiguration object
     *
     * @param config a FileConfiguration object
     * @return int
     */
    public static int getBangMax(FileConfiguration config) {
        int bangMax = config.getInt("bangMax");
        return bangMax;
    }

    /**
     * Tests a Random Int from 1 to 20 million and returns true if it contains magicNumber
     *
     * @param magicNumber The number Random number should be to return true
     * @return boolean
     */
    public static boolean shouldBang(FileConfiguration config, int magicNumber) {
        int number = new Random().nextInt(config.getInt("bangMax"));
        return number == magicNumber;
    }

    /**
     * Passes a default magicNumber to shouldBang()
     *
     * @return boolean
     */
    public static boolean shouldBang(FileConfiguration config) {
        return shouldBang(config, 4);
    }

    /**
     *  Spawns a PRIMED_TNT Entity 10 blocks above the target Location
     *
     * @param world The world to spawn the Entity in
     * @param player The Player who should spawn this
     * @param location The Location to spawn the Entity above
     */
    public static void bang(World world, Player player, Location location) {
        Location newLocation = new Location(world, location.getX(), location.getY() + 10, location.getZ());
        player.sendMessage("Uh...");
        world.spawn(location, TNTPrimed.class);
    }
}
