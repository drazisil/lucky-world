package com.drazisil.messy.event;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class LuckyEventSealantern implements LuckyEvent {


    @Override
    public void doAction(BlockBreakEvent event, World world, Location location, Player player) {

        world.dropItem(location, new ItemStack(Material.SEA_LANTERN, Material.SEA_LANTERN.getMaxStackSize()));

    }
}
