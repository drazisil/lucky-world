package com.drazisil.messy.event;

import com.drazisil.messy.event.EventLuckyHandler.LuckyEventRarity;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static com.drazisil.messy.event.EventLuckyHandler.LuckyEventRarity.UNCOMMON;

public class LuckyEventSlowFall implements LuckyEvent {

    public LuckyEventRarity rarity = UNCOMMON;

    public LuckyEventSlowFall() {
        this.rarity = UNCOMMON;

    }

    public static void doAction(Event event, World world, Location location, Player player) {
        location.setY(world.getMaxHeight());

        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 1200, 1));
        player.teleport(location);

    }
}
