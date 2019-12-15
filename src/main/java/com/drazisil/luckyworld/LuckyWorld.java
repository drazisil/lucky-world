package com.drazisil.luckyworld;

import com.drazisil.luckyworld.event.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.plugin.java.JavaPlugin;

import static com.drazisil.luckyworld.event.LWEventHandler.LuckyEventRarity.*;

public final class LuckyWorld extends JavaPlugin {

    public static final Logger logger = LogManager.getLogger();
    public static LuckyWorld instance;
    static final String name = "LuckyWorld";

    @Override
    public void onEnable() {

        instance = this;

        LWEventHandler.registerEvent(COMMON, new LuckyEventEntry(
                new LuckyEventMultiBlock(), "multiblock"));
        LWEventHandler.registerEvent(COMMON, new LuckyEventEntry(
                new LuckyEventUndeadHorse(), "undead_horse"));
        LWEventHandler.registerEvent(COMMON, new LuckyEventEntry(
                new LuckyEventSheep(), "sheep"));
        LWEventHandler.registerEvent(COMMON, new LuckyEventEntry(
                new LuckyEventParrotDance(), "parrot_dance"));
        LWEventHandler.registerEvent(UNCOMMON, new LuckyEventEntry(
                new LuckyEventJumpBoost(), "jump"));
        LWEventHandler.registerEvent(UNCOMMON, new LuckyEventEntry(
                new LuckyEventMooshoom(), "mooshoom"));
        LWEventHandler.registerEvent(UNCOMMON, new LuckyEventEntry(
                new LuckyEventChickens(), "chickens"));
        LWEventHandler.registerEvent(UNCOMMON, new LuckyEventEntry(
                new LuckyEventOuch(), "loony"));
        LWEventHandler.registerEvent(RARE, new LuckyEventEntry(
                new LuckyEventSlowFallGhast(), "ghast"));
        LWEventHandler.registerEvent(RARE, new LuckyEventEntry(
                new LuckyEventBang(), "tnt"));
        LWEventHandler.registerEvent(RARE, new LuckyEventEntry(
                new LuckyEventSealantern(), "sea_lantern"));
        LWEventHandler.registerEvent(RARE, new LuckyEventEntry(
                new LuckyEventDisco(), "disco"));
        LWEventHandler.registerEvent(RARE, new LuckyEventEntry(
                new LuckyEventEndTimes(), "end_times"));
        LWEventHandler.registerEvent(RARE, new LuckyEventEntry(
                new LuckyEventZombieLord(), "miniboss"));


        getServer().getPluginManager().registerEvents(new LWListener(), this);

        // Register command manager
        this.getCommand("lucky").setExecutor(new LWCommands());
        this.getCommand("lucky").setTabCompleter(new LWTabComplete());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static LuckyWorld getInstance() {
        return instance;
    }
}
