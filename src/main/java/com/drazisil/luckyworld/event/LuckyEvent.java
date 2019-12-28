package com.drazisil.luckyworld.event;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

public class LuckyEvent {

    private String name = null;

    public void doAction(BlockBreakEvent event, World world, Location location, Player player) {}

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
