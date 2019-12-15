package com.drazisil.luckyworld.event;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

public class LuckyEventOuch extends LuckyEvent {


    @Override
    public void doAction(BlockBreakEvent event, World world, Location location, Player player) {

        player.sendMessage("Incoming...");


        Location newLocation = player.getEyeLocation();

        double startY = newLocation.getY();

        for (int y = 1; y <= 6; y++) {
            newLocation.setY(startY + y);
            if (y == 6) {
                world.getBlockAt(newLocation).setType(Material.ANVIL);
            } else {
                world.getBlockAt(newLocation).setType(Material.AIR);
            }
        }

    }
}