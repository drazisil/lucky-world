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
import org.bukkit.scheduler.BukkitTask;

import static com.drazisil.luckyworld.BlockSaveRecord.CenterType.CENTER_OFFSET_Y;


public class LuckyEventLavaFloor extends LuckyEvent {


    private final LuckyWorld plugin = LuckyWorld.getInstance();

    private double lavaFloorY;

    private BukkitTask raiseLavaLevelTask;


    @Override
    public void doAction(BlockBreakEvent event, World world, Location location, Player player) {


        // Drop it by one
        Location startLocation = location.clone();
        startLocation.setY(location.getY() - 1);

        int height = 9;
        int width = 9;
        int depth = 9;

        // Saved blocks
        BlockSaveRecord savedBlocks = new BlockSaveRecord();
        savedBlocks.generateBlockSaveCube(location.clone(),
                height, width, depth, CENTER_OFFSET_Y,  -3);


        // Blocks to change
        BlockSaveRecord blocksToChange
                = new BlockSaveRecord();
        blocksToChange.generateBlockSaveCube(location.clone(),
                height, width, depth, CENTER_OFFSET_Y, -3);


        for (BlockSave blockSave: blocksToChange.getBlocks()) {
            clearBlockInventory(blockSave.getBlock());
            blockSave.getBlock().setType(Material.AIR);
        }

        for (BlockSave blockSave: blocksToChange.getBlocks()) {
            Location savedLocation = blockSave.getBlock().getLocation();
            Location playerLocation = startLocation.clone();


            if (compareFloorLocation(savedLocation, playerLocation)) {

                blockSave.getBlock().setType(Material.BARRIER);
            }


        }

        lavaFloorY = blocksToChange.getBottomSideY();


        raiseLavaLevelTask = Bukkit.getScheduler().runTaskTimer(plugin,
                () -> raiseLavaLevel(blocksToChange, startLocation.clone()), 0, 20);


        Bukkit.getScheduler().scheduleSyncDelayedTask(
                plugin, () -> savedBlocks.restoreAll(blocksToChange), 320);

    }

    private void raiseLavaLevel(BlockSaveRecord blocksToChange, Location playerLocation) {
        for (BlockSave blockSave: blocksToChange.getBlocks()) {

            if (checkLocationY(blockSave.getLocation(), getLavaFloorY())) {
                blockSave.getBlock().setType(Material.LAVA);
            }

            if (compareFloorLocation(blockSave.getLocation(), playerLocation)) {

                blockSave.getBlock().setType(Material.BARRIER);
            }




        }
        if (lavaFloorY == Math.floor(playerLocation.getY())) {
            raiseLavaLevelTask.cancel();
            resetLavaFloorY();

        }
      incLavaFloorY();
    }



    private boolean compareFloorDouble(double d1, double d2) {
        return Math.floor(d1) == Math.floor(d2);
    }

    private boolean compareFloorLocation(Location l1, Location l2) {
        return (compareFloorDouble(l1.getX(), l2.getX())
                && compareFloorDouble(l1.getY(), l2.getY())
                && compareFloorDouble(l1.getZ(), l2.getZ()));
    }

    private boolean checkLocationY(Location l1, double y) {
        return (compareFloorDouble(l1.getY(), y));
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

    private double getLavaFloorY() {
        return this.lavaFloorY;
    }

    private void incLavaFloorY() {
        this.lavaFloorY++;
    }

    private void resetLavaFloorY() {
        this.lavaFloorY = 0.0;
    }

}
