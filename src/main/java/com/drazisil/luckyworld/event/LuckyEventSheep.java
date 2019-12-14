package com.drazisil.luckyworld.event;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.block.BlockBreakEvent;

public class LuckyEventSheep extends LuckyEvent {


    @Override
    public void doAction(BlockBreakEvent event, World world, Location location, Player player) {

        for (int i = 1; i <= 5; i++) {
            Sheep sheep = world.spawn(location, Sheep.class);
            sheep.setColor(DyeColor.PINK);
        }

    }
}
