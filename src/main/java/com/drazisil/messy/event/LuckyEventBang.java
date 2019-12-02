package com.drazisil.messy.event;

import com.drazisil.messy.event.EventLuckyHandler.LuckyEventRarity;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import static com.drazisil.messy.event.EventLuckyHandler.LuckyEventRarity.RARE;
import static org.bukkit.entity.EntityType.PRIMED_TNT;

public class LuckyEventBang implements LuckyEvent {

    public LuckyEventRarity rarity = RARE;


    public LuckyEventBang() {
        this.rarity = RARE;

    }

    public void doAction(Event event, World world, Location location, Player player) {
        EntityType entity = PRIMED_TNT;
        player.sendMessage("Hmm...");
        world.spawnEntity(location, entity);
    }
}
