package com.drazisil.luckyworld.event;

import com.drazisil.luckyworld.LuckyWorld;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.potion.PotionEffectType;

import static com.drazisil.luckyworld.LuckyWorld.worldHandler;
import static com.drazisil.luckyworld.shared.LWUtilities.cleanLocation;
import static com.drazisil.luckyworld.shared.LWUtilities.locationToString;
import static org.bukkit.Bukkit.*;

public class LuckyEventNewWorld extends LuckyEvent {

    private final LuckyWorld plugin = LuckyWorld.getInstance();

    @Override
    public void doAction(BlockBreakEvent event, World world, Location location, Player player) {

        ConsoleCommandSender consoleSender = getConsoleSender();

        String playerName = player.getDisplayName();
        GameMode oldGameMode = player.getGameMode();

        int numSeconds = 60 * 5;

        // Get new world spawn
        World newWorld = getWorld(LuckyWorld.worldName);

        Location newSpawn;
        newSpawn = cleanLocation(new Location(newWorld, 300, 225, 0));

        Location playerSafeSpawn = newSpawn.clone();
        playerSafeSpawn.setY(playerSafeSpawn.getY() + 1);

        dispatchCommand(consoleSender, "execute in " + LuckyWorld.worldName + " run tp " + playerName +  " " + locationToString(playerSafeSpawn));

        if (worldHandler.getSpawnLocation() == null) {
            worldHandler.setNewSpawnLocation(playerSafeSpawn);

            try {
                worldHandler.generateSpawnPlatform();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Set and move player to spawn
        player.setGameMode(GameMode.SURVIVAL);
        player.setInvulnerable(true);

        player.teleport(playerSafeSpawn);


        // Revert
        Bukkit.getScheduler().scheduleSyncDelayedTask(
                plugin, () -> {
                    System.out.println("execute in overworld run tp " + playerName +  " " + locationToString(location));
                    dispatchCommand(player, "execute in overworld run tp " + locationToString(location));
                    player.setGameMode(oldGameMode);
                    player.removePotionEffect(PotionEffectType.CONDUIT_POWER);
                    player.removePotionEffect(PotionEffectType.DOLPHINS_GRACE);
                    player.setInvulnerable(false);
                }, getDurationBySeconds(numSeconds));

    }

    private int getDurationBySeconds(int seconds) {
        return 20 * seconds;
    }

}
