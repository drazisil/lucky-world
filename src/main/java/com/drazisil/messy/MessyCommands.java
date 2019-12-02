package com.drazisil.messy;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static com.drazisil.messy.Messy.instance;
import static com.drazisil.messy.Messy.logger;
import static com.drazisil.messy.util.Utilities.*;
import static org.bukkit.entity.EntityType.GHAST;
import static org.bukkit.entity.EntityType.PRIMED_TNT;

public class MessyCommands implements CommandExecutor {


    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        FileConfiguration config = instance.config;

        logger.info("Detected messy command sent with the following args: " + String.join(" ", args));

        if (args.length == 0) {
            instance.sendMessyMessage(sender, "That's...not quite correct");
            return false;
        }

        String commandName = args[0];

        switch (commandName) {
            case "getMultiBlockCount":
                instance.sendMessyMessage(sender, "The current multiBlockCount is: " + getMultiBlockCount(config));
                break;
            case "setMultiBlockCount":
                if (!(args.length == 2) || (Integer.parseInt(args[1]) < 0)) {
                    instance.sendMessyMessage(sender, "Please give me a number to set it to.");
                    return true;
                }
                setMultiBlockCount(config, Integer.parseInt(args[1]));
                instance.sendMessyMessage(sender, "The current multiBlockCount is: " + getMultiBlockCount(config));
                break;
            case "getBangMax":
                instance.sendMessyMessage(sender, "The current bangMax is: " + getBangMax(config));
                break;
            case "setBangMax":
                if (!(args.length == 2) || (Integer.parseInt(args[1]) < 0)) {
                    instance.sendMessyMessage(sender, "Please give me a number to set it to.");
                    return true;
                }
                setBangMax(config, Integer.parseInt(args[1]));
                instance.sendMessyMessage(sender, "The current bangMax is: " + getBangMax(config));
                break;
            case "bang":
                logger.info("Should bang: " + shouldBang(config));
                return true;
            case "bangForce":
                if (sender instanceof Player) {
                    Location location = ((Player) sender).getLocation();
                    World world = ((Player) sender).getWorld();
                    bang(world, (Player) sender, location, PRIMED_TNT);
                }
                return true;
            case "fortuneForce":
                if (sender instanceof Player) {
                    Player player = ((Player) sender).getPlayer();
                    World world = player.getWorld();
                    Location location = player.getLocation();


                    location.setY(world.getHighestBlockYAt(location) + 100);

                    Entity spawnedEntity = fortune(world, (Player) sender, location, GHAST);
                    spawnedEntity.addPassenger(player);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 600, 1));

                }
                return true;
            default:
                return false;
        }
        return true;

    }




}