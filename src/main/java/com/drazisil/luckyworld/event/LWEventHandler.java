package com.drazisil.luckyworld.event;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;
import java.util.Objects;

import static com.drazisil.luckyworld.LuckyWorld.logger;
import static com.drazisil.luckyworld.shared.LWUtilities.randInt;

public class LWEventHandler {

    public enum LuckyEventRarity {
        COMMON,
        UNCOMMON,
        RARE,
        ALWAYS,
        PARTS
    }

    private static final ArrayList<LuckyEventEntry> eventsCommon = new ArrayList<>();

    private static final ArrayList<LuckyEventEntry> eventsUncommon = new ArrayList<>();

    private static final ArrayList<LuckyEventEntry> eventsRare = new ArrayList<>();

    private static final ArrayList<LuckyEventEntry> eventsAlways = new ArrayList<>();

    private static final ArrayList<LuckyEventEntry> eventsParts = new ArrayList<>();

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
                break;
            case PARTS:
                eventsParts.add(entry);
                break;
        }
    }

    static Material getRandomMaterial() {
        int max = LuckyEventDisco.coloredGlassBlocks.size();
        int randIndex = randInt(max);
        return LuckyEventDisco.coloredGlassBlocks.get(randIndex);
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
            case PARTS:
                return eventsParts;

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

    public static LuckyEventRarity getRarityByStringName(String rarity) {
        switch (rarity) {
            case "common":
                return LuckyEventRarity.COMMON;
            case "uncommon":
                return LuckyEventRarity.UNCOMMON;
            case "rare":
                return LuckyEventRarity.RARE;
            case "always":
                return LuckyEventRarity.ALWAYS;
        }
        return null;
    }

    public static ArrayList<String> getEventsNamesByType(LuckyEventRarity rarity) {
        ArrayList<String> eventNames = new ArrayList<>();

        ArrayList<LuckyEventEntry> eventEntries = getEventsByRarity(rarity);

        assert eventEntries != null;
        for (LuckyEventEntry eventEntry : eventEntries) {
            eventNames.add(eventEntry.name);
        }

        return eventNames;
    }

    public static LuckyEventEntry getEventByRarityAndName(LuckyEventRarity rarity, String name) {

        ArrayList<LuckyEventEntry> eventEntries = getEventsByRarity(rarity);

        assert eventEntries != null;
        for (LuckyEventEntry eventEntry : eventEntries) {
            if (Objects.equals(eventEntry.name, name)) {
                return eventEntry;
            }
        }
        return null;

    }

    private static LuckyEventRarity getRandEventClass() {
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

    public static boolean shouldEvent(int max, int magicNumber) {
        int number = randInt(max);
        return number == magicNumber;
    }

}
