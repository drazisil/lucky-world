package com.drazisil.messy.event;

import com.drazisil.messy.BlockSave;
import com.drazisil.messy.Messy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.*;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;


public class LuckyEventDisco implements LuckyEvent {


    @Override
    public void doAction(BlockBreakEvent event, World world, Location location, Player player) {

        Location standingLocation = location;

        ArrayList<ArrayList<BlockSave>> oldStateMatrix = new ArrayList<>();

        ArrayList<ArrayList<Block>> newBlockMatrix = new ArrayList<>();

        // Drop it by one
        standingLocation.setY(standingLocation.getY() - 1);

        Location startLocation = standingLocation;
        startLocation.setX(startLocation.getX() - 3);
        startLocation.setZ(startLocation.getZ() - 3);

        Location cursorLocation = startLocation;

        double startX = startLocation.getX();
        double startZ = startLocation.getZ();

        for (int z = 0; z <= 6; z++) {
            cursorLocation.setZ(startZ + z);
            ArrayList<BlockSave> blockRow = new ArrayList<>();
            for (int x = 0; x <= 6; x++) {
                cursorLocation.setX(startX + x);

                Block block = cursorLocation.getBlock();
                Material type = block.getType();
                BlockState state = block.getState();

                blockRow.add(new BlockSave(block, type, cursorLocation, state));
            }
            oldStateMatrix.add(blockRow);

        }

        for (int z = 0; z <= 6; z++) {
            cursorLocation.setZ(startZ + z);
            ArrayList<Block> blockRow = new ArrayList<>();
            for (int x = 0; x <= 6; x++) {
                cursorLocation.setX(startX + x);
                blockRow.add(cursorLocation.getBlock());
            }
            newBlockMatrix.add(blockRow);

        }

        // ============================================


        for (int z = 0; z <= 6; z++) {
            cursorLocation.setX(startZ + z);
            for (int x = 0; x <= 6; x++) {
                cursorLocation.setX(startX + x);
                Block block = newBlockMatrix.get(z).get(x);


                clearBlockInventory(block);


                block.setType(Material.REDSTONE_LAMP);
            }

        }



        Messy plugin = Messy.getInstance();

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            for (int z = 0; z <= 6; z++) {
                for (int x = 0; x <= 6; x++) {
                    newBlockMatrix.get(z).get(x).setType(oldStateMatrix.get(z).get(x).getType());
                    oldStateMatrix.get(z).get(x).getState().update();
                }

            }
        }, 400L);

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
            case DROPPER:
            case BARREL:
            case HOPPER:
                ((Dispenser) block.getState()).getInventory().clear();
                break;
            case LECTERN:
                ((Lectern) block.getState()).getInventory().clear();
                break;
        }
    }
}
