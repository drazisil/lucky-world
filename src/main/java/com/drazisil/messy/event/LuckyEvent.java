package com.drazisil.messy.event;

import com.drazisil.messy.event.EventLuckyHandler.LuckyEventRarity;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public interface LuckyEvent {

    // How rare is this event?
    LuckyEventRarity rarity = null;

    static void doAction(Event event, World world, Location location, Player player) {}


}
