package com.drazisil.luckyworld.world;

import org.bukkit.*;

import static org.bukkit.Bukkit.createWorld;

public class WorldHandler {

    private Location newSpawnLocation;


    public WorldHandler() {
        // https://hub.spigotmc.org/javadocs/spigot/org/bukkit/WorldCreator.html
        WorldCreator newWorldCreator = new WorldCreator("new_world");
        newWorldCreator.type(WorldType.BUFFET);

        World newWorld = createWorld(newWorldCreator);

        newWorld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        newWorld.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
//        newWorld.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        newWorld.setFullTime(100);
    }

    public Location getSpawnLocation() {
        return newSpawnLocation;
    }

    public void setNewSpawnLocation(Location newSpawnLocation) {
        this.newSpawnLocation = newSpawnLocation;
    }
}
