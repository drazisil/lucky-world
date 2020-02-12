package com.drazisil.luckyworld.event;

import com.drazisil.luckyworld.shared.LWUtilities;
import com.drazisil.luckyworld.shared.RoundLocation;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.type.TrapDoor;
import org.bukkit.entity.*;
import org.bukkit.entity.minecart.RideableMinecart;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static com.drazisil.luckyworld.shared.LWUtilities.cleanLocation;

public class EventClassroom extends LuckyEvent {

    private final int effectDuration = (20 * 60 * 60);
    public boolean needsCancel = false;
    private boolean isRunning = false;
    private Location priorLocation = null;

    @Override
    public void doAction(BlockBreakEvent event, World world, Location location, Player player) {

        this.isRunning = true;
        this.priorLocation = location.clone();

        RoundLocation rawLocation = cleanLocation(location);

        RoundLocation classroomSpawnLoc = rawLocation.clone();
        classroomSpawnLoc.setY(225);

        LWUtilities.loadAndPlaceSchematic(world, classroomSpawnLoc, "Classroom");

        RoundLocation deskSeatSpawnLoc = (RoundLocation) classroomSpawnLoc.clone().add(0, -1, 0);


        // Player Seat
        Pig playerSeat = makeDesk(world, deskSeatSpawnLoc);

        RoundLocation seatLocation = classroomSpawnLoc.clone();
        player.teleport(new Location(world, seatLocation.getX(), seatLocation.getY(), seatLocation.getZ(), 180.0f, 0.0f));
        playerSeat.addPassenger(player);


        // Seat 1
        RoundLocation seat1Location = (RoundLocation) deskSeatSpawnLoc.clone().add(-6, 0, -3);
        Pig seat1 = makeDesk(world, seat1Location);

        Husk classmate1 = world.spawn(seat1Location, Husk.class);
        classmate1.setCustomName("classmate");
        classmate1.setCustomNameVisible(false);
        classmate1.setBaby(true);
        classmate1.setInvulnerable(true);

        seat1.addPassenger(classmate1);

        // Seat 2
        RoundLocation seat2Location = (RoundLocation) deskSeatSpawnLoc.clone().add(-3, 0, -3);
        Pig seat2 = makeDesk(world, seat2Location);

        Zombie classmate2 = world.spawn(seat2Location, Zombie.class);
        classmate2.setCustomName("classmate");
        classmate2.setCustomNameVisible(false);
        classmate2.setBaby(true);
        classmate2.setInvulnerable(true);

        seat2.addPassenger(classmate2);

        // Seat 3
        RoundLocation seat3Location = (RoundLocation) deskSeatSpawnLoc.clone().add(0, 0, -3);
        Pig seat3 = makeDesk(world, seat3Location);

        Drowned classmate3 = world.spawn(seat3Location, Drowned.class);
        classmate3.setCustomName("classmate");
        classmate3.setCustomNameVisible(false);
        classmate3.setBaby(true);
        classmate3.setInvulnerable(true);

        seat3.addPassenger(classmate3);

        // Seat 4
        RoundLocation seat4Location = (RoundLocation) deskSeatSpawnLoc.clone().add(+3, 0, -3);
        Pig seat4 = makeDesk(world, seat4Location);

        Creeper classmate4 = world.spawn(seat4Location, Creeper.class);
        classmate4.setCustomName("classmate");
        classmate4.setCustomNameVisible(false);
        classmate4.setInvulnerable(true);

        seat4.addPassenger(classmate4);


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


    }

    private Pig makeDesk(World world, RoundLocation location) {
        Pig playerSeat = makeDeskChair(world, location);

        RoundLocation deskSpawnLoc = (RoundLocation) cleanLocation(location.clone()).add(0, 1, -1);
        makeDeskDesk(world, deskSpawnLoc);
        return  playerSeat;
    }

