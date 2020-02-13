package com.drazisil.luckyworld.event;

import com.drazisil.luckyworld.BlockSave;
import com.drazisil.luckyworld.BlockSaveRecord;
import com.drazisil.luckyworld.LuckyWorld;
import com.drazisil.luckyworld.shared.RoundLocation;
import com.drazisil.luckyworld.shared.VecOffset;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.Structure;
import org.bukkit.block.structure.UsageMode;
import org.bukkit.entity.*;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Objects;

import static com.drazisil.luckyworld.BlockSaveRecord.CenterType.CENTER;
import static com.drazisil.luckyworld.shared.LWUtilities.cleanLocation;
import static com.drazisil.luckyworld.shared.LWUtilities.randInt;
import static org.bukkit.GameRule.DO_MOB_SPAWNING;


public class LuckyEventOneSteve extends LuckyEvent {


    private final LuckyWorld plugin = LuckyWorld.getInstance();
    private boolean isActive = false;
    private BukkitTask fightTickTask;
    private Location priorLocation;
    private Location boxCenter;
    private World world;
    private boolean isDoorOpen = false;
    private final ArrayList<Entity> enemiesLeft = new ArrayList<>();

    @Override
    public void doAction(BlockBreakEvent event, World world, Location rawLocation, Player player) {

        if (isActive) {
            return;
        }

        this.priorLocation = rawLocation.clone();
        this.world = this.priorLocation.getWorld();
        this.isActive = true;

        RoundLocation location = cleanLocation(rawLocation.clone());

        this.boxCenter = location.clone();
        boxCenter.setY(150.0);

        int height = 50;
        int width = 50;
        int depth = 50;

        // Saved blocks
        BlockSaveRecord savedBlocks = new BlockSaveRecord();
        try {
            savedBlocks.generateBlockSaveCube(boxCenter.clone(),
                    height, width, depth, CENTER,  new VecOffset(0, 0, 0));
        } catch (Exception e) {
            e.printStackTrace();
        }


        // Blocks to change
        BlockSaveRecord blocksToChange
                = new BlockSaveRecord();
        try {
            blocksToChange.generateBlockSaveCube(boxCenter.clone(),
                    height, width, depth, CENTER, new VecOffset(0, 0, 0));
        } catch (Exception e) {
            e.printStackTrace();
        }


        // Fill with water
        for (BlockSave blockSave: blocksToChange.getBlocks()) {
            blockSave.getBlock().setType(Material.WATER);
            world.getBlockAt(blockSave.getLocation()).setBiome(Biome.WARM_OCEAN);
        }

        // Generate border
        for (BlockSave blockSave: blocksToChange.getBlocks()) {

            RoundLocation blockLocation = blockSave.getLocation();

            if (blocksToChange.isBorder(blockLocation)) {
                blockSave.getBlock().setType(Material.BARRIER);

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
                    && blockSave.getLocation().getY() < (blocksToChange.getBottomSideY() + 3)) {

                if (!blocksToChange.isBorder(blockSave.getLocation())) {
                    blockSave.getBlock().setType(Material.SAND);

                }
            }

        }

        // Place player
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, (20 * 15), 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, (6000), 1));
        player.teleport(boxCenter);
        player.sendMessage(player.getName() + " enters the arena! Can they kill the enemies and escape alive?");


        // Populate seafloor
        populateSeafloor(blocksToChange);

        Bukkit.getScheduler().scheduleSyncDelayedTask(
                plugin, () -> populateTank(blocksToChange), 10);

        fightTickTask = Bukkit.getScheduler().runTaskTimer(plugin,
                () -> fightTick(player, savedBlocks, blocksToChange), 20, 20);

    }

    private void fightTick(Entity player, BlockSaveRecord savedBlocks, BlockSaveRecord blocksToChange) {

        System.out.println("Fight!");

        if (!(this.isActive)) {
            fightTickTask.cancel();
        }

        enemiesLeft.removeIf(Entity::isDead);

        System.out.println("There are " + getRemainingEnemyCount() + " lights...er, not.");

        if (!isDoorOpen && getRemainingEnemyCount() == 0) {
            openDoor(blocksToChange);
            isDoorOpen = true;
        }

        World priorWorld = priorLocation.getWorld();


        if (!(blocksToChange.isLocationInsideArea(player.getLocation()))) {
            this.isActive = false;
            savedBlocks.restoreAll(blocksToChange);
            player.teleport(this.priorLocation);
            Objects.requireNonNull(priorWorld).setGameRule(DO_MOB_SPAWNING, true);
        }

        if (player.isDead()) {
            this.isActive = false;
            savedBlocks.restoreAll(blocksToChange);
            player.teleport(this.priorLocation);
            Objects.requireNonNull(priorWorld).setGameRule(DO_MOB_SPAWNING, true);
        }

    }

    private void openDoor(BlockSaveRecord blocksToChange) {
        System.out.println("Behold!");
        int doorX = ((int) blocksToChange.getLeftSideX());
        int doorY = ((int) (blocksToChange.getTopSideY() - ((blocksToChange.getTopSideY() - blocksToChange.getBottomSideY()) / 2)));
        int doorZ = ((int) (blocksToChange.getBackSideZ() - ((blocksToChange.getBackSideZ() - blocksToChange.getFrontSideZ()) / 2)));

        System.out.println(doorX + ", " + doorY + ", " + doorZ);

        BlockSaveRecord doorBlocks
                = new BlockSaveRecord();
        try {
            doorBlocks.generateBlockSaveCube(new Location(blocksToChange.getWorld(), doorX, doorY, doorZ),
                    10, 3, 10, CENTER,  new VecOffset(0,0,0));
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (BlockSave doorBlock: doorBlocks.getBlocks()) {
            doorBlock.getBlock().setType(Material.AIR);
        }


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
                            world.spawn(blockSave.getLocation(), TropicalFish.class);
                            break;

                        case 4:
                            world.spawn(blockSave.getLocation(), Turtle.class);
                            break;
                    }
                }

            }
        }
        addEnemies();
        World priorWorld = priorLocation.getWorld();

        Objects.requireNonNull(priorWorld).setGameRule(DO_MOB_SPAWNING, false);
    }

    private int getRemainingEnemyCount() {
        if (this.enemiesLeft.isEmpty()) {
            return 0;
        }
        return this.enemiesLeft.size();
    }

    private void addEnemyToList(Entity enemy) {
        this.enemiesLeft.add(enemy);
    }

    private void addEnemies() {
        for (int i = 1; i < 4; i++) {
            Zombie drowned = world.spawn(boxCenter, Drowned.class);

            ItemStack trident = new ItemStack(Material.TRIDENT);

            EntityEquipment drownedEquipment = drowned.getEquipment();


            Objects.requireNonNull(drownedEquipment).setItemInMainHand(trident);
            drownedEquipment.setItemInMainHandDropChance(1.0f);
            addEnemyToList(drowned);
            System.out.println("One drowned enters...");
        }

    }

    private void populateSeafloor(BlockSaveRecord blocksToChange) {
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
    }
}
