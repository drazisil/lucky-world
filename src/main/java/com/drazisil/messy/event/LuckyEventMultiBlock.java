package com.drazisil.messy.event;

import com.drazisil.messy.event.EventLuckyHandler.LuckyEventRarity;
import com.google.common.collect.Iterables;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

import static com.drazisil.messy.event.EventLuckyHandler.LuckyEventRarity.COMMON;

public class LuckyEventMultiBlock implements LuckyEvent {

    public LuckyEventRarity rarity = COMMON;


    public LuckyEventMultiBlock() {
        this.rarity = COMMON;

    }

    public static void doAction(BlockBreakEvent event, World world, Location location, Player player) {

        boolean isSilk = false;
        int multiCountFactor = 5;


        player.sendMessage("Hi");


        Block block = event.getBlock();
        Collection<ItemStack> oldDrops = block.getDrops();

        if (player.getEquipment().getItemInMainHand().containsEnchantment(Enchantment.SILK_TOUCH)) isSilk = true;


        // Exit if empty
        if (oldDrops.isEmpty() && !isSilk) return;

        if (isSilk) {
            // Was this Silk Touch?
            int stackSize = block.getType().getMaxStackSize();
            dropStacks(world, location, block.getType(), multiCountFactor, stackSize);
        } else {
            // Drop normally
            ItemStack oldItem = Iterables.get(oldDrops, 0);
            Material oldMat = oldItem.getType();
            int stackSize = block.getType().getMaxStackSize();
            dropStacks(world, location, oldMat, oldItem.getAmount() * multiCountFactor, stackSize);
        }

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
    private static void dropStacks(World world, Location location, Material material, int itemCount, int maxStackSize) {
        int stackCount = itemCount / maxStackSize;
        int itemsLeft = itemCount % maxStackSize;


        // Drop the full stacks
        if (stackCount > 0) {
            for (int i = stackCount; i > 0; i--) {
                world.dropItem(location, new ItemStack(material, maxStackSize));
            }
        }

        // Drop the rest
        if (itemsLeft > 0) {
            world.dropItem(location, new ItemStack(material, itemsLeft));
        }

    }
}
