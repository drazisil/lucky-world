package com.drazisil.messy;

import net.minecraft.server.TileEntity;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.TNT;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

import static com.drazisil.messy.Messy.instance;
import static com.drazisil.messy.Messy.logger;

public class MessyCommands implements CommandExecutor {

    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        logger.info("Detected messy command sent with the following args: " + String.join(" ", args));

        if (args.length == 0) {
            sendMessyMessage(sender, "That's...not quite correct");
            return false;
        }

        String commandName = args[0];

        switch (commandName) {
            case "getMultiBlockCount":
                sendMessyMessage(sender, "The current multiBlockCount is: " + instance.getMultiBlockCount());
                break;
            case "setMultiBlockCount":
                if (!(args.length == 2) || (Integer.parseInt(args[1]) < 0)) {
                    sendMessyMessage(sender, "Please give me a number to set it to.");
                    return true;
                }
                instance.setMultiBlockCount(Integer.parseInt(args[1]));
                sendMessyMessage(sender, "The current multiBlockCount is: " + instance.getMultiBlockCount());
                break;
            case "bang":
                logger.info("Should bang: " + instance.shouldBang());
                return true;
            case "bangForce":
                if (sender instanceof Player) {
                    Location location = ((Player) sender).getLocation();
                    World world = ((Player) sender).getWorld();
                    instance.bang(world, location);
                }
                return true;
            default:
                return false;
        }
        return true;

    }

    void sendMessyMessage( CommandSender sender, String message) {
        if (sender instanceof Player) {
            sender.sendMessage(instance.name + ": " + message);
        } else {
            logger.info(message);
        }
    }


}