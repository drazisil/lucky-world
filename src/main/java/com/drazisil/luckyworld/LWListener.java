package com.drazisil.luckyworld;


import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import static com.drazisil.luckyworld.LuckyWorld.*;
import static com.drazisil.luckyworld.event.LWEventHandler.handleLuckyEvent;
import static com.drazisil.luckyworld.event.LWEventHandler.shouldEvent;
import static com.drazisil.luckyworld.shared.LWUtilities.cleanLocation;


class LWListener implements Listener {

    private final LuckyWorld plugin = LuckyWorld.getInstance();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().sendMessage("Welcome, " + event.getPlayer().getName() + " to..." + LuckyWorld.name + "!");

    }


    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        Player player = event.getPlayer();
        World world = player.getWorld();
        String worldName = world.getName();

        // Fast fail if not a world we care about
        if (!worldName.equals("overworld") && !worldName.equals("new_world")) {
            return;
        }

        Location location = cleanLocation(event.getBlock().getLocation());

        // Handle new_world events
        if (worldName.equals("new_world")) {
            worldHandler.handleBlockBreakEvent(event, world, location, player);
        }


        // Handle overworld events
        if (world.getName().equals("overworld")) {

            // Fast fail if not a lucky event
            if (!shouldEvent(getMaxNumber(), getMagicNumber())) return;

            handleLuckyEvent(event, world, location, player);
        }



    }

//    @EventHandler
//    public void onPlayerInteractEvent(PlayerInteractEvent event) {
//        Player player = event.getPlayer();
//        Block clickedBlock = event.getClickedBlock();
//        ItemStack itemClickedWith = event.getItem();
//
//        if (clickedBlock.getType() == Material.EMERALD_BLOCK
//                && itemClickedWith.getType() == Material.STICK) {
//            System.out.println("It was so");
//        }
//    }

//    @EventHandler
//    public void onEntityEvent(EntityEvent event) {
//
//        if (event.getEntityType() == EntityType.GIANT) {
//            System.out.println(event.getEntity());
//        }
//
//    }

}

