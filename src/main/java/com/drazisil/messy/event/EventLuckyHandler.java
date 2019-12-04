package com.drazisil.messy.event;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;
import java.util.Random;

public class EventLuckyHandler {

    public enum LuckyEventRarity {
        COMMON,
        UNCOMMON,
        RARE
    }

    public static ArrayList<LuckyEvent> eventsCommon = new ArrayList<LuckyEvent>();

    public static ArrayList<LuckyEvent> eventsUncommon = new ArrayList<LuckyEvent>();

    public static ArrayList<LuckyEvent> eventsRare = new ArrayList<LuckyEvent>();

    public static void registerEvent(LuckyEventRarity rarity, LuckyEvent event) {
        switch (rarity) {
            case COMMON:
                eventsCommon.add(event);
                break;
            case UNCOMMON:
                eventsUncommon.add(event);
                break;
            case RARE:
                eventsRare.add(event);
        }
    }

    private static LuckyEvent getRandomEventFromSet(ArrayList<LuckyEvent> set) {
        int max = set.size();
        int randIndex = randInt(max);
        return set.get(randIndex);
    }

    public static LuckyEvent getRandomEvent(LuckyEventRarity rarity) {
        switch (rarity) {
            case COMMON:
                return getRandomEventFromSet(eventsCommon);
            case UNCOMMON:
                return getRandomEventFromSet(eventsUncommon);
            case RARE:
                return getRandomEventFromSet(eventsRare);
        }
        return getRandomEventFromSet(eventsCommon);
    }

    public static boolean shouldEvent(int max, int magicNumber) {
        int number = randInt(max);
        return number == magicNumber;
    }

    public static int randInt(int max) {
        return new Random().nextInt(max);
    }

    public static LuckyEventRarity getRandEventClass() {
        int number = randInt(10);

        if (number >= 1 && number <= 6) return LuckyEventRarity.COMMON;

        if (number >= 7 && number <= 9) return LuckyEventRarity.UNCOMMON;

        if (number == 10) return LuckyEventRarity.RARE;

        return LuckyEventRarity.COMMON;

    }

    public static void handleLuckyEvent(BlockBreakEvent event, World world, Location location, Player player) {
        LuckyEventRarity rarity = getRandEventClass();

        player.sendMessage("Rarity: " + rarity.toString());

        getRandomEvent(rarity).doAction(event, world, location, player);

    }

}
