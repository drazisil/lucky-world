package com.drazisil.luckyworld.event;

public class LuckyEventEntry {

    public final LuckyEvent event;
    public final String name;

    public LuckyEventEntry(LuckyEvent event, String name) {
        this.event = event;
        this.name = name;
    }
}
