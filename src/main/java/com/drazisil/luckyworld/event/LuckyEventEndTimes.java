package com.drazisil.luckyworld.event;

import com.drazisil.luckyworld.LuckyWorld;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import static org.bukkit.Bukkit.dispatchCommand;
import static org.bukkit.Bukkit.getConsoleSender;

public class LuckyEventEndTimes implements LuckyEvent {

    private LuckyWorld plugin = LuckyWorld.getInstance();


    @Override
    public void doAction(BlockBreakEvent event, World world, Location location, Player player) {

        World oldWorld = world;

        ConsoleCommandSender consoleSender = getConsoleSender();

        String playerName = player.getDisplayName();

        dispatchCommand(consoleSender, "execute in the_end run tp " + playerName +  " 0 100 0");

        player.setInvulnerable(true);

        Bukkit.getScheduler().scheduleSyncDelayedTask(
                plugin, () -> {
                    dispatchCommand(consoleSender, "execute in overworld run tp " + playerName +  " " + location.getX() + " " + location.getY() + " " + location.getZ());
                    player.setInvulnerable(false);
                }, 300);

    }
}
