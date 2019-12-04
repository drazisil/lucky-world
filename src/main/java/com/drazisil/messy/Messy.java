package com.drazisil.messy;

import com.drazisil.messy.event.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.plugin.java.JavaPlugin;

import static com.drazisil.messy.event.EventLuckyHandler.LuckyEventRarity.COMMON;
import static com.drazisil.messy.event.EventLuckyHandler.LuckyEventRarity.RARE;
import static com.drazisil.messy.event.EventLuckyHandler.LuckyEventRarity.UNCOMMON;

public final class Messy extends JavaPlugin {

    static final Logger logger = LogManager.getLogger();
    public static Messy instance;
    static final String name = "Messy";

    @Override
    public void onEnable() {

        EventLuckyHandler.registerEvent(COMMON, new LuckyEventMultiBlock());
        EventLuckyHandler.registerEvent(UNCOMMON, new LuckyEventJumpBoost());
        EventLuckyHandler.registerEvent(UNCOMMON, new LuckyEventSlowFall());
        EventLuckyHandler.registerEvent(RARE, new LuckyEventBang());

        getServer().getPluginManager().registerEvents(new MessyListener(), this);

        // Register command manager
        this.getCommand("messy").setExecutor(new MessyCommands());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
