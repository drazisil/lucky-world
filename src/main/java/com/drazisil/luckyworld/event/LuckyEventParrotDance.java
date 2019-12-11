package com.drazisil.luckyworld.event;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Jukebox;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;


public class LuckyEventParrotDance implements LuckyEvent {


    @Override
    public void doAction(BlockBreakEvent event, World world, Location location, Player player) {

        Block block = location.getBlock();

        block.setType(Material.JUKEBOX);

        Jukebox jukeBlock = ((Jukebox) block.getState());

        for (int i = 1; i <= 30; i++) {
            world.spawn(location, Parrot.class);
        }

        jukeBlock.setRecord(new ItemStack(Material.MUSIC_DISC_13));

        jukeBlock.setPlaying(Material.MUSIC_DISC_13);

        jukeBlock.update();

    }
}
