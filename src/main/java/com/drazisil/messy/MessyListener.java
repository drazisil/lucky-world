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



        dropStacks(world, location, oldMat, newCount, stackSize);


        // Update the counter
        instance.setMultiBlockCount(instance.getMultiBlockCount() * 2);

    }

    /**
     * Drop a number of full ItemStacks, as well as any leftovers
     *
     * @param world The World in which to drop the stacks
     * @param location The Location in which to drop the ItemStacks
     * @param material The Material which the ItemStacks are
     * @param itemCount The number of Items to drop in total
     * @param maxStackSize The max stack size for the ItemStack
     */
    private void dropStacks(World world, Location location, Material material, int itemCount, int maxStackSize) {
        int stackCount = itemCount / maxStackSize;
        int itemsLeft = itemCount % maxStackSize;

        logger.info("total stack size: " + maxStackSize + ", stacks: " + stackCount + ", leftovers: " + itemsLeft);

        // Drop the full stacks
        if (stackCount > 0) {
            logger.info("dropping " + stackCount + " stacks...");
            for (int i = stackCount; i > 0; i--) {
                world.dropItem(location, new ItemStack(material, maxStackSize));
            }
        }

        // Drop the rest
        if (itemsLeft > 0) {
            logger.info("dropping rest: " + itemsLeft);
            world.dropItem(location, new ItemStack(material, itemsLeft));
        }

    }
}

