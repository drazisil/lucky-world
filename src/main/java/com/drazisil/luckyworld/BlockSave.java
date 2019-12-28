package com.drazisil.luckyworld;

import com.drazisil.luckyworld.shared.RoundLocation;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import static com.drazisil.luckyworld.shared.LWUtilities.cleanLocation;

public class BlockSave {

    private final Block block;
    private final Material type;
    private final RoundLocation location;
    private final BlockState state;

    BlockSave(Block block, Material type, Location location, BlockState state) {
        this.block = block;
        this.type = type;
        this.location = cleanLocation(location);
        this.state = state;
    }

    public Block getBlock() {
        return block;
    }

    public RoundLocation getLocation() {
        return location;
    }

    public BlockState getState() {
        return state;
    }

    public Material getType() {
        return type;
    }


}
