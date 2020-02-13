package com.drazisil.luckyworld.shared;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;

public class RoundLocation extends Location {

    public RoundLocation(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    @Override
    @Nonnull
    public RoundLocation clone() {
        return (RoundLocation) super.clone();
    }

    @SuppressWarnings("NullableProblems")
    @Override
    @Nonnull
    public RoundLocation add(Vector vec) {
        return (RoundLocation) super.add(vec);
    }

    @SuppressWarnings("UnusedReturnValue")
    public RoundLocation add(VecOffset vec) {
        setX(getBlockX() + vec.getX());
        setY(getBlockY() + vec.getY());
        setZ(getBlockZ() + vec.getZ());
        return this;
    }


}
