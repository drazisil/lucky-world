package com.drazisil.messy;

import com.drazisil.messy.event.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.plugin.java.JavaPlugin;

import static com.drazisil.messy.event.EventLuckyHandler.LuckyEventRarity.COMMON;
import static com.drazisil.messy.event.EventLuckyHandler.LuckyEventRarity.RARE;
import static com.drazisil.messy.event.EventLuckyHandler.LuckyEventRarity.UNCOMMON;

public final class Messy extends JavaPlugin {

    public static final Logger logger = LogManager.getLogger();
    public static Messy instance;
    static final String name = "Messy";

    @Override
    public void onEnable() {

        EventLuckyHandler.registerEvent(COMMON, new LuckyEventMultiBlock());
        EventLuckyHandler.registerEvent(COMMON, new LuckyEventUndeadHorse());
        EventLuckyHandler.registerEvent(COMMON, new LuckyEventSheep());
        EventLuckyHandler.registerEvent(COMMON, new LuckyEventParrotDance());
        EventLuckyHandler.registerEvent(UNCOMMON, new LuckyEventJumpBoost());
        EventLuckyHandler.registerEvent(UNCOMMON, new LuckyEventMooshoom());
        EventLuckyHandler.registerEvent(UNCOMMON, new LuckyEventChickens());
        EventLuckyHandler.registerEvent(RARE, new LuckyEventSlowFallGhast());
        EventLuckyHandler.registerEvent(RARE, new LuckyEventBang());
        EventLuckyHandler.registerEvent(RARE, new LuckyEventSealantern());

        getServer().getPluginManager().registerEvents(new MessyListener(), this);

        // Register command manager
        this.getCommand("messy").setExecutor(new MessyCommands());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
