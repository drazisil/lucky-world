package com.drazisil.luckyworld.event;

public class LuckyEventEntry {

    public LuckyEvent event;
    public String name;

    public LuckyEventEntry(LuckyEvent event, String name) {
        this.event = event;
        this.name = name;
    }
}
