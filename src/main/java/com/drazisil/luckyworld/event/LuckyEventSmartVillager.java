package com.drazisil.luckyworld.event;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class LuckyEventSmartVillager extends LuckyEvent {

    @Override
    public void doAction(BlockBreakEvent event, World world, Location location, Player player) {
        int newY = world.getMaxHeight() - 10;
        location.setY(newY);

        player.teleport(location);
        Ghast ghast = world.spawn(location, Ghast.class);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 1200, 1));
        ghast.addPassenger(player);


    }
}
