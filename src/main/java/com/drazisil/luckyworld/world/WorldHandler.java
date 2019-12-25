package com.drazisil.luckyworld.world;

import com.drazisil.luckyworld.BlockSave;
import com.drazisil.luckyworld.BlockSaveRecord;
import com.drazisil.luckyworld.LuckyWorld;
import org.bukkit.*;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.potion.PotionEffectType;

import java.util.logging.Logger;

import static com.drazisil.luckyworld.BlockSaveRecord.CenterType.CENTER_OFFSET_Y;
import static org.bukkit.Bukkit.createWorld;
import static org.bukkit.Bukkit.dispatchCommand;

public class WorldHandler {

    private LuckyWorld plugin = LuckyWorld.getInstance();
    private Logger logger = plugin.getLogger();

    private Location newSpawnLocation;

    private World newWorld;


    public WorldHandler() {
        // https://hub.spigotmc.org/javadocs/spigot/org/bukkit/WorldCreator.html

        WorldCreator newWorldCreator = new WorldCreator("new_world");

//        newWorldCreator.generator(new LWChunkGenerator());

        // Dubug things
        String generatorSettings = newWorldCreator.generatorSettings();
        logger.info("generator settings: " + generatorSettings);

        ChunkGenerator chunkGenerator = newWorldCreator.generator();
        logger.info("chunk generator: " + String.valueOf(chunkGenerator));

        WorldType worldType = newWorldCreator.type();
        logger.info(String.valueOf("worldtype: " + worldType));

        World.Environment worldEnv = newWorldCreator.environment();
        logger.info("worldenv: " + String.valueOf(worldEnv));

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
        Location spawnLoc = getSpawnLocation().clone();
        spawnLoc.setY(spawnLoc.getY() - 1);

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

        BlockSaveRecord blocksToChange
                = new BlockSaveRecord();
        blocksToChange.generateBlockSaveCube(newWorld, getSpawnLocation().clone(),
                1, 5, 5, CENTER_OFFSET_Y, 0, -1, 0);

        for (BlockSave blockSave: blocksToChange.getBlocks()) {
            blockSave.getBlock().setType(Material.QUARTZ_BLOCK);
        }

        Location returnBlockLocation = getSpawnLocation().clone();
        returnBlockLocation.setY(returnBlockLocation.getY() - 1);
        Block returnBlock = newWorld.getBlockAt(returnBlockLocation);
        returnBlock.setType(Material.GREEN_STAINED_GLASS);

        // Create beacon
        Location beaconBlockLocation = returnBlockLocation.clone();
        beaconBlockLocation.setY(beaconBlockLocation.getY() - 1);

        Block beaconBlock = newWorld.getBlockAt(beaconBlockLocation);
        beaconBlock.setType(Material.BEACON);
        Beacon beaconState = (Beacon) beaconBlock.getState();
        beaconState.setPrimaryEffect(PotionEffectType.JUMP);

        // Open the sky
        for (int i = (int) (getSpawnLocation().getY() + 1); i < newWorld.getMaxHeight(); i++) {
            newWorld.getBlockAt(getSpawnLocation().getBlockX(), i, getSpawnLocation().getBlockZ()).setType(Material.AIR);
        }

    }


}