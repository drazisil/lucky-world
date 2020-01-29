package com.drazisil.luckyworld.world;

import com.drazisil.luckyworld.BlockSave;
import com.drazisil.luckyworld.BlockSaveRecord;
import org.bukkit.*;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.potion.PotionEffectType;

import static com.drazisil.luckyworld.BlockSaveRecord.CenterType.CENTER_OFFSET_Y;
import static com.drazisil.luckyworld.shared.LWUtilities.locationToString;
import static org.bukkit.Bukkit.createWorld;
import static org.bukkit.Bukkit.dispatchCommand;

public class WorldHandler {

    private Location newSpawnLocation;

    private final World newWorld;


    public WorldHandler() {
        // https://hub.spigotmc.org/javadocs/spigot/org/bukkit/WorldCreator.html

        WorldCreator newWorldCreator = new WorldCreator("new_world");

        newWorldCreator.generator(new LWChunkGenerator());
        newWorldCreator.generateStructures(true);

        newWorld = createWorld(newWorldCreator);
        assert newWorld != null;

        newWorld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        newWorld.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        newWorld.setGameRule(GameRule.DO_MOB_SPAWNING, true);
        newWorld.setFullTime(100);
    }

    public Location getSpawnLocation() {
        return newSpawnLocation;
    }

    public void setNewSpawnLocation(Location newSpawnLocation) {
        this.newSpawnLocation = newSpawnLocation;
    }

    public void handleBlockBreakEvent(BlockBreakEvent event,
                                      Location location,
                                      Player player) {

        // Fetch spawn location
        Location spawnLoc = getSpawnLocation().clone();
        spawnLoc.setY(spawnLoc.getY() - 1);

        if (locationToString(location).equals(locationToString(spawnLoc))
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
        blocksToChange.generateBlockSaveCube(getSpawnLocation().clone(),
                1, 5, 5, CENTER_OFFSET_Y,  -1);

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