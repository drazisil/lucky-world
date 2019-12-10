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


    private Messy plugin = Messy.getInstance();

    @Override
    public void doAction(BlockBreakEvent event, World world, Location location, Player player) {



//        ArrayList<ArrayList<BlockSave>> oldStateMatrix = new ArrayList<>();

//        ArrayList<ArrayList<Block>> newBlockMatrix = new ArrayList<>();

        // Drop it by one
        Location startLocation1 = location.clone();
        startLocation1.setY(location.getY() - 1);
        startLocation1.setX(startLocation1.getX() - 3);
        startLocation1.setZ(startLocation1.getZ() - 3);

//        System.out.println("Location: " + location.toString());
//        System.out.println("startLocation1: " + startLocation1.toString());
        ArrayList<ArrayList<BlockSave>> oldStateMatrix1 = generateBlockSaveMatrix(startLocation1);

//        Location standingLocation2 = location.clone();
//        standingLocation2.setY(standingLocation2.getY() - 1);
//
//        Location startLocation2 = standingLocation2.clone();
//        startLocation2.setX(startLocation2.getX() - 3);
//        startLocation2.setZ(startLocation2.getZ() - 3);
//
//
//        System.out.println("Location: " + location.toString());
//        System.out.println("startLocation2: " + startLocation2.toString());
        ArrayList<ArrayList<Block>> newBlockMatrix1 = generateBlockMatrix(startLocation1);

        // Drop it by one
        Location startLocation2 = location.clone();
        startLocation2.setY(location.getY() - 2);
        startLocation2.setX(startLocation2.getX() - 3);
        startLocation2.setZ(startLocation2.getZ() - 3);

        ArrayList<ArrayList<BlockSave>> oldStateMatrix2 = generateBlockSaveMatrix(startLocation2);
        ArrayList<ArrayList<Block>> newBlockMatrix2 = generateBlockMatrix(startLocation2);


        // ============================================


        Location cursorLocation1 = startLocation1.clone();

        double startX1 = startLocation1.getX();
        double startZ1 = startLocation1.getZ();

//
//        System.out.println("Location: " + location.toString());
//        System.out.println("cursorLocation1: " + cursorLocation1.toString());


        for (int z = 0; z <= 6; z++) {
            cursorLocation1.setX(startZ1 + z);
            for (int x = 0; x <= 6; x++) {
                cursorLocation1.setX(startX1 + x);
                Block block = newBlockMatrix1.get(z).get(x);


                clearBlockInventory(block);


                block.setType(Material.GLASS);

            }

        }


        Location cursorLocation2 = startLocation2.clone();

        double startX2 = startLocation2.getX();
        double startZ2 = startLocation2.getZ();

//
//        System.out.println("Location: " + location.toString());
//        System.out.println("cursorLocation1: " + cursorLocation1.toString());


        for (int z = 0; z <= 6; z++) {
            cursorLocation2.setX(startZ2 + z);
            for (int x = 0; x <= 6; x++) {
                cursorLocation2.setX(startX2 + x);
                Block block = newBlockMatrix2.get(z).get(x);


                clearBlockInventory(block);


                block.setType(Material.SEA_LANTERN);

            }

        }



//        Bukkit.getScheduler().scheduleSyncDelayedTask(
//
//                plugin, () -> {
//                    for (int z = 0; z <= 6; z++) {
//                        cursorLocation.setX(startZ + z);
//                        for (int x = 0; x <= 6; x++) {
//                            cursorLocation.setX(startX + x);
//                            Block block = newBlockMatrix.get(z).get(x);
//
//                            BlockState state = block.getState();
//
//                            Lightable data = ((Lightable) state.getBlockData());
//                            data.setLit(true);
//                            state.setBlockData(data);
//                            state.update(true, false);
//                        }
//
//                    }
//                }, 40L);

        Bukkit.getScheduler().scheduleSyncDelayedTask(
                plugin, () -> restoreBlockState(newBlockMatrix1, oldStateMatrix1), 300L);

        Bukkit.getScheduler().scheduleSyncDelayedTask(
                plugin, () -> restoreBlockState(newBlockMatrix2, oldStateMatrix2), 300L);

    }

    private ArrayList<ArrayList<Block>> generateBlockMatrix(Location startLocation) {
        ArrayList<ArrayList<Block>> blockMatrix = new ArrayList<>();

        double startX = startLocation.getX();
        double startZ = startLocation.getZ();
        Location cursorLocation = startLocation.clone();
        for (int z = 0; z <= 6; z++) {
            cursorLocation.setZ(startZ + z);
            ArrayList<Block> blockRow = new ArrayList<>();
            for (int x = 0; x <= 6; x++) {
                cursorLocation.setX(startX + x);
                blockRow.add(cursorLocation.getBlock());
            }
            blockMatrix.add(blockRow);

        }
        return blockMatrix;
    }

    private ArrayList<ArrayList<BlockSave>> generateBlockSaveMatrix(Location startLocation) {
        ArrayList<ArrayList<BlockSave>> blockSaveMatrix = new ArrayList<>();

        double startX = startLocation.getX();
        double startZ = startLocation.getZ();
        Location cursorLocation = startLocation.clone();
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
            blockSaveMatrix.add(blockRow);

        }
        return  blockSaveMatrix;
    }

    public void restoreBlockState(ArrayList<ArrayList<Block>> blockMatrix,
                                  ArrayList<ArrayList<BlockSave>> blockSaveMatrix) {
        for (int z = 0; z <= 6; z++) {
            for (int x = 0; x <= 6; x++) {
                blockMatrix.get(z).get(x).setType(blockSaveMatrix.get(z).get(x).getType());
                blockSaveMatrix.get(z).get(x).getState().update();
            }

        }
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
