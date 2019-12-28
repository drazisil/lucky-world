package com.drazisil.luckyworld.shared;

import org.bukkit.Location;
import org.bukkit.World;

public class RoundLocation extends Location {
    public RoundLocation(World world, double x, double y, double z) {
        super(world, x, y, z);
    }
}
