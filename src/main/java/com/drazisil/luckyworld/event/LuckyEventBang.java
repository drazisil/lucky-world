package com.drazisil.luckyworld.event;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import static org.bukkit.entity.EntityType.PRIMED_TNT;

public class LuckyEventBang implements LuckyEvent {

    @Override
    public void doAction(BlockBreakEvent event, World world, Location location, Player player) {
        EntityType entity = PRIMED_TNT;
        player.sendMessage("Hmm...");
        world.spawnEntity(location, entity);
    }
}
