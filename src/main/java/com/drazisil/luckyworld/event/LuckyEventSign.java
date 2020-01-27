package com.drazisil.luckyworld.event;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

public class LuckyEventSign extends  LuckyEvent {

    @Override
    public void doAction(BlockBreakEvent event, World world, Location location, Player player) {
        Location signLocation = location.clone();

        Block block = world.getBlockAt(signLocation);
        block.setType(Material.ACACIA_SIGN);
        org.bukkit.block.data.type.Sign data = ((org.bukkit.block.data.type.Sign) block.getBlockData());
        data.setRotation(BlockFace.NORTH_NORTH_WEST);
        block.setBlockData(data);
        Sign state = ((Sign) world.getBlockAt(signLocation).getState());
        state.setLine(1, "They all jump");
        state.setLine(3, "eventually...");
        state.update();
    }
}
