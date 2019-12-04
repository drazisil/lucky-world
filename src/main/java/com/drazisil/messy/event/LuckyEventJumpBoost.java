package com.drazisil.messy.event;

import com.drazisil.messy.event.EventLuckyHandler.LuckyEventRarity;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static com.drazisil.messy.event.EventLuckyHandler.LuckyEventRarity.COMMON;
import static com.drazisil.messy.event.EventLuckyHandler.LuckyEventRarity.UNCOMMON;

public class LuckyEventJumpBoost implements LuckyEvent {

    public LuckyEventRarity rarity = UNCOMMON;

    public LuckyEventJumpBoost() {
        this.rarity = COMMON;

    }

    @Override
    public void doAction(BlockBreakEvent event, World world, Location location, Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 400, 1));

    }
}
