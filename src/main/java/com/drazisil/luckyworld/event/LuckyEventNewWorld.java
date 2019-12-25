package com.drazisil.luckyworld.event;

import com.drazisil.luckyworld.LuckyWorld;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static com.drazisil.luckyworld.LuckyWorld.worldHandler;
import static org.bukkit.Bukkit.*;

public class LuckyEventNewWorld extends LuckyEvent {

    private LuckyWorld plugin = LuckyWorld.getInstance();

    int numSeconds = 60 * 3;

    @Override
    public void doAction(BlockBreakEvent event, World world, Location location, Player player) {

        ConsoleCommandSender consoleSender = getConsoleSender();

        String playerName = player.getDisplayName();
        GameMode oldGameMode = player.getGameMode();

        // Get new world spawn
        World newWorld = getWorld("new_world");
        Location newSpawn;
        newSpawn = plugin.cleanLocation(newWorld.getSpawnLocation());

        Location playerSafeSpawn = newSpawn.clone();
        playerSafeSpawn.setY(playerSafeSpawn.getY() + 1);

        dispatchCommand(consoleSender, "execute in new_world run tp " + playerName +  " " + plugin.locationToString(playerSafeSpawn));

        if (worldHandler.getSpawnLocation() == null) {
            worldHandler.setNewSpawnLocation(playerSafeSpawn);

            worldHandler.generateSpawnPlatform();
        }

        // Set and move player to spawn
        player.setGameMode(GameMode.SURVIVAL);
        PotionEffect superJump = new PotionEffect(PotionEffectType.CONDUIT_POWER, getDurationBySeconds(numSeconds), 80);
        PotionEffect superWater = new PotionEffect(PotionEffectType.DOLPHINS_GRACE, getDurationBySeconds(numSeconds), 80);
        player.setInvulnerable(true);
        player.addPotionEffect(superJump);

        player.teleport(playerSafeSpawn);


        // Revert
        Bukkit.getScheduler().scheduleSyncDelayedTask(
                plugin, () -> {
                    System.out.println("execute in overworld run tp " + playerName +  " " + plugin.locationToString(location));
                    dispatchCommand(player, "execute in overworld run tp " + plugin.locationToString(location));
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
