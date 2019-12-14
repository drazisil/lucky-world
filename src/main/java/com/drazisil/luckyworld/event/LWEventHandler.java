package com.drazisil.luckyworld.event;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import static com.drazisil.luckyworld.LuckyWorld.logger;

public class LWEventHandler {

    public enum LuckyEventRarity {
        COMMON,
        UNCOMMON,
        RARE,
        ALWAYS
    }

    public static ArrayList<LuckyEventEntry> eventsCommon = new ArrayList<>();

    public static ArrayList<LuckyEventEntry> eventsUncommon = new ArrayList<>();

    public static ArrayList<LuckyEventEntry> eventsRare = new ArrayList<>();

    public static ArrayList<LuckyEventEntry> eventsAlways = new ArrayList<>();

    public static void registerEvent(LuckyEventRarity rarity, LuckyEventEntry entry) {
        switch (rarity) {
            case COMMON:
                eventsCommon.add(entry);
                break;
            case UNCOMMON:
                eventsUncommon.add(entry);
                break;
            case RARE:
                eventsRare.add(entry);
                break;
            case ALWAYS:
                eventsAlways.add(entry);
        }
    }

    public static Material getRandomMaterial(ArrayList<Material> set) {
        int max = set.size();
        int randIndex = randInt(max);
        return set.get(randIndex);
    }


    private static LuckyEventEntry getRandomEventFromSet(ArrayList<LuckyEventEntry> set) {
        int max = set.size();
        int randIndex = randInt(max);
        return set.get(randIndex);
    }

    private static ArrayList<LuckyEventEntry> getEventsByRarity(LuckyEventRarity rarity) {
        switch (rarity) {
            case COMMON:
                return eventsCommon;
            case UNCOMMON:
                return eventsUncommon;
            case RARE:
                return eventsRare;
            case ALWAYS:
                return eventsAlways;
        }
        return null;
    }

    public static LuckyEventEntry getRandomEvent(LuckyEventRarity rarity) {
        switch (rarity) {
            case COMMON:
                return getRandomEventFromSet(eventsCommon);
            case UNCOMMON:
                return getRandomEventFromSet(eventsUncommon);
            case RARE:
                return getRandomEventFromSet(eventsRare);
            case ALWAYS:
                return getRandomEventFromSet(eventsAlways);
        }
        return getRandomEventFromSet(eventsCommon);
    }

    public static ArrayList<String> getEventsNamesByType(LuckyEventRarity rarity) {
        ArrayList<String> eventNames = new ArrayList<>();

        ArrayList<LuckyEventEntry> eventEntries = getEventsByRarity(rarity);

        for (int counter = 0; counter < eventEntries.size(); counter++) {
            eventNames.add(eventEntries.get(counter).name);
        }

        return eventNames;
    }

    public static LuckyEventEntry getEventByRarityAndName(LuckyEventRarity rarity, String name) {

        ArrayList<LuckyEventEntry> eventEntries = getEventsByRarity(rarity);

        for (LuckyEventEntry eventEntry : eventEntries) {
            if (Objects.equals(eventEntry.name, name)) {
                return eventEntry;
            }
        }
        return null;

    }

    public static boolean shouldEvent(int max, int magicNumber) {
        int number = randInt(max);
        return number == magicNumber;
    }

    public static int randInt(int max) {
        return new Random().nextInt(max);
    }

    public static LuckyEventRarity getRandEventClass() {
        int number = randInt(100);

        if (number >= 1 && number <= 70) return LuckyEventRarity.COMMON;

        if (number >= 71 && number <= 95) return LuckyEventRarity.UNCOMMON;

        if (number >= 96 && number <= 100) return LuckyEventRarity.RARE;

        return LuckyEventRarity.COMMON;

    }

    public static void handleLuckyEvent(BlockBreakEvent event, World world, Location location, Player player) {
        LuckyEventRarity rarity = getRandEventClass();

        LuckyEventEntry newEvent = getRandomEvent(rarity);

        logger.info("Triggering: " + newEvent.event + " of Rarity: " + rarity.toString());

        newEvent.event.doAction(event, world, location, player);

    }

}
