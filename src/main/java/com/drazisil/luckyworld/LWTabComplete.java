package com.drazisil.luckyworld;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.drazisil.luckyworld.event.LWEventHandler.getEventsNamesByType;
import static com.drazisil.luckyworld.event.LWEventHandler.getRarityByStringName;

class LWTabComplete implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if (sender instanceof Player) {

            List<String> commands = new ArrayList<>();

            if (args.length == 1) {
                commands.add("listEvents");
                commands.add("triggerEvent");
                commands.add("luckyForce");
                return  commands;

            }

            if (args.length == 2) {
                String cmd = args[0].toLowerCase();

                switch (cmd) {
                    case "triggerevent":
                    case "listevents":
                        commands.add("common");
                        commands.add("uncommon");
                        commands.add("rare");
                        commands.add("always");
                        return commands;
                }

            }

            if (args.length == 3) {
                String cmd = args[0].toLowerCase();

                switch (cmd) {
                    case "triggerevent":
                        String rarity = args[1].toLowerCase();
                        ArrayList<String> eventNameList = getEventsNamesByType(getRarityByStringName(rarity));
                        if (Objects.equals(rarity, "common")) eventNameList.remove(eventNameList.indexOf("multiblock"));
                        return eventNameList;
                }
            }

        }

        return null;
    }
}
