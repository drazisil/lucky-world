package com.drazisil.messy.event;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class LuckyEventJumpBoost implements LuckyEvent {

    @Override
    public void doAction(BlockBreakEvent event, World world, Location location, Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 400, 31));

    }
}
