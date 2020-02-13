package com.drazisil.luckyworld.shared;

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
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.*;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;
import java.util.Random;

public class LWUtilities {

    public static int randInt(int max) {
        return new Random().nextInt(max);
    }

    public static RoundLocation cleanLocation(Location inLocation) {
        RoundLocation outLocation = new RoundLocation(inLocation.getWorld(),
                inLocation.getX(),
                inLocation.getBlockY(),
                inLocation.getZ());
        outLocation.setX(Math.floor(inLocation.getX()));
        outLocation.setY(Math.floor(inLocation.getY()));
        outLocation.setZ(Math.floor(inLocation.getZ()));
        return outLocation;
    }

    public static String locationToString(Location loc) {
        return Math.floor(loc.getX()) +
                " " + Math.floor(loc.getY()) +
                " " + Math.floor(loc.getZ());
    }

    public static void clearBlockInventory(Block block) {

        // Attempt to clear drops if campfire
        if ((block.getType() == Material.CAMPFIRE)) {
            Campfire state = ((Campfire) block.getState());
            state.setItem(0, null);
            state.setItem(1, null);
            state.setItem(2, null);
            state.setItem(3, null);
            state.update();

        }


        // Attempt to clear drops if jukebox
        if ((block.getType() == Material.JUKEBOX)) {
            ((Jukebox)block.getState()).setRecord(null);
        }

        // Attempt to clear drops if furnace
        if ((block.getType() == Material.FURNACE)
                || (block.getType() == Material.BLAST_FURNACE)
                || (block.getType() == Material.SMOKER)) {
            ((Furnace)block.getState()).getInventory().setFuel(null);
            ((Furnace)block.getState()).getInventory().setResult(null);
            ((Furnace)block.getState()).getInventory().setSmelting(null);
        }

        // Attempt to clear drops if chest
        if ((block.getType() == Material.CHEST)
                || (block.getType() == Material.TRAPPED_CHEST)) {
            ((Chest) block.getState()).getBlockInventory().clear();
        }

        // Attempt to clear drops if brewing stand
        if ((block.getType() == Material.BREWING_STAND)) {
            ((BrewingStand) block.getState()).getInventory().setFuel(null);
            ((BrewingStand) block.getState()).getInventory().setIngredient(null);
            ((BrewingStand) block.getState()).getInventory().clear();
        }


        // Attempt to clear drops if container
        switch (block.getType()) {
            case DISPENSER:
                ((Dispenser) block.getState()).getInventory().clear();
                break;
            case BARREL:
                ((Barrel) block.getState()).getInventory().clear();
                break;
            case HOPPER:
                ((Hopper) block.getState()).getInventory().clear();
                break;
            case DROPPER:
                ((Dropper) block.getState()).getInventory().clear();
                break;
            case LECTERN:
                ((Lectern) block.getState()).getInventory().clear();
                break;
        }
    }

    public static void loadAndPlaceSchematic(World world, Location location, String schematicName) {

        RoundLocation newLocation = cleanLocation(location.clone());

        Clipboard clipboard = null;

        File file = new File( LuckyWorld.getInstance().getDataFolder()  + "/schematics/" + schematicName + ".schem");
        ClipboardFormat format = ClipboardFormats.findByFile(file);
        try  {
            FileInputStream inputStream = new FileInputStream(file);

            ClipboardReader reader = Objects.requireNonNull(format).getReader(inputStream);
            clipboard = reader.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /* use the clipboard here */

//        newLocation.setYaw(0.0f);
//        newLocation.setY((newLocation.getY() - 1));

        try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(BukkitAdapter.adapt(world), -1)) {

            Operation operation = new ClipboardHolder(Objects.requireNonNull(clipboard))
                    .createPaste(editSession)
                    .to(BlockVector3.at(newLocation.getX(), newLocation.getY(), newLocation.getZ()))

                    // configure here
                    .build();
            Operations.complete(operation);
        } catch (WorldEditException e) {
            e.printStackTrace();
        }



    }

    /*
     * this copy(); method copies the specified file from your jar
     *     to your /plugins/<pluginName>/ folder
     */
    public static void copy(URL inUrl, File file) throws IOException {


        URLConnection uc = inUrl.openConnection();
        String contentType = uc.getContentType();
        int contentLength = uc.getContentLength();
        if (contentType.startsWith("text/") || contentLength == -1) {
            throw new IOException("This is not a binary file.");
        }
        InputStream raw = uc.getInputStream();
        InputStream in = new BufferedInputStream(raw);
        byte[] data = new byte[contentLength];
        int bytesRead;
        int offset = 0;
        while (offset < contentLength) {
            bytesRead = in.read(data, offset, data.length - offset);
            if (bytesRead == -1)
                break;
            offset += bytesRead;
        }
        in.close();

        if (offset != contentLength) {
            throw new IOException("Only read " + offset + " bytes; Expected " + contentLength + " bytes");
        }

//        String filename = inUrl.getFile().substring(filename.lastIndexOf('/') + 1);
        FileOutputStream out = new FileOutputStream(file);
        out.write(data);
        out.flush();
        out.close();

//        try {
//            byte[] buffer = new byte[4096];
//            int n;
//
//            OutputStream output = new FileOutputStream( file );
//            while ((n = in.read(buffer)) != -1)
//            {
//                output.write(buffer, 0, n);
//            }
//            output.close();
//
//
////            OutputStream out = new FileOutputStream(file);
////            byte[] buf = new byte[1024 * 64];
////            int len;
////            while((len=in.read(buf, 0, buf.length))>0){
////                System.out.println("156 " + len);
////                out.write(buf,0,len);
////            }
////            out.close();
////            in.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

}
