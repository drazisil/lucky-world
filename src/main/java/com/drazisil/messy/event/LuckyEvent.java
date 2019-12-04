package com.drazisil.messy.event;

import com.drazisil.messy.event.EventLuckyHandler.LuckyEventRarity;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

public interface LuckyEvent {

    // How rare is this event?
    LuckyEventRarity rarity = null;

    void doAction(BlockBreakEvent event, World world, Location location, Player player);


}
