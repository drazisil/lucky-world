package com.drazisil.luckyworld.world;

import com.drazisil.luckyworld.LuckyWorld;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.potion.PotionEffectType;

import static org.bukkit.Bukkit.createWorld;
import static org.bukkit.Bukkit.dispatchCommand;

public class WorldHandler {

    private LuckyWorld plugin = LuckyWorld.getInstance();

    private Location newSpawnLocation;


    public WorldHandler() {
        // https://hub.spigotmc.org/javadocs/spigot/org/bukkit/WorldCreator.html
        WorldCreator newWorldCreator = new WorldCreator("new_world");
//        newWorldCreator.type(WorldType.BUFFET);

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

    public void handleBlockBreakEvent(BlockBreakEvent event,
                                      World world,
                                      Location location,
                                      Player player) {

        // Fetch spawn location
        Location spawnLoc = getSpawnLocation();

        System.out.println(plugin.locationToString(location) +
                " = " + plugin.locationToString(spawnLoc) +
                " - " + event.getBlock().getType());

        if (plugin.locationToString(location).equals(plugin.locationToString(spawnLoc))
                && event.getBlock().getType() == Material.EMERALD_BLOCK) {
            dispatchCommand(player, "execute in overworld run tp 0 64 0");
            player.removePotionEffect(PotionEffectType.CONDUIT_POWER);
            player.removePotionEffect(PotionEffectType.DOLPHINS_GRACE);
            player.setInvulnerable(false);
            event.setCancelled(true);
        }
    }


}