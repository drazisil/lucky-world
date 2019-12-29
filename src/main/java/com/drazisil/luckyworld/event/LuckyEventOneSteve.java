package com.drazisil.luckyworld.event;

import com.drazisil.luckyworld.BlockSave;
import com.drazisil.luckyworld.BlockSaveRecord;
import com.drazisil.luckyworld.LuckyWorld;
import com.drazisil.luckyworld.shared.RoundLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.Structure;
import org.bukkit.block.structure.UsageMode;
import org.bukkit.entity.Dolphin;
import org.bukkit.entity.Player;
import org.bukkit.entity.Turtle;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static com.drazisil.luckyworld.BlockSaveRecord.CenterType.CENTER;
import static com.drazisil.luckyworld.shared.LWUtilities.cleanLocation;
import static com.drazisil.luckyworld.shared.LWUtilities.randInt;


public class LuckyEventOneSteve extends LuckyEvent {


    private final LuckyWorld plugin = LuckyWorld.getInstance();

    @Override
    public void doAction(BlockBreakEvent event, World world, Location rawLocation, Player player) {

        RoundLocation location = cleanLocation(rawLocation.clone());

        Location boxCenter = location.clone();
        boxCenter.setY(150.0);

        int height = 50;
        int width = 50;
        int depth = 50;

        // Saved blocks
        BlockSaveRecord savedBlocks = new BlockSaveRecord();
        savedBlocks.generateBlockSaveCube(boxCenter.clone(),
                height, width, depth, CENTER,  0);


        // Blocks to change
        BlockSaveRecord blocksToChange
                = new BlockSaveRecord();
        blocksToChange.generateBlockSaveCube(boxCenter.clone(),
                height, width, depth, CENTER,  0);


        // Fill with water
        for (BlockSave blockSave: blocksToChange.getBlocks()) {
            blockSave.getBlock().setType(Material.WATER);
            world.getBlockAt(blockSave.getLocation()).setBiome(Biome.WARM_OCEAN);
        }

        // Generate border
        for (BlockSave blockSave: blocksToChange.getBlocks()) {

            RoundLocation blockLocation = blockSave.getLocation();

            if (blocksToChange.isBorder(blockLocation)) {
                blockSave.getBlock().setType(Material.GLASS);

            }

        }

        Location rightCenterFloorLoc = boxCenter.clone();

        rightCenterFloorLoc.setZ(blocksToChange.getFrontSideZ() + 10);
        rightCenterFloorLoc.setY(blocksToChange.getBottomSideY() + 2);

        Block centerBlock = world.getBlockAt(rightCenterFloorLoc);
        centerBlock.setType(Material.STRUCTURE_BLOCK);
        Structure structureBlock
                = (Structure) world.getBlockAt(rightCenterFloorLoc).getState();

        // Load the structure
        structureBlock.setUsageMode(UsageMode.LOAD);
        structureBlock.setStructureName("shipwreck/rightsideup_full_degraded");
        structureBlock.update();

        // Power the structure block
        Location powerBlockLoc = rightCenterFloorLoc.clone();
        powerBlockLoc.setX(powerBlockLoc.getX() - 1);
        Block powerBlock = world.getBlockAt(powerBlockLoc);
        powerBlock.setType(Material.REDSTONE_BLOCK);

        // Clear structure block
        world.getBlockAt(structureBlock.getLocation()).setType(Material.WATER);

        // Add sea lantern
        world.getBlockAt(boxCenter).setType(Material.SEA_LANTERN);

        // Clear power block
        world.getBlockAt(powerBlock.getLocation()).setType(Material.WATER);

        // Add bottom
        for (BlockSave blockSave: blocksToChange.getBlocks()) {
            if (blockSave.getLocation().getY() > blocksToChange.getBottomSideY()
                    && blockSave.getLocation().getY() < (blocksToChange.getBottomSideY() +3)) {
                if (!blocksToChange.isBorder(blockSave.getLocation())) {
                    blockSave.getBlock().setType(Material.SAND);

                }
            }

        }

        // Populate seafloor
        for (BlockSave blockSave: blocksToChange.getBlocks()) {
            if (blockSave.getLocation().getY() > (blocksToChange.getBottomSideY() + 2)
                    && blockSave.getLocation().getY() < (blocksToChange.getBottomSideY() + 4)) {
                if (!blocksToChange.isBorder(blockSave.getLocation())) {

                int spawnNum = randInt(60);
                switch (spawnNum) {
                    case 7:
                        blockSave.getBlock().setType(Material.SOUL_SAND);
                        break;

//                    case 6:
//                        blockSave.getBlock().setType(Material.MAGMA_BLOCK);
//                        break;

                    case 5:
                    case 15:
                    case 25:
                    case 35:
                        blockSave.getBlock().setType(Material.GRAVEL);
                        break;
                    case 4:
                    case 14:
                    case 24:
                    case 34:
                        blockSave.getBlock().setType(Material.TALL_SEAGRASS);
                        break;
                    case 3:
                    case 13:
                    case 23:
                    case 33:
                        blockSave.getBlock().setType(Material.KELP);
                        break;
                    case 2:
                    case 12:
                    case 22:
                    case 32:
                        blockSave.getBlock().setType(Material.FIRE_CORAL);
                        break;
                    case 1:
                    case 11:
                    case 21:
                    case 31:
                        blockSave.getBlock().setType(Material.TUBE_CORAL);
                        break;
                    case 0:
                    case 10:
                    case 20:
                    case 30:
                        blockSave.getBlock().setType(Material.SAND);
                        break;

                }
                }

            }
        }

        // Place player
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, (20 * 15), 1));
        player.teleport(boxCenter);

        Bukkit.getScheduler().scheduleSyncDelayedTask(
                plugin, () -> populateTank(blocksToChange), 10);

        Bukkit.getScheduler().scheduleSyncDelayedTask(
                plugin, () -> savedBlocks.restoreAll(blocksToChange), 40);

    }

    private void populateTank(BlockSaveRecord blocksToChange) {

        // Populate water
        for (BlockSave blockSave: blocksToChange.getBlocks()) {
            if (blockSave.getLocation().getY() > (blocksToChange.getBottomSideY() + 2)
                    && blockSave.getLocation().getY() < (blocksToChange.getBottomSideY() + 4)) {

                if (!blocksToChange.isBorder(blockSave.getLocation()) && blockSave.getBlock().getType() == Material.WATER) {

                    int spawnNum = randInt(60);
                    World world = blockSave.getBlock().getWorld();
                    switch (spawnNum) {
                        case 5:
                            world.spawn(blockSave.getLocation(), Dolphin.class);
                            break;

                        case 4:
                            world.spawn(blockSave.getLocation(), Turtle.class);
                            break;
                    }
                }

            }
        }
    }
}
