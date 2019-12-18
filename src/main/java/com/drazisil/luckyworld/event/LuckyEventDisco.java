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

import java.util.ArrayList;

import static com.drazisil.luckyworld.event.LWEventHandler.getRandomMaterial;


public class LuckyEventDisco extends LuckyEvent {


    public static ArrayList<Material> coloredGlassBlocks = new ArrayList<Material>();


    public LuckyEventDisco() {
        coloredGlassBlocks.add(Material.RED_STAINED_GLASS);
        coloredGlassBlocks.add(Material.LIME_STAINED_GLASS);
        coloredGlassBlocks.add(Material.YELLOW_STAINED_GLASS);
        coloredGlassBlocks.add(Material.LIGHT_BLUE_STAINED_GLASS);
        coloredGlassBlocks.add(Material.MAGENTA_STAINED_GLASS);
        coloredGlassBlocks.add(Material.ORANGE_STAINED_GLASS);
        coloredGlassBlocks.add(Material.PURPLE_STAINED_GLASS);
    }


    private LuckyWorld plugin = LuckyWorld.getInstance();

    private int colorChangeCount = 0;

    public int getColorChangeCount() {
        return  this.colorChangeCount;
    }

    public void incColorChangeCount() {
        this.colorChangeCount++;
    }

    public void resetColorChangeCount() {
        this.colorChangeCount = 0;
    }

    private BukkitTask setGlassColorTask;

    @Override
    public void doAction(BlockBreakEvent event, World world, Location location, Player player) {


        // Drop it by one
        Location startLocation1 = location.clone();
        startLocation1.setY(location.getY() - 1);
        startLocation1.setX(startLocation1.getX() - 3);
        startLocation1.setZ(startLocation1.getZ() - 3);

        BlockSaveRecord savedBlocks = new BlockSaveRecord();
        savedBlocks.generateBlockSaveCube(world, location.clone(),
                7, 7, 7);

        BlockSaveRecord blocksToChange
                = new BlockSaveRecord();
        blocksToChange.generateBlockSaveCube(world, location.clone(),
                7, 7, 7);

        for (BlockSave blockSave: blocksToChange.getBlocks()) {
            blockSave.getBlock().setType(Material.COBWEB);
        }

        player.getServer().broadcastMessage("Start XYZ: " + savedBlocks.getLeftSideX()
                + " " +  savedBlocks.getTopSideY() + " " + savedBlocks.getFrontSideZ());

        player.getServer().broadcastMessage("End XYZ: " + savedBlocks.getRightSideX()
                + " " +  savedBlocks.getBottomSideY() + " " + savedBlocks.getBackSideZ());


        ArrayList<ArrayList<Block>> newBlockMatrix1 = generateBlockMatrix(startLocation1);

        // Drop it by one
        Location startLocation2 = location.clone();
        startLocation2.setY(location.getY() - 2);
        startLocation2.setX(startLocation2.getX() - 3);
        startLocation2.setZ(startLocation2.getZ() - 3);

        ArrayList<ArrayList<Block>> newBlockMatrix2 = generateBlockMatrix(startLocation2);


        // ============================================


        Location cursorLocation2 = startLocation2.clone();

        double startX2 = startLocation2.getX();
        double startZ2 = startLocation2.getZ();

        for (int z = 0; z <= 6; z++) {
            cursorLocation2.setX(startZ2 + z);
            for (int x = 0; x <= 6; x++) {
                cursorLocation2.setX(startX2 + x);
                Block block = newBlockMatrix2.get(z).get(x);


                clearBlockInventory(block);


                block.setType(Material.SEA_LANTERN);

            }

        }

        setGlassColorTask = Bukkit.getScheduler().runTaskTimer(plugin,
                () -> setGlassColors(newBlockMatrix1, startLocation1), 0, 20);


        Bukkit.getScheduler().scheduleSyncDelayedTask(
                plugin, () -> savedBlocks.restoreAll(blocksToChange), 320);

    }


    public void setGlassColors(ArrayList<ArrayList<Block>> blockMatrix, Location startLocation){

        double startX1 = startLocation.getX();
        double startZ1 = startLocation.getZ();

        Location cursorLocation = startLocation.clone();
        for (int z = 0; z <= 6; z++) {
            cursorLocation.setX(startZ1 + z);
            for (int x = 0; x <= 6; x++) {
                cursorLocation.setX(startX1 + x);
                Block block = blockMatrix.get(z).get(x);


                clearBlockInventory(block);

                final Material color = getRandomMaterial(coloredGlassBlocks);

                block.setType(color);


            }

        }

        if (getColorChangeCount() >= 15) {
            setGlassColorTask.cancel();
            resetColorChangeCount();

        }
        incColorChangeCount();

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
