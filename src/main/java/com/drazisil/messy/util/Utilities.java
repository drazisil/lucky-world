package com.drazisil.messy.util;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

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
     * Tests a Random Int from 1 to 20 million and returns true if it contains magicNumber
     *
     * @param magicNumber The number Random number should be to return true
     * @return boolean
     */
    public static boolean shouldFortune(FileConfiguration config, int magicNumber) {
        int number = new Random().nextInt(config.getInt("fortuneMax"));
        return number == magicNumber;
    }

    public static boolean shouldEvent() {
        int number = randInt(100);
        return number == 42;
    }

    /**
     * Passes a default magicNumber to shouldFortube()
     *
     * @return boolean
     */
    public static boolean shouldFortune(FileConfiguration config) {
        return shouldFortune(config, 4);
    }

    /**
     *  Spawns a Entity at the target Location
     *
     * @param world The world to spawn the Entity in
     * @param player The Player who should spawn this
     * @param location The Location to spawn the Entity above
     */
    public static void bang(World world, Player player, Location location, EntityType entity) {
        player.sendMessage("Hmm...");
        world.spawnEntity(location, entity);
    }

    /**
     *  Spawns a Entity at the target Location
     *
     * @param world The world to spawn the Entity in
     * @param player The Player who should spawn this
     * @param location The Location to spawn the Entity above
     */
    public static Entity fortune(World world, Player player, Location location, EntityType entityType) {
        player.sendMessage("Uh...");
        return world.spawnEntity(location, entityType);
    }

    public static int randInt(int max) {
        return new Random().nextInt(max);
    }


}
