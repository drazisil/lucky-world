package com.drazisil.luckyworld.world;

import com.drazisil.luckyworld.BlockSave;
import com.drazisil.luckyworld.BlockSaveRecord;
import com.drazisil.luckyworld.LuckyWorld;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.potion.PotionEffectType;

import static com.drazisil.luckyworld.BlockSaveRecord.CenterType.CENTER;
import static org.bukkit.Bukkit.*;

public class WorldHandler {

    private LuckyWorld plugin = LuckyWorld.getInstance();

    private Location newSpawnLocation;

    private World newWorld;


    public WorldHandler() {
        // https://hub.spigotmc.org/javadocs/spigot/org/bukkit/WorldCreator.html

        WorldCreator newWorldCreator
                = new WorldCreator("new_world").copy(getWorld("world_nether"));

        //        newWorldCreator.type(WorldType.BUFFET);

        newWorld = createWorld(newWorldCreator);

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

        if (plugin.locationToString(location).equals(plugin.locationToString(spawnLoc))
                && event.getBlock().getType() == Material.GREEN_STAINED_GLASS) {
            dispatchCommand(player, "execute in overworld run tp 0 64 0");
            player.removePotionEffect(PotionEffectType.CONDUIT_POWER);
            player.removePotionEffect(PotionEffectType.DOLPHINS_GRACE);
            player.setInvulnerable(false);
            event.setCancelled(true);
        }
    }

    public void generateSpawnPlatform() {
        
        // TODO: Make platform unbreakable

        Block spawnBlock = newWorld.getBlockAt(getSpawnLocation());

        BlockSaveRecord blocksToChange
                = new BlockSaveRecord();
        blocksToChange.generateBlockSaveCube(newWorld, getSpawnLocation().clone(),
                1, 5, 5, CENTER, 0, 0, 0);

        for (BlockSave blockSave: blocksToChange.getBlocks()) {
            blockSave.getBlock().setType(Material.QUARTZ_BLOCK);
        }

        spawnBlock.setType(Material.GREEN_STAINED_GLASS);

        // Create beacon
        Location beaconBlockLocation = getSpawnLocation().clone();
        beaconBlockLocation.setY(beaconBlockLocation.getY() - 1);

        Block beaconBlock = newWorld.getBlockAt(beaconBlockLocation);
        beaconBlock.setType(Material.BEACON);

        // Open the sky
        for (int i = (int) (getSpawnLocation().getY() + 1); i < newWorld.getMaxHeight(); i++) {
            newWorld.getBlockAt(getSpawnLocation().getBlockX(), i, getSpawnLocation().getBlockZ()).setType(Material.AIR);
        }

    }


}