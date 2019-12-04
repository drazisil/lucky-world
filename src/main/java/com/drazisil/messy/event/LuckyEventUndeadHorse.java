package com.drazisil.messy.event;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.SkeletonHorse;
import org.bukkit.event.block.BlockBreakEvent;

public class LuckyEventUndeadHorse implements LuckyEvent {


    @Override
    public void doAction(BlockBreakEvent event, World world, Location location, Player player) {

        for (int i = 1; i <= 3; i++) {
            world.spawn(location, SkeletonHorse.class);
        }

    }
}
