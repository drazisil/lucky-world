package com.drazisil.messy;


import com.google.common.collect.Iterables;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;


import static com.drazisil.messy.Messy.instance;
import static com.drazisil.messy.Messy.logger;



public class MessyListener implements Listener


{
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage("Welcome, " + event.getPlayer().getName() + " to...messy!");


    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();
        Location location = player.getLocation();
        World world = player.getWorld();
        Collection<ItemStack> oldDrops = block.getDrops();

        // Exit if empty
        if (oldDrops.isEmpty()) return;


        ItemStack oldItem = Iterables.get(oldDrops, 0);
        Material oldMat = oldItem.getType();
        int stackSize = oldMat.getMaxStackSize();
        int newCount = instance.getMultiBlockCount();
        int stackCount = newCount / stackSize;
        int itemsLeft = newCount % stackSize;

        logger.info("total stack size: " + stackSize + ", stacks: " + stackCount + ", leftovers: " + itemsLeft);

        // Drop the full stacks
        if (stackCount > 0) {
            logger.info("dropping " + stackCount + " stacks...");
            for (int i = stackCount; i > 0; i--) {
                ItemStack newStack = new ItemStack(oldMat, stackSize);
                world.dropItem(location, newStack);
            }
        }

        // Drop the rest
        if (itemsLeft > 0) {
            logger.info("dropping rest: " + itemsLeft);
            ItemStack newStack = new ItemStack(oldMat, itemsLeft);
            world.dropItem(location, newStack);
        }

        // Update the counter
        instance.updateTimes(instance.getMultiBlockCount() * 2);

    }
}

