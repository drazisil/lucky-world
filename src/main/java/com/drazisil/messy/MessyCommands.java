package com.drazisil.messy;

import com.drazisil.messy.event.LuckyEventBang;
import com.drazisil.messy.event.LuckyEventSlowFall;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import static com.drazisil.messy.Messy.instance;
import static com.drazisil.messy.Messy.logger;

public class MessyCommands implements CommandExecutor {


    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        FileConfiguration config = instance.config;

        logger.info("Detected messy command sent with the following args: " + String.join(" ", args));

        if (args.length == 0) {
            sendMessyMessage(sender, "That's...not quite correct");
            return false;
        }

        String commandName = args[0];

        switch (commandName) {
            case "bangForce":
                if (sender instanceof Player) {
                    Player player = ((Player) sender).getPlayer();
                    Location location = player.getLocation();
                    World world = player.getWorld();
                    new LuckyEventBang().doAction(null, world, location, player);
                }
                return true;
            case "fortuneForce":
                if (sender instanceof Player) {
                    Player player = ((Player) sender).getPlayer();
                    World world = player.getWorld();
                    Location location = player.getLocation();

                    LuckyEventSlowFall.doAction(null, world, location, player);

                }
                return true;
            default:
                return false;
        }

    }



    void sendMessyMessage(CommandSender sender, String message) {
        if (sender instanceof Player) {
            sender.sendMessage(instance.name + ": " + message);
        } else {
            logger.info(message);
        }
    }

}