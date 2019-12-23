package com.drazisil.luckyworld.event;

import com.drazisil.luckyworld.BlockSave;
import com.drazisil.luckyworld.BlockSaveRecord;
import com.drazisil.luckyworld.LuckyWorld;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.*;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import static com.drazisil.luckyworld.BlockSaveRecord.CenterType.CENTER;


public class LuckyEventLavaFloor extends LuckyEvent {


    private LuckyWorld plugin = LuckyWorld.getInstance();


    @Override
    public void doAction(BlockBreakEvent event, World world, Location location, Player player) {


        // Drop it by one
        Location startLocation = location.clone();
        startLocation.setY(location.getY() - 1);

        int height = 7;

        // Saved blocks
        BlockSaveRecord savedBlocks = new BlockSaveRecord();
        savedBlocks.generateBlockSaveCube(world, location.clone(),
                height, 7, 7, CENTER, 0, 0, 0);


        // Blocks to change
        BlockSaveRecord blocksToChange
                = new BlockSaveRecord();
        blocksToChange.generateBlockSaveCube(world, location.clone(),
                height, 7, 7, CENTER, 0, 0, 0);


        for (BlockSave blockSave: blocksToChange.getBlocks()) {
            clearBlockInventory(blockSave.getBlock());
            blockSave.getBlock().setType(Material.AIR);
        }

        for (BlockSave blockSave: blocksToChange.getBlocks()) {
            Location savedLocation = blockSave.getBlock().getLocation();
            Location playerLocation = startLocation.clone();

            if (savedLocation.getY() <= startLocation.getY()) {

                if (Math.floor(savedLocation.getX()) == Math.floor(playerLocation.getX())
                        && Math.floor(savedLocation.getZ()) == Math.floor(playerLocation.getZ())) {
                    System.out.println("cobble " + savedLocation);
                    System.out.println("cobble!" + location.clone());
                    blockSave.getBlock().setType(Material.COBBLESTONE);
                } else {
                    blockSave.getBlock().setType(Material.LAVA);
                }

            }

        }


        Bukkit.getScheduler().scheduleSyncDelayedTask(
                plugin, () -> savedBlocks.restoreAll(blocksToChange), 320);

    }

    private void clearBlockInventory(Block block) {

        // Attempt to clear drops if campfire
        if ((block.getType() == Material.CAMPFIRE)) {
            Campfire state = ((Campfire) block.getState());
            state.setItem(0, null);
            state.setItem(1, null);
            state.setItem(2, null);
            state.setItem(3, null);
            state.update();

        }


        // Attempt to clear drops if jukebox
        if ((block.getType() == Material.JUKEBOX)) {
            ((Jukebox)block.getState()).setRecord(null);
        }

        // Attempt to clear drops if furnace
        if ((block.getType() == Material.FURNACE)
                || (block.getType() == Material.BLAST_FURNACE)
                || (block.getType() == Material.SMOKER)) {
            ((Furnace)block.getState()).getInventory().setFuel(null);
            ((Furnace)block.getState()).getInventory().setResult(null);
            ((Furnace)block.getState()).getInventory().setSmelting(null);
        }

        // Attempt to clear drops if chest
        if ((block.getType() == Material.CHEST)
                || (block.getType() == Material.TRAPPED_CHEST)) {
            ((Chest) block.getState()).getBlockInventory().clear();
        }

        // Attempt to clear drops if brewing stand
        if ((block.getType() == Material.BREWING_STAND)) {
            ((BrewingStand) block.getState()).getInventory().setFuel(null);
            ((BrewingStand) block.getState()).getInventory().setIngredient(null);
            ((BrewingStand) block.getState()).getInventory().clear();
        }


        // Attempt to clear drops if container
        switch (block.getType()) {
            case DISPENSER:
                ((Dispenser) block.getState()).getInventory().clear();
                break;
            case BARREL:
                ((Barrel) block.getState()).getInventory().clear();
                break;
            case HOPPER:
                ((Hopper) block.getState()).getInventory().clear();
                break;
            case DROPPER:
                ((Dropper) block.getState()).getInventory().clear();
                break;
            case LECTERN:
                ((Lectern) block.getState()).getInventory().clear();
                break;
        }
    }
}
