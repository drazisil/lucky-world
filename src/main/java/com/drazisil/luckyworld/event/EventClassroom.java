package com.drazisil.luckyworld.event;

import com.drazisil.luckyworld.shared.LWUtilities;
import com.drazisil.luckyworld.shared.RoundLocation;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.TrapDoor;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.RideableMinecart;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EventClassroom extends LuckyEvent {

    private final int effectDuration = (20 * 60 * 60);

    @Override
    public void doAction(BlockBreakEvent event, World world, Location location, Player player) {

        RoundLocation deskSeatSpawnLoc = (RoundLocation) LWUtilities.cleanLocation(location.clone()).add(-4, -1, 0);

        world.getBlockAt(deskSeatSpawnLoc).setType(Material.ACACIA_SLAB);
        RideableMinecart rideableMinecart = world.spawn(deskSeatSpawnLoc.clone().add(0.5, 0.5, 0.5), RideableMinecart.class);
        rideableMinecart.setRotation(90, 0);

        Pig pigSeat = world.spawn(deskSeatSpawnLoc, Pig.class);
        pigSeat.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, effectDuration, 1));
        pigSeat.setSilent(true);
        pigSeat.setSaddle(true);
        pigSeat.setCustomName("classroom_seat");
        pigSeat.setCustomNameVisible(false);

        rideableMinecart.addPassenger(pigSeat);

        RoundLocation leftDeskSeatLoc = (RoundLocation) deskSeatSpawnLoc.clone().add(-1.0, 1.0, 0.0);
        placeSign(leftDeskSeatLoc, Material.DARK_OAK_WALL_SIGN, BlockFace.WEST);

        RoundLocation rightDeskSeatLoc = (RoundLocation) deskSeatSpawnLoc.clone().add(1.0, 1.0, 0.0);
        placeSign(rightDeskSeatLoc, Material.DARK_OAK_WALL_SIGN, BlockFace.EAST);


        Block trapdoorSeatBlock = world.getBlockAt(deskSeatSpawnLoc.clone().add(0.0, 1.0, 0.0));
        trapdoorSeatBlock.setType(Material.DARK_OAK_TRAPDOOR);

        Block trapdoorBackSeatBlock = world.getBlockAt(deskSeatSpawnLoc.clone().add(0.0, 1.0, 1.0));
        trapdoorBackSeatBlock.setType(Material.DARK_OAK_TRAPDOOR);
        TrapDoor trapdoorBackSeatData = (TrapDoor) trapdoorBackSeatBlock.getBlockData();
        trapdoorBackSeatData.setFacing(BlockFace.SOUTH);
        trapdoorBackSeatData.setOpen(true);
        trapdoorBackSeatBlock.setBlockData(trapdoorBackSeatData);

//        RoundLocation leftDeskSeatLoc = (RoundLocation) deskSeatSpawnLoc.clone().add(-1.0, 1.0, -0.0);
//        Block trapdoorLeftSeatBlock = world.getBlockAt(leftDeskSeatLoc);
//        trapdoorLeftSeatBlock.setType(Material.ACACIA_TRAPDOOR);
//        TrapDoor trapdoorLeftSeatData = (TrapDoor) trapdoorLeftSeatBlock.getBlockData();
//        trapdoorLeftSeatData.setFacing(BlockFace.EAST);
//        trapdoorLeftSeatData.setOpen(true);
//        trapdoorLeftSeatBlock.setBlockData(trapdoorLeftSeatData);
//
//        RoundLocation rightDeskSeatLoc = (RoundLocation) deskSeatSpawnLoc.clone().add(1.0, 1.0, -0.0);
//        Block trapdoorRightSeatBlock = world.getBlockAt(rightDeskSeatLoc);
//        trapdoorLeftSeatBlock.setType(Material.ACACIA_TRAPDOOR);
//        TrapDoor trapdoorRightSeatData = (TrapDoor) trapdoorLeftSeatBlock.getBlockData();
//        trapdoorRightSeatData.setFacing(BlockFace.EAST);
//        trapdoorRightSeatData.setOpen(true);
//        trapdoorRightSeatBlock.setBlockData(trapdoorRightSeatData);

        pigSeat.addPassenger(player);
    }

    private void placeSign(RoundLocation location, Material material, BlockFace face) {
        RoundLocation rightDeskSeatLoc = location;
        Block trapdoorRightSeatBlock = location.getWorld().getBlockAt(rightDeskSeatLoc);
        trapdoorRightSeatBlock.setType(material);
        org.bukkit.block.data.type.WallSign trapdoorRightSeatData = ((org.bukkit.block.data.type.WallSign) trapdoorRightSeatBlock.getBlockData());
        trapdoorRightSeatData.setFacing(face);
        trapdoorRightSeatBlock.setBlockData(trapdoorRightSeatData);
    }


}
