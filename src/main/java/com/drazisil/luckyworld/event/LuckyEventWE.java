package com.drazisil.luckyworld.event;

import com.drazisil.luckyworld.BlockSave;
import com.drazisil.luckyworld.BlockSaveRecord;
import com.drazisil.luckyworld.LuckyWorld;
import com.drazisil.luckyworld.shared.VecOffset;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import static com.drazisil.luckyworld.BlockSaveRecord.CenterType.CENTER;
import static com.drazisil.luckyworld.BlockSaveRecord.CenterType.CENTER_OFFSET;
import static com.drazisil.luckyworld.event.LWEventHandler.LuckyEventRarity.PARTS;
import static com.drazisil.luckyworld.shared.LWUtilities.loadAndPlaceSchematic;

@SuppressWarnings("unused")
public class LuckyEventWE extends LuckyEvent {

    private BukkitTask gateTriggerTickTask;
    private BukkitTask gateTickTask;

    private final LuckyWorld plugin = LuckyWorld.getInstance();
    private double currentGateY;
    private boolean isRunning;
    private BukkitTask playerTrackTickTask;
    private Location priorLocation;
    private boolean hasSpawned = false;
    public boolean needsCancel = false;

    @Override
    public void doAction(BlockBreakEvent event, World world, Location location, Player player) {
        this.isRunning = true;
        this.priorLocation = location.clone();

        Location newLocation = player.getLocation().clone();
        newLocation.setY(225);

        Location signLocation = newLocation.clone();
        signLocation.add(2.0d, -1.0d, 5.0d);

        newLocation.setY(newLocation.getY() -1);

        player.teleport(newLocation);



        loadAndPlaceSchematic(world, newLocation, "FloatingCastle");


        LuckyEventEntry signEvent = LWEventHandler.getEventByRarityAndName(PARTS, "sign");

        Objects.requireNonNull(signEvent).event.doAction(null, world, signLocation, player);

        // Blocks to change
        BlockSaveRecord gateTriggerBox
                = new BlockSaveRecord();
        try {
            gateTriggerBox.generateBlockSaveCube(newLocation.clone().add(0.0, 0.0, -13),
                    3, 11, 9, CENTER,  new VecOffset(0,0,0));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Blocks to track
        BlockSaveRecord castleBoundsBox
                = new BlockSaveRecord();
        try {
            castleBoundsBox.generateBlockSaveCube(newLocation.clone().add(0.0, 0.0, -19),
                    27, 54, 54, CENTER_OFFSET,  new VecOffset(0.0, 11, 0.0));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Blocks to change
        BlockSaveRecord gateBox
                = new BlockSaveRecord();
        try {
            gateBox.generateBlockSaveCube(newLocation.clone().add(0.0, 7.0, -19),
                    15, 7, 3, CENTER,  new VecOffset(0,0,0));
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.currentGateY = gateBox.getTopSideY();

        gateTriggerTickTask = Bukkit.getScheduler().runTaskTimer(plugin,
                () -> gateTriggerTick(player, gateTriggerBox, gateBox), 20, 20);

        playerTrackTickTask = Bukkit.getScheduler().runTaskTimer(plugin,
                () -> playerTrackTick(castleBoundsBox, player), 40, 20);

    }

    private void playerTrackTick(BlockSaveRecord castleBoundsBox, Player player) {
        if (!castleBoundsBox.isLocationInsideArea(player.getLocation()) || player.isDead()) {

            player.teleport(priorLocation.subtract(0, 1, 0), PlayerTeleportEvent.TeleportCause.PLUGIN);
            castleBoundsBox.clearEntities();
            for (BlockSave blockSave: castleBoundsBox.getBlocks()) {
                blockSave.getBlock().setType(Material.AIR);
            }
            this.hasSpawned = false;
            gateTriggerTickTask.cancel();
            playerTrackTickTask.cancel();
            this.isRunning = false;
            this.needsCancel = true;
        }
    }

    private void gateTriggerTick(Player player, BlockSaveRecord blockSaveRecord, BlockSaveRecord gateBox) {
        if (blockSaveRecord.isLocationInsideArea(player.getLocation())) {
            player.sendMessage("Uh Oh...");

            player.getWorld().playSound(blockSaveRecord.getStartLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0f, 1.0f);


            gateTickTask = Bukkit.getScheduler().runTaskTimer(plugin,
                    () -> gateTick(gateBox, player), 20, 20);

            gateTriggerTickTask.cancel();
        }
    }

    private void gateTick(BlockSaveRecord blockSaveRecord, Player player) {

        ArrayList<BlockSave> gateBlocks = blockSaveRecord.getBlocksByY(this.currentGateY);
        for (BlockSave blockSave: gateBlocks) {
            if (blockSave.getType() == Material.NETHER_BRICK_FENCE || blockSave.getType() == Material.RED_NETHER_BRICK_WALL) {
                blockSave.getBlock().setType(Material.AIR);
            }


        }
        this.currentGateY  = this.currentGateY - 1.0d;
        World world = blockSaveRecord.getWorld();
        world.playSound(blockSaveRecord.getStartLocation(), Sound.BLOCK_GRINDSTONE_USE, 1.0f, 1.0f);

        if (this.currentGateY < blockSaveRecord.getBottomSideY() && !hasSpawned) {

            LuckyEventEntry hugeEvent = LWEventHandler.getEventByRarityAndName(PARTS, "huge");

            double playerY = player.getLocation().getY();
            Location gateLocation = blockSaveRecord.getStartLocation().clone();
//            gateLocation.setY(playerY);
            Objects.requireNonNull(hugeEvent).event.doAction(null, world, gateLocation.clone().add(-0.0, 0.0, -16.0), player);

            spawnZombiePod(world, gateLocation.clone().add(-16, 0.0, -5.0));

            spawnZombiePod(world, gateLocation.clone().add(16, 0.0, -5.0));

            this.hasSpawned = true;

            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 120, 1));

            gateTickTask.cancel();

        }

    }

    private void spawnZombiePod(World world, Location location) {
        for (int i = 1; i < 30; i++) {
            Zombie zombie = world.spawn(location, Zombie.class);
            zombie.setBaby(new Random().nextBoolean());
            EntityEquipment zombieEquipment = zombie.getEquipment();


            Objects.requireNonNull(zombieEquipment).setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
            zombieEquipment.setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
            zombieEquipment.setHelmet(new ItemStack(Material.DIAMOND_HELMET));

            ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
//            sword.addEnchantment(Enchantment.DAMAGE_ALL, 5);
//            sword.addEnchantment(Enchantment.DURABILITY, 3);
//            sword.addEnchantment(Enchantment.LOOT_BONUS_MOBS, 3);
//            sword.addEnchantment(Enchantment.MENDING, 1);

            zombieEquipment.setItemInMainHand(sword);
        }

    }

    public boolean isRunning() {
        return this.isRunning;
    }

    public Location getPriorLocation() {
        return this.priorLocation;
    }
}
