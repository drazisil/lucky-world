package com.drazisil.luckyworld.shared;

import com.sun.istack.internal.NotNull;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class RoundLocation extends Location {
    private double x;
    private double y;
    private double z;

    public RoundLocation(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    @Override
    @NotNull
    public RoundLocation clone() {
        return (RoundLocation) super.clone();
    }

    /**
     * Adds the location by a vector.
     *
     * @see Vector
     * @param vec Vector to use
     * @return the same location
     */
    @Override
    @NotNull
    public RoundLocation add(@NotNull Vector vec) {
        this.x += vec.getX();
        this.y += vec.getY();
        this.z += vec.getZ();
        return this;
    }
}
