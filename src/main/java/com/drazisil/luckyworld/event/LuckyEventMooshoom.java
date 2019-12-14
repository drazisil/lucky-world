package com.drazisil.luckyworld.event;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

public class LuckyEventMooshoom extends LuckyEvent {


    @Override
    public void doAction(BlockBreakEvent event, World world, Location location, Player player) {

        for (int i = 1; i <= 10; i++) {
            world.spawn(location, MushroomCow.class);
        }

    }
}
