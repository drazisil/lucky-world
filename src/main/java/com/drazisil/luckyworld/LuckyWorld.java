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

        LWEventHandler.registerEvent(COMMON, new LuckyEventMultiBlock());
        LWEventHandler.registerEvent(COMMON, new LuckyEventUndeadHorse());
        LWEventHandler.registerEvent(COMMON, new LuckyEventSheep());
        LWEventHandler.registerEvent(COMMON, new LuckyEventParrotDance());
        LWEventHandler.registerEvent(UNCOMMON, new LuckyEventJumpBoost());
        LWEventHandler.registerEvent(UNCOMMON, new LuckyEventMooshoom());
        LWEventHandler.registerEvent(UNCOMMON, new LuckyEventChickens());
        LWEventHandler.registerEvent(RARE, new LuckyEventSlowFallGhast());
        LWEventHandler.registerEvent(RARE, new LuckyEventBang());
        LWEventHandler.registerEvent(RARE, new LuckyEventSealantern());

        LWEventHandler.registerEvent(ALWAYS, new LuckyEventDisco());

        getServer().getPluginManager().registerEvents(new LWListener(), this);

        // Register command manager
        this.getCommand("lucky").setExecutor(new LWCommands());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static LuckyWorld getInstance() {
        return instance;
    }
}
