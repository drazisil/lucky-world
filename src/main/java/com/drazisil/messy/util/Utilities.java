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

    /**
     * Tests a Random Int from 0 to 1 million and returns true if it contains magicNumber
     *
     * @param length The length the Random number should be to return true
     * @return boolean
     */
    public static boolean shouldBang(int length) {
        int number = new Random().nextInt(1000000);
        return String.valueOf(number).length() == length;
    }

    /**
     * Passes a default magicNumber to shouldBang()
     *
     * @return boolean
     */
    public static boolean shouldBang() {
        return shouldBang(2);
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
        player.sendMessage("Look up!");
        world.spawn(newLocation, TNTPrimed.class);
    }
}
