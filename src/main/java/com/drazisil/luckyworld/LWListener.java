package com.drazisil.luckyworld;


import com.drazisil.luckyworld.event.EventClassroom;
import com.drazisil.luckyworld.event.LWEventHandler;
import com.drazisil.luckyworld.event.LuckyEventEntry;
import com.drazisil.luckyworld.event.LuckyEventWE;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;

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
        System.out.println(damageEvent.getCause() + ", " + damageEvent.getEntity());
        if (damageEvent.getCause() == EntityDamageEvent.DamageCause.FALL && damageEvent.getEntity() instanceof  Player) {
            LuckyEventEntry rawLuckyEvent = LWEventHandler.getEventByRarityAndName(LWEventHandler.LuckyEventRarity.RARE, "we");
            assert rawLuckyEvent != null;
            LuckyEventWE luckyEvent = (LuckyEventWE) rawLuckyEvent.event;
            if (luckyEvent.needsCancel) {
                damageEvent.setCancelled(true);
                luckyEvent.needsCancel = false;
            }
        }
    }

    @EventHandler
    public void onPlayerSleep(PlayerBedEnterEvent event) {
        LuckyEventEntry luckyEvent = LWEventHandler.getEventByRarityAndName(LWEventHandler.LuckyEventRarity.RARE, "we");
        assert luckyEvent != null;
        luckyEvent.event.doAction(null, event.getPlayer().getWorld(), event.getPlayer().getLocation(), event.getPlayer());
        event.setCancelled(true);
    }

    @EventHandler
    public void onVehicleExit(VehicleExitEvent event) {
        if (event.getExited() instanceof Player) {
            Player player = (Player) event.getExited();
            String vehicleName = event.getVehicle().getCustomName();
            assert vehicleName != null;
            if (vehicleName.equals("classroom_seat")) {
                LuckyEventEntry rawLuckyEvent = LWEventHandler.getEventByRarityAndName(LWEventHandler.LuckyEventRarity.DREAM, "classroom");
                assert rawLuckyEvent != null;
                EventClassroom luckyEvent = (EventClassroom) rawLuckyEvent.event;
                if (luckyEvent.needsCancel) {
                    luckyEvent.needsCancel = false;
                    luckyEvent.reset();
                } else
                    player.sendMessage("Uh uh uh!");
                event.setCancelled(true);
            }
        }
    }


//    @EventHandler
//    public void onPlayerInteractEvent(PlayerInteractEvent event) {
//        Player player = event.getPlayer();
//        Block clickedBlock = event.getClickedBlock();
//        ItemStack itemClickedWith = event.getItem();
//
//        if (clickedBlock.getType() == Material.EMERALD_BLOCK
//                && itemClickedWith.getType() == Material.STICK) {
//            System.out.println("It was so");
//        }
//    }

//    @EventHandler
//    public void onEntityEvent(EntityEvent event) {
//
//        if (event.getEntityType() == EntityType.GIANT) {
//            System.out.println(event.getEntity());
//        }
//
//    }

}

