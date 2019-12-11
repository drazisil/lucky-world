package com.drazisil.luckyworld;

import com.drazisil.luckyworld.event.LuckyEvent;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.drazisil.luckyworld.Messy.instance;
import static com.drazisil.luckyworld.Messy.logger;
import static com.drazisil.luckyworld.event.EventLuckyHandler.LuckyEventRarity.ALWAYS;
import static com.drazisil.luckyworld.event.EventLuckyHandler.getRandomEvent;

public class MessyCommands implements CommandExecutor {


    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        logger.info("Detected luckyworld command sent with the following args: " + String.join(" ", args));

        if (args.length == 0) {
            sendMessyMessage(sender, "That's...not quite correct");
            return false;
        }

        String commandName = args[0];

        switch (commandName) {
            case "luckyForce":
                if (sender instanceof Player) {
                    Player player = ((Player) sender).getPlayer();
                    Location location = player.getLocation();
                    World world = player.getWorld();
                    LuckyEvent newEvent = getRandomEvent(ALWAYS);
                    player.sendMessage("Hello, you requested a " + newEvent);
                    newEvent.doAction(null, world, location, player);
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