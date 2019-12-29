package com.drazisil.luckyworld;


import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

import static com.drazisil.luckyworld.LuckyWorld.*;
import static com.drazisil.luckyworld.event.LWEventHandler.handleLuckyEvent;
import static com.drazisil.luckyworld.event.LWEventHandler.shouldEvent;
import static com.drazisil.luckyworld.shared.LWUtilities.cleanLocation;
import static org.bukkit.entity.EntityType.PRIMED_TNT;


class LWListener implements Listener {

    private final LuckyWorld plugin = LuckyWorld.getInstance();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().sendMessage("Welcome, " + event.getPlayer().getName() + " to..." + LuckyWorld.name + "!");

    }

    @EventHandler
    public void onEntityExplodePrime(ExplosionPrimeEvent event) {
        System.out.println(event.getEntityType() + " = " + event.getRadius());
        if (event.getEntityType().equals(PRIMED_TNT)) {
            event.setRadius(event.getRadius() * 20);
        }

    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        System.out.println(event.getEntityType());
        if (event.getEntityType().equals(PRIMED_TNT)) {
            List<Block> blocks = event.blockList();
            for (Block block: blocks
                    ) {
                block.getWorld().getBlockAt(block.getLocation()).setType(Material.GLOWSTONE);

            }
            event.setCancelled(true);
        }


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


}