    private void placeSign(RoundLocation location, Material material, BlockFace face) {
        RoundLocation rightDeskSeatLoc = location;
        Block trapdoorRightSeatBlock = location.getWorld().getBlockAt(rightDeskSeatLoc);
        trapdoorRightSeatBlock.setType(material);
        org.bukkit.block.data.type.WallSign trapdoorRightSeatData = ((org.bukkit.block.data.type.WallSign) trapdoorRightSeatBlock.getBlockData());
        trapdoorRightSeatData.setFacing(face);
        trapdoorRightSeatBlock.setBlockData(trapdoorRightSeatData);
    }

    private void makeDeskDesk(World world, RoundLocation location) {
        Block trapdoorDeskBlock = world.getBlockAt(location.clone());
        trapdoorDeskBlock.setType(Material.BIRCH_TRAPDOOR);
        TrapDoor trapdoorDeskData = (TrapDoor) trapdoorDeskBlock.getBlockData();
        trapdoorDeskData.setFacing(BlockFace.SOUTH);
        trapdoorDeskData.setHalf(Bisected.Half.TOP);
        trapdoorDeskBlock.setBlockData(trapdoorDeskData);

        Block trapdoorDeskLeft = world.getBlockAt(location.clone().add(-1.0, 0.0, 0.0));
        trapdoorDeskLeft.setType(Material.ACACIA_TRAPDOOR);
        TrapDoor trapdoorDeskLeftData = (TrapDoor) trapdoorDeskLeft.getBlockData();
        trapdoorDeskLeftData.setFacing(BlockFace.WEST);
        trapdoorDeskLeftData.setOpen(true);
        trapdoorDeskLeft.setBlockData(trapdoorDeskLeftData);

        Block trapdoorDeskRight = world.getBlockAt(location.clone().add(1.0, 0.0, 0.0));
        trapdoorDeskRight.setType(Material.ACACIA_TRAPDOOR);
        TrapDoor trapdoorDeskRightData = (TrapDoor) trapdoorDeskRight.getBlockData();
        trapdoorDeskRightData.setFacing(BlockFace.EAST);
        trapdoorDeskRightData.setOpen(true);
        trapdoorDeskRight.setBlockData(trapdoorDeskRightData);

    }

    private Pig makeDeskChair(World world, RoundLocation location) {
        Location deskSeatSpawnLoc = location;

        world.getBlockAt(deskSeatSpawnLoc).setType(Material.ACACIA_SLAB);
        RideableMinecart rideableMinecart = world.spawn(deskSeatSpawnLoc.clone().add(0.5, 0.5, 0.5), RideableMinecart.class);
        rideableMinecart.setRotation(90, 0);
        rideableMinecart.setDisplayBlock(null);

        Pig pigSeat = world.spawn(deskSeatSpawnLoc, Pig.class);
        pigSeat.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, effectDuration, 1));
        pigSeat.setSilent(true);
        pigSeat.setAI(false);
        pigSeat.setSaddle(true);
        pigSeat.setInvulnerable(true);
        pigSeat.setRotation(180, 0);
        pigSeat.setCustomName("classroom_seat");
        pigSeat.setCustomNameVisible(false);

        rideableMinecart.addPassenger(pigSeat);

        Block slabSeatBotom = world.getBlockAt(deskSeatSpawnLoc.clone().add(0.0, 1.0, 0.0));
        slabSeatBotom.setType(Material.DARK_OAK_SLAB);

        Block trapdoorBackSeatBlock = world.getBlockAt(deskSeatSpawnLoc.clone().add(0.0, 1.0, 1.0));
        trapdoorBackSeatBlock.setType(Material.DARK_OAK_TRAPDOOR);
        TrapDoor trapdoorBackSeatData = (TrapDoor) trapdoorBackSeatBlock.getBlockData();
        trapdoorBackSeatData.setFacing(BlockFace.SOUTH);
        trapdoorBackSeatData.setOpen(true);
        trapdoorBackSeatBlock.setBlockData(trapdoorBackSeatData);

        return pigSeat;
    }


    public void reset() {
        this.needsCancel = false;
        setRunning(false);
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }
}
