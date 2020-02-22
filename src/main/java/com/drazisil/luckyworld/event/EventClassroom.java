package com.drazisil.luckyworld.event;

import com.drazisil.luckyworld.BlockSaveRecord;
import com.drazisil.luckyworld.LuckyWorld;
import com.drazisil.luckyworld.shared.LWUtilities;
import com.drazisil.luckyworld.shared.RoundLocation;
import com.drazisil.luckyworld.shared.VecOffset;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.type.TrapDoor;
import org.bukkit.entity.*;
import org.bukkit.entity.minecart.RideableMinecart;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static com.drazisil.luckyworld.BlockSaveRecord.CenterType.CENTER_OFFSET;
import static com.drazisil.luckyworld.shared.LWUtilities.cleanLocation;

public class EventClassroom extends LuckyEvent {

    public boolean needsCancel = false;
    private boolean isRunning = false;
    private Location priorLocation = null;
    private Player player;
    private String techerName = "Ms. Ender";
    private final LuckyWorld plugin = LuckyWorld.getInstance();
    private World priorWorld;
    private World classroomWorld;
    private BlockSaveRecord classroomBox;
    private BlockSaveRecord classroomRestoreBox;

    @Override
    public void doAction(BlockBreakEvent event, World world, Location location, Player player) {

        this.isRunning = true;
        this.priorLocation = location.clone();
        this.priorWorld = world;
        this.player = player;
        this.classroomWorld = this.player.getServer().getWorld("new_world");

            if (this.classroomWorld != null) {
                this.classroomWorld.setGameRule(GameRule.DO_MOB_SPAWNING, false);
            }

        this.player.setPlayerTime(1200, false);
        this.player.setPlayerWeather(WeatherType.CLEAR);

        RoundLocation rawLocation = cleanLocation(new Location(this.classroomWorld, 300, 225, 0));


//        RoundLocation rawLocation = cleanLocation(location);

        RoundLocation classroomSpawnLoc = rawLocation.clone();

        classroomSpawnLoc.setY(200);


        this.classroomRestoreBox = new BlockSaveRecord();
        try {
            classroomRestoreBox.generateBlockSaveCube(classroomSpawnLoc.clone().add(-30, 17, -4),
                    60, 75, 70, CENTER_OFFSET,  new VecOffset(0,0,0));

        } catch (Exception e) {
            e.printStackTrace();
        }

        LWUtilities.loadAndPlaceSchematic(classroomWorld, classroomSpawnLoc.clone(), "Classroom");

        RoundLocation deskSeatSpawnLoc = (RoundLocation) classroomSpawnLoc.clone().add(0, -1, 0);

        // Player Seat
        Pig playerSeat = makeDesk(classroomWorld, deskSeatSpawnLoc);

        RoundLocation seatLocation = classroomSpawnLoc.clone();
        player.teleport(new Location(classroomWorld, seatLocation.getX(), seatLocation.getY(), seatLocation.getZ(), 180.0f, 0.0f));
        playerSeat.addPassenger(player);

        populateClassmates(classroomWorld, deskSeatSpawnLoc);

        Enderman teacher = createTeacher(classroomWorld, (RoundLocation) classroomSpawnLoc.clone().add(-1, 0, -8));

        this.classroomBox = new BlockSaveRecord();
        try {
            System.out.println("====================================\n" +classroomSpawnLoc.toString());
            classroomBox.generateBlockSaveCube(classroomSpawnLoc.clone().add(-30, 17, -4),
                    60, 75, 70, CENTER_OFFSET,  new VecOffset(0,0,0));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(
                plugin, () -> runTeacher(player), (20 * 5));

    }

    private void runTeacher(Player player) {
        String firstMessage = "Well, class. It looks like " + player.getDisplayName() + " hasn't finished their test yet. Nobody gets to go outside until they do!";

        teacherSpeak(player, firstMessage);

        Bukkit.getScheduler().scheduleSyncDelayedTask(
                plugin, () -> askQuestion(player), (20 * 3));
    }

    public String getCorrectAnswer() {
        return "c";
    }

    private void askQuestion(Player player) {
        String question = "Which material can't be used to make a hoe?\n" +
                "\n" +
                "Is it:\n" +
                "A: Iron\n" +
                "B: Diamond\n" +
                "C: Emerald\n" +
                "\n" +
                "Please type the correct letter in chat.";

        teacherSpeak(player, question);
    }

    public void teacherSpeak(Player player, String message) {
        player.sendMessage("<" + getTeacherName() + "> " + message);
    }

    private Pig makeDesk(World world, RoundLocation location) {
        Pig playerSeat = makeDeskChair(world, location);

        RoundLocation deskSpawnLoc = (RoundLocation) cleanLocation(location.clone()).add(0, 1, -1);
        makeStudentDesk(world, deskSpawnLoc);
        return  playerSeat;
    }

    private void makeStudentDesk(World world, RoundLocation location) {
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




    private Pig makeDeskChair(World world, RoundLocation deskSeatSpawnLoc) {

        world.getBlockAt(deskSeatSpawnLoc).setType(Material.ACACIA_SLAB);
        RideableMinecart rideableMinecart = world.spawn(((Location) deskSeatSpawnLoc).clone().add(0.5, 0.5, 0.5), RideableMinecart.class);
        rideableMinecart.setRotation(90, 0);
        rideableMinecart.setDisplayBlock(null);

        Pig pigSeat = world.spawn(deskSeatSpawnLoc, Pig.class);
        int effectDuration = (20 * 60 * 60);
        pigSeat.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, effectDuration, 1));
        pigSeat.setSilent(true);
        pigSeat.setAI(false);
        pigSeat.setSaddle(true);
        pigSeat.setInvulnerable(true);
        pigSeat.setRotation(180, 0);
        pigSeat.setMetadata("classroom_name", new FixedMetadataValue(LuckyWorld.getInstance(), "classroom_seat"));

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

    private <T extends LivingEntity> LivingEntity createClassmate(World world, RoundLocation location, Class<T> clazz, String name) {
        LivingEntity classmate = world.spawn(location, clazz);
        classmate.setMetadata("classroom_name", new FixedMetadataValue(LuckyWorld.getInstance(), "classmate"));
        classmate.setCustomName(name);
        if (classmate instanceof Zombie) {
            ((Zombie) classmate).setBaby(true);
        }

        if (classmate instanceof Ageable) {
            ((Ageable) classmate).setBaby();
        }

        classmate.setInvulnerable(true);
        return classmate;
    }

    private Enderman createTeacher(World world, RoundLocation location) {
        Enderman teacher = world.spawn(location, Enderman.class);
        teacher.setMetadata("classroom_name", new FixedMetadataValue(LuckyWorld.getInstance(), "teacher"));
        teacher.setCustomName(getTeacherName());

        teacher.setInvulnerable(true);
        return teacher;
    }

    private String getTeacherName() {
        return this.techerName;
    }

    private void populateClassmates(World world, RoundLocation startingLocation) {

        RoundLocation deskSeatSpawnLoc = startingLocation.clone();

        // Seat 1
        RoundLocation seat1Location = (RoundLocation) deskSeatSpawnLoc.clone().add(-6, 0, -3);
        Pig seat1 = makeDesk(world, seat1Location);

        LivingEntity classmate1 = createClassmate(world, seat1Location, Husk.class, "Rusty");

        seat1.addPassenger(classmate1);

        // Seat 2
        RoundLocation seat2Location = (RoundLocation) deskSeatSpawnLoc.clone().add(-3, 0, -3);
        Pig seat2 = makeDesk(world, seat2Location);

        LivingEntity classmate2 = createClassmate(world, seat2Location, Zombie.class, "Harold");

        seat2.addPassenger(classmate2);

        // Seat 3
        RoundLocation seat3Location = (RoundLocation) deskSeatSpawnLoc.clone().add(0, 0, -3);
        Pig seat3 = makeDesk(world, seat3Location);

        LivingEntity classmate3 = createClassmate(world, seat3Location, Drowned.class, "Triton");

        seat3.addPassenger(classmate3);

        // Seat 4
        RoundLocation seat4Location = (RoundLocation) deskSeatSpawnLoc.clone().add(+3, 0, -3);
        Pig seat4 = makeDesk(world, seat4Location);

        LivingEntity classmate4 = createClassmate(world, seat4Location, Sheep.class, "Dinner");

        seat4.addPassenger(classmate4);

        // Seat 5
        RoundLocation seat5Location = (RoundLocation) deskSeatSpawnLoc.clone().add(-6, 0, 0);
        Pig seat5 = makeDesk(world, seat5Location);

        LivingEntity classmate5 = createClassmate(world, seat5Location, ZombieVillager.class, "Jhonny");

        seat5.addPassenger(classmate5);

        // Seat 6
        RoundLocation seat6Location = (RoundLocation) deskSeatSpawnLoc.clone().add(-3, 0, 0);
        Pig seat6 = makeDesk(world, seat6Location);

        LivingEntity classmate6 = createClassmate(world, seat6Location, Panda.class, "Munchy");

        seat6.addPassenger(classmate6);

        // Seat 7 is the player

        // Seat 8
        RoundLocation seat8Location = (RoundLocation) deskSeatSpawnLoc.clone().add(3, 0, 0);
        Pig seat8 = makeDesk(world, seat8Location);

        LivingEntity classmate8 = createClassmate(world, seat8Location, Fox.class, "Fire");

        seat8.addPassenger(classmate8);

        // Seat 9
        RoundLocation seat9Location = (RoundLocation) deskSeatSpawnLoc.clone().add(-6, 0, 3);
        Pig seat9 = makeDesk(world, seat9Location);

        LivingEntity classmate9 = createClassmate(world, seat9Location, Villager.class, "Gerry");

        seat9.addPassenger(classmate9);

        // Seat 10
        RoundLocation seat10Location = (RoundLocation) deskSeatSpawnLoc.clone().add(-3, 0, 3);
        Pig seat10 = makeDesk(world, seat10Location);

        LivingEntity classmate10 = createClassmate(world, seat10Location, PigZombie.class, "Hammy");

        seat10.addPassenger(classmate10);

        // Seat 11
        RoundLocation seat11Location = (RoundLocation) deskSeatSpawnLoc.clone().add(0, 0, 3);
        Pig seat11 = makeDesk(world, seat11Location);

        LivingEntity classmate11 = createClassmate(world, seat11Location, Creeper.class, "Boomie");

        seat11.addPassenger(classmate11);

        // Seat 12
        RoundLocation seat12Location = (RoundLocation) deskSeatSpawnLoc.clone().add(3, 0, 3);
        Pig seat12 = makeDesk(world, seat12Location);

        LivingEntity classmate12 = createClassmate(world, seat12Location, Pig.class, "Oggie");

        seat12.addPassenger(classmate12);

    }


    public void reset() {
        this.needsCancel = false;
        setRunning(false);
        this.player.resetPlayerTime();
        this.player.resetPlayerWeather();
        this.player.teleport(this.priorLocation);
        this.classroomRestoreBox.restoreAll(this.classroomBox);
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }
}
