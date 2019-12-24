package com.drazisil.luckyworld;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

public class BlockSave {

    private Block block;
    private Material type;
    private Location location;
    private BlockState state;

    public BlockSave(Block block, Material type, Location location, BlockState state) {
        this.block = block;
        this.type = type;
        this.location = cleanLocation(location);
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

    private Location cleanLocation(Location inLocation) {
        Location outLocation = inLocation.clone();
        outLocation.setX(Math.floor(inLocation.getX()));
        outLocation.setY(Math.floor(inLocation.getY()));
        outLocation.setZ(Math.floor(inLocation.getZ()));
        return outLocation;
    }
}
