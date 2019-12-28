package com.drazisil.luckyworld.shared;

import org.bukkit.Location;

import java.util.Random;

public class LWUtilities {

    public static int randInt(int max) {
        return new Random().nextInt(max);
    }

    public static RoundLocation cleanLocation(Location inLocation) {
        RoundLocation outLocation = new RoundLocation(inLocation.getWorld(),
                inLocation.getX(),
                inLocation.getBlockY(),
                inLocation.getZ());
        outLocation.setX(Math.floor(inLocation.getX()));
        outLocation.setY(Math.floor(inLocation.getY()));
        outLocation.setZ(Math.floor(inLocation.getZ()));
        return outLocation;
    }

    public static String locationToString(Location loc) {
        return Math.floor(loc.getX()) +
                " " + Math.floor(loc.getY()) +
                " " + Math.floor(loc.getZ());
    }

}
