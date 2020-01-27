package com.drazisil.luckyworld.event;

import com.drazisil.luckyworld.LuckyWorld;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static com.drazisil.luckyworld.event.LWEventHandler.LuckyEventRarity.PARTS;
import static com.drazisil.luckyworld.event.LWEventHandler.getEventByRarityAndName;

public class LuckyEventWE extends LuckyEvent {


    @Override
    public void doAction(BlockBreakEvent event, World world, Location location, Player player) {
        Clipboard clipboard = null;

        File file = new File( LuckyWorld.getInstance().getDataFolder()+ "/../WorldEdit/schematics/" + "FloatingCastle.schem");

        ClipboardFormat format = ClipboardFormats.findByFile(file);
        try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
            clipboard = reader.read();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
/* use the clipboard here */
        Location newLocation = player.getLocation().clone();
        newLocation.setY(225);

        Location signLocation = newLocation.clone();
        signLocation.add(2.0d, -1.0d, 5.0d);

        newLocation.setYaw(0.0f);
        player.teleport(newLocation);
        newLocation.setY(newLocation.getY()-1);
        try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(BukkitAdapter.adapt(world), -1)) {
            Operation operation = new ClipboardHolder(clipboard)
                    .createPaste(editSession)
                    .to(BlockVector3.at(newLocation.getX(), newLocation.getY(), newLocation.getZ()))
                    // configure here
                    .build();
            Operations.complete(operation);
        } catch (WorldEditException e) {
            e.printStackTrace();
        }
        getEventByRarityAndName(PARTS, "sign").event.doAction(null, world, signLocation, player);

    }
}
