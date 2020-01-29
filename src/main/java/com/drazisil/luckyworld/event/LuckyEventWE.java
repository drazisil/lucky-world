package com.drazisil.luckyworld.event;

import com.drazisil.luckyworld.BlockSave;
import com.drazisil.luckyworld.BlockSaveRecord;
import com.drazisil.luckyworld.LuckyWorld;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import static com.drazisil.luckyworld.BlockSaveRecord.CenterType.CENTER;
import static com.drazisil.luckyworld.event.LWEventHandler.LuckyEventRarity.PARTS;

@SuppressWarnings("unused")
public class LuckyEventWE extends LuckyEvent {

    private BukkitTask gateTriggerTickTask;
    private BukkitTask gateTickTask;

    private final LuckyWorld plugin = LuckyWorld.getInstance();
    private double currentGateY;
    private boolean isRunning = false;

    @Override
    public void doAction(BlockBreakEvent event, World world, Location location, Player player) {
        this.isRunning = true;
        Clipboard clipboard = null;

        File file = new File( LuckyWorld.getInstance().getDataFolder()+ "/../WorldEdit/schematics/" + "FloatingCastle.schem");

        ClipboardFormat format = ClipboardFormats.findByFile(file);
        assert format != null;
        try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
            clipboard = reader.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /* use the clipboard here */
        Location newLocation = player.getLocation().clone();
        newLocation.setY(225);

        Location signLocation = newLocation.clone();
        signLocation.add(2.0d, -1.0d, 5.0d);

        newLocation.setYaw(0.0f);
        player.teleport(newLocation);
        newLocation.setY(newLocation.getY()-1);
        try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(BukkitAdapter.adapt(world), -1)) {
            assert clipboard != null;
            Operation operation = new ClipboardHolder(clipboard)
                    .createPaste(editSession)
                    .to(BlockVector3.at(newLocation.getX(), newLocation.getY(), newLocation.getZ()))
                    // configure here
                    .build();
            Operations.complete(operation);
        } catch (WorldEditException e) {
            e.printStackTrace();
        }
        LuckyEventEntry signEvent = LWEventHandler.getEventByRarityAndName(PARTS, "sign");
        assert signEvent != null;
        signEvent.event.doAction(null, world, signLocation, player);



        // Blocks to change
        BlockSaveRecord gateTriggerBox
                = new BlockSaveRecord();
        gateTriggerBox.generateBlockSaveCube(newLocation.clone().add(0.0, 0.0, -13),
                3, 11, 9, CENTER,  0);

        // Blocks to change
        BlockSaveRecord gateBox
                = new BlockSaveRecord();
        gateBox.generateBlockSaveCube(newLocation.clone().add(0.0, 7.0, -19),
                15, 7, 3, CENTER,  0);

        System.out.println(gateBox.getTopSideY() + ", " + gateBox.getBottomSideY());

        this.currentGateY = gateBox.getTopSideY();

        gateTriggerTickTask = Bukkit.getScheduler().runTaskTimer(plugin,
                () -> gateTriggerTick(player, gateTriggerBox, gateBox), 20, 20);

    }

    private void gateTriggerTick(Player player, BlockSaveRecord blockSaveRecord, BlockSaveRecord gateBox) {
        if (blockSaveRecord.isLocationInsideArea(player.getLocation())) {
            player.sendMessage("Uh Oh...");

            player.getWorld().playSound(blockSaveRecord.getStartLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0f, 1.0f);


            gateTickTask = Bukkit.getScheduler().runTaskTimer(plugin,
                    () -> gateTick(gateBox), 20, 20);

            gateTriggerTickTask.cancel();
        }
    }

    private void gateTick(BlockSaveRecord blockSaveRecord) {

        ArrayList<BlockSave> gateBlocks = blockSaveRecord.getBlocksByY(this.currentGateY);
        for (BlockSave blockSave: gateBlocks) {
            if (blockSave.getType() == Material.NETHER_BRICK_FENCE || blockSave.getType() == Material.NETHER_BRICK_WALL) {
                blockSave.getBlock().setType(Material.AIR);
            }


        }
        this.currentGateY  = this.currentGateY - 1.0d;
        World world = blockSaveRecord.getWorld();
        world.playSound(blockSaveRecord.getStartLocation(), Sound.BLOCK_GRINDSTONE_USE, 1.0f, 1.0f);

        if (this.currentGateY < blockSaveRecord.getBottomSideY()) {

            spawnZombiePod(world, blockSaveRecord.getStartLocation().clone().add(-16, 0.0, -5.0));

            spawnZombiePod(world, blockSaveRecord.getStartLocation().clone().add(16, 0.0, -5.0));

            gateTickTask.cancel();
            this.isRunning = false;
        }

    }

    private void spawnZombiePod(World world, Location location) {
        for (int i = 1; i < 30; i++) {
            Zombie zombie = world.spawn(location, Zombie.class);
            zombie.setBaby(new Random().nextBoolean());
            EntityEquipment zombieEquipment = zombie.getEquipment();
            assert zombieEquipment != null;

            zombieEquipment.setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
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

    @SuppressWarnings("unused")
    public boolean isRunning() {
        return isRunning;
    }
}
