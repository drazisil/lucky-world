package com.drazisil.luckyworld;

import com.drazisil.luckyworld.event.LuckyEventEntry;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;

import static com.drazisil.luckyworld.LuckyWorld.logger;
import static com.drazisil.luckyworld.event.LWEventHandler.LuckyEventRarity.*;
import static com.drazisil.luckyworld.event.LWEventHandler.*;

class LWCommands implements CommandExecutor {


    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {

        if (!(sender instanceof Player)) return false;

        logger.info("Detected " + LuckyWorld.name + " command sent with the following args: " + String.join(" ", args));

        if (args.length == 0) {
            sender.sendMessage("That's...not quite correct");
            return false;
        }

        String commandName = args[0];

        Player player = ((Player) sender).getPlayer();
        assert player != null;

        Location location = player.getLocation();
        World world = player.getWorld();
        String rarity;

        switch (commandName) {
            case "listEvents":
                if (args.length < 2) {
                    sender.sendMessage("That's...not quite correct");
                    return false;
                }


                rarity = args[1];

                switch (rarity.toLowerCase()) {
                    case "common":
                        ArrayList<String> commonEvents = getEventsNamesByType(COMMON);
                        commonEvents.remove("multiblock");
                        sender.sendMessage(commonEvents.toString());
                        return true;
                    case "uncommon":
                        sender.sendMessage(getEventsNamesByType(UNCOMMON).toString());
                        return true;
                    case "rare":
                        sender.sendMessage(getEventsNamesByType(RARE).toString());
                        return true;
                    case "always":
                        sender.sendMessage(getEventsNamesByType(ALWAYS).toString());
                        return true;
                    default:
                        sender.sendMessage("That's not a valid rarity, please try again.");
                        return false;
                }

            case "triggerEvent":
                if (args.length < 3) {
                    sender.sendMessage("That's...not quite correct");
                    return false;
                }


                rarity = args[1];
                String eventName = args[2].toLowerCase();

                LuckyEventEntry eventEntry;

                switch (rarity.toLowerCase()) {
                    case "common":
                        eventEntry = getEventByRarityAndName(COMMON, eventName);
                        break;
                    case "uncommon":
                        eventEntry = getEventByRarityAndName(UNCOMMON, eventName);
                        break;
                    case "rare":
                        eventEntry = getEventByRarityAndName(RARE, eventName);
                        break;
                    case "always":
                        eventEntry = getEventByRarityAndName(ALWAYS, eventName);
                        break;
                    default:
                        sender.sendMessage("That's not a valid rarity, please try again.");
                        return false;
                }

                if (eventEntry == null) {
                    sender.sendMessage("Unable to locate a event by that rarity and name.");
                    return false;
                }

                eventEntry.event.doAction(null, world, location, player);
                return true;



            case "luckyForce":
                LuckyEventEntry newEvent = getRandomEvent(ALWAYS);
                player.sendMessage("Hello, you requested a " + newEvent);
                newEvent.event.doAction(null, world, location, player);
                return true;
            default:
                return false;
        }

    }

}