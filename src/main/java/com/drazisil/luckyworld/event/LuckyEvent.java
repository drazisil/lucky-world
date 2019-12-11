package com.drazisil.luckyworld.event;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

public interface LuckyEvent {

    void doAction(BlockBreakEvent event, World world, Location location, Player player);


}
