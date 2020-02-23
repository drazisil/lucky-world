package com.drazisil.luckyworld;


import com.drazisil.luckyworld.event.EventClassroom;
import com.drazisil.luckyworld.event.LWEventHandler;
import com.drazisil.luckyworld.event.LuckyEventEntry;
import com.drazisil.luckyworld.event.LuckyEventWE;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.projectiles.ProjectileSource;

import java.util.Objects;

import static com.drazisil.luckyworld.LuckyWorld.*;
import static com.drazisil.luckyworld.event.LWEventHandler.handleLuckyEvent;
import static com.drazisil.luckyworld.event.LWEventHandler.shouldEvent;
import static com.drazisil.luckyworld.shared.LWUtilities.cleanLocation;


class LWListener implements Listener {

    /**
     * Fires when a player joins
     * @param event PlayerJoinEvent
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().sendMessage("Welcome, " + event.getPlayer().getName() + " to..." + LuckyWorld.name + "!");

    }

    /**
     * Watches for block break events in the overworld and the LuckyWorld world
     * @param event BlockBreakEvent
     */
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        Player player = event.getPlayer();
        World world = player.getWorld();
        String worldName = world.getName();

        // Fast fail if not a world we care about
        if (!worldName.equals("overworld") && !worldName.equals(LuckyWorld.worldName)) {
            return;
        }

        Location location = cleanLocation(event.getBlock().getLocation());

        // Handle LuckyWorld world events events
//        if (worldName.equals(LuckyWorld.worldName)) {
//            worldHandler.handleBlockBreakEvent(event, location, player);
//        }


        // Handle overworld events
        if (world.getName().equals("overworld")) {

            // Fast fail if not a lucky event
            if (!shouldEvent(getMaxNumber(), getMagicNumber())) return;

            handleLuckyEvent(event, world, location, player);
        }



    }

    /**
     * This event handler cancels spawns in the LuckyWorld world caused by lightning
     * @param spawnEvent CreatureSpawnEvent
     */
    @EventHandler
    public void onCreatureSpawnEvent(CreatureSpawnEvent spawnEvent) {
        World spawnWorld = spawnEvent.getLocation().getWorld();
        if (spawnWorld != null && spawnEvent.getSpawnReason() == CreatureSpawnEvent.SpawnReason.LIGHTNING
                && spawnWorld.getName().equalsIgnoreCase(LuckyWorld.worldName)) {
            spawnEvent.setCancelled(true);
        }
    }

    /**
     * Cancels fall damage to a player during the {@link LuckyEventWE} event
     * @param damageEvent EntityDamageEvent
     */
    @EventHandler
    public void onPlayerDamage(EntityDamageEvent damageEvent) {
        if (damageEvent.getCause() == EntityDamageEvent.DamageCause.FALL && damageEvent.getEntity() instanceof  Player) {
            LuckyEventEntry rawLuckyEvent = LWEventHandler.getEventByRarityAndName(LWEventHandler.LuckyEventRarity.RARE, "we");

            LuckyEventWE luckyEvent = (LuckyEventWE) Objects.requireNonNull(rawLuckyEvent).event;
            if (luckyEvent.needsCancel) {
                damageEvent.setCancelled(true);
                luckyEvent.needsCancel = false;
            }
        }
    }

    /**
     * Triggers the {@link EventClassroom} event when a player sleeps
     * @param event PlayerBedEnterEvent
     */
    @EventHandler
    public void onPlayerSleep(PlayerBedEnterEvent event) {
        LuckyEventEntry luckyEvent = LWEventHandler.getEventByRarityAndName(LWEventHandler.LuckyEventRarity.DREAM, "classroom");

        Objects.requireNonNull(luckyEvent).event.doAction(null, event.getPlayer().getWorld(), event.getPlayer().getLocation(), event.getPlayer());
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        LuckyEventEntry rawLuckyEvent;

        rawLuckyEvent = LWEventHandler.getEventByRarityAndName(LWEventHandler.LuckyEventRarity.DREAM, "classroom");

        EventClassroom luckyEvent = (EventClassroom) Objects.requireNonNull(rawLuckyEvent).event;
        if (luckyEvent.isRunning()) {

//            if (event.getMessage().equals("Mischief Managed")) {
//                event.getPlayer().sendMessage("Ok");
//                rawLuckyEvent = LWEventHandler.getEventByRarityAndName(LWEventHandler.LuckyEventRarity.DREAM, "classroom");
//
//                luckyEvent = (EventClassroom) Objects.requireNonNull(rawLuckyEvent).event;
//                luckyEvent.reset();
//            }

            if (event.getMessage().length() == 1) {

                if (event.getMessage().equalsIgnoreCase(luckyEvent.getCorrectAnswer())) {

                    Bukkit.getScheduler().scheduleSyncDelayedTask(
                            LuckyWorld.getInstance(), () -> {
                                luckyEvent.teacherSpeak(event.getPlayer(), "Correct!");
                                luckyEvent.reset();
                            }, 20);

                } else {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(
                            LuckyWorld.getInstance(), () -> luckyEvent.teacherSpeak(event.getPlayer(), "Looks you could really use that fresh air. Wrong, try again."), 20);

                }
            }

        }



    }

    @EventHandler
    public void onVehicleExit(VehicleExitEvent event) {
        if (event.getExited() instanceof Player) {
            Player player = (Player) event.getExited();
            Vehicle vehicle = event.getVehicle();
            if (vehicle.hasMetadata("classroom_name")) {
                if (vehicle.getMetadata("classroom_name").get(0).asString().equals("classroom_seat")) {
                    LuckyEventEntry rawLuckyEvent = LWEventHandler.getEventByRarityAndName(LWEventHandler.LuckyEventRarity.DREAM, "classroom");

                    EventClassroom luckyEvent = (EventClassroom) Objects.requireNonNull(rawLuckyEvent).event;
                    if (luckyEvent.isRunning() && !luckyEvent.needsCancel && !event.getVehicle().isDead()) {
                        player.sendMessage("Uh uh uh!");
                        event.setCancelled(true);
                    }

                }
            }
        }
    }

    @EventHandler
    public void onExplosionPrimeEvent(ExplosionPrimeEvent event) {
        Entity entity = event.getEntity();
        if (entity.hasMetadata("classroom_name")) {
            if (entity.getMetadata("classroom_name").get(0).asString().equals("classmate")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityTeleport(EntityTeleportEvent event) {
        Entity entity = event.getEntity();
        if (entity.hasMetadata("classroom_name")) {
            if (entity.getMetadata("classroom_name").get(0).asString().equals("teacher")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onLaunchProjectileEvent(ProjectileLaunchEvent event) {
        ProjectileSource projectileSource = event.getEntity().getShooter();

        if (projectileSource instanceof LivingEntity) {
            LivingEntity shooter = (LivingEntity) projectileSource;
            if (shooter.hasMetadata("classroom_name")) {
                if (shooter.getMetadata("classroom_name").get(0).asString().equals("classmate")) {
                    event.setCancelled(true);
                }
            }
        }

    }
}

