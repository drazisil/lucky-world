package com.drazisil.messy.event;

import java.util.Random;

public class EventLuckyHandler {

    public enum LuckyEventRarity {
        COMMON,
        UNCOMMON,
        RARE
    }

    public static boolean shouldEvent(int max, int magicNumber) {
        int number = randInt(max);
        return number == magicNumber;
    }

    public static int randInt(int max) {
        return new Random().nextInt(max);
    }



}
