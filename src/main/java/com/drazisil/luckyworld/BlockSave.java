package com.drazisil.luckyworld;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

public class BlockSave {

    private LuckyWorld plugin = LuckyWorld.getInstance();

    private Block block;
    private Material type;
    private Location location;
    private BlockState state;

    public BlockSave(Block block, Material type, Location location, BlockState state) {
        this.block = block;
        this.type = type;
        this.location = plugin.cleanLocation(location);
        this.state = state;
    }

    public Block getBlock() {
        return block;
    }

    public Location getLocation() {
        return location;
    }

    public BlockState getState() {
        return state;
    }

    public Material getType() {
        return type;
    }


}
