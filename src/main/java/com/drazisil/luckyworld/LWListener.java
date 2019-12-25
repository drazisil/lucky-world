package com.drazisil.luckyworld;


import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import static com.drazisil.luckyworld.event.LWEventHandler.handleLuckyEvent;
import static com.drazisil.luckyworld.event.LWEventHandler.shouldEvent;


public class LWListener implements Listener {

    private LuckyWorld plugin = LuckyWorld.getInstance();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().sendMessage("Welcome, " + event.getPlayer().getName() + " to..." + LuckyWorld.name + "!");

    }

    //TODO: Watch for spawn block

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {



        // Fast fail if not a lucky event
        if (!shouldEvent(plugin.getMaxNumber(), plugin.getMagicNumber())) return;


        Player player = event.getPlayer();
        World world = player.getWorld();
        Location location = player.getLocation();

        handleLuckyEvent(event, world, location, player);
    }


}

