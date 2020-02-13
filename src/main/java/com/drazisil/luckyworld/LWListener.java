package com.drazisil.luckyworld;


import com.drazisil.luckyworld.event.EventClassroom;
import com.drazisil.luckyworld.event.LWEventHandler;
import com.drazisil.luckyworld.event.LuckyEventEntry;
import com.drazisil.luckyworld.event.LuckyEventWE;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.projectiles.ProjectileSource;

import java.util.Objects;

import static com.drazisil.luckyworld.LuckyWorld.*;
import static com.drazisil.luckyworld.event.LWEventHandler.handleLuckyEvent;
import static com.drazisil.luckyworld.event.LWEventHandler.shouldEvent;
import static com.drazisil.luckyworld.shared.LWUtilities.cleanLocation;


class LWListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().sendMessage("Welcome, " + event.getPlayer().getName() + " to..." + LuckyWorld.name + "!");

    }


    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        Player player = event.getPlayer();
        World world = player.getWorld();
        String worldName = world.getName();

        // Fast fail if not a world we care about
        if (!worldName.equals("overworld") && !worldName.equals("new_world")) {
            return;
        }

        Location location = cleanLocation(event.getBlock().getLocation());

        // Handle new_world events
        if (worldName.equals("new_world")) {
            worldHandler.handleBlockBreakEvent(event, location, player);
        }


        // Handle overworld events
        if (world.getName().equals("overworld")) {

            // Fast fail if not a lucky event
            if (!shouldEvent(getMaxNumber(), getMagicNumber())) return;

            handleLuckyEvent(event, world, location, player);
        }



    }

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

    @EventHandler
    public void onPlayerSleep(PlayerBedEnterEvent event) {
        LuckyEventEntry luckyEvent = LWEventHandler.getEventByRarityAndName(LWEventHandler.LuckyEventRarity.RARE, "we");

        Objects.requireNonNull(luckyEvent).event.doAction(null, event.getPlayer().getWorld(), event.getPlayer().getLocation(), event.getPlayer());
        event.setCancelled(true);
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onPlayerSay(PlayerChatEvent event) {
        if (event.getMessage().equals("Mischief Managed")) {
            event.getPlayer().sendMessage("Ok");
            LuckyEventEntry rawLuckyEvent = LWEventHandler.getEventByRarityAndName(LWEventHandler.LuckyEventRarity.DREAM, "classroom");

            EventClassroom luckyEvent = (EventClassroom) Objects.requireNonNull(rawLuckyEvent).event;
            luckyEvent.reset();
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
                    if (!luckyEvent.isRunning() || luckyEvent.needsCancel || event.getVehicle().isDead()) {
                        luckyEvent.reset();
                    } else {
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

