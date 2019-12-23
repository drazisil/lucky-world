package com.drazisil.luckyworld;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import java.util.ArrayList;

public class BlockSaveRecord {

    public enum CenterType {
        NONE,
        CENTER,
        CENTER_OFFSET_X,
        CENTER_OFFSET_Y,
        CENTER_OFFSET_Z
    }

    private World world;
    private int height;
    private int width;
    private int depth;

    public double getLeftSideX() {
        return leftSideX;
    }

    public double getRightSideX() {
        return rightSideX;
    }

    public double getFrontSideZ() {
        return frontSideZ;
    }

    public double getBackSideZ() {
        return backSideZ;
    }

    public double getTopSideY() {
        return topSideY;
    }

    public double getBottomSideY() {
        return bottomSideY;
    }

    private double leftSideX;
    private double rightSideX;
    private double frontSideZ;
    private double backSideZ;
    private double topSideY;
    private double bottomSideY;

    private ArrayList<BlockSave> blocks = new ArrayList<>();


    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public ArrayList<BlockSave> getBlocks() {
        return blocks;
    }

    public void addBlock(BlockSave block) {
        this.blocks.add(block);
    }

    public BlockSaveRecord generateBlockSaveCube(World world, Location startLocation,
                                                 int height, int width, int depth,
                                                 CenterType centerType, int offsetX,
                                                 int offsetY, int offsetZ) {

        setWorld(world);
        setHeight(height);
        setWidth(width);
        setDepth(depth);

        double startX = 0.0d;
        double startY = 0.0d;
        double startZ = 0.0d;

        switch (centerType) {
            case CENTER:
                startX = getSquareStartX(startLocation, width);
                startY = getSquareStartY(startLocation, height);
                startZ = getSquareStartZ(startLocation, depth);
                break;
            case CENTER_OFFSET_Y:
                startX = getSquareStartX(startLocation, width);
                startY = getSquareStartY(startLocation, height) + offsetY;
                startZ = getSquareStartZ(startLocation, depth);
                break;

            case NONE:
                startX = startLocation.getX();
                startY = startLocation.getY();
                startZ = startLocation.getZ();
                break;
        }

        computeSides(startX, startY, startZ);

        Location cursorLocation = startLocation.clone();

        for (double y = startY; y > (startY - depth); y -= 1.0d) {
            cursorLocation.setY(y);

            for (double x = startX; x < (startX + width); x += 1.0d) {
                cursorLocation.setX(x);

                for (double z = startZ; z < (startZ + depth); z += 1.0d) {
                    cursorLocation.setZ(z);

                    // Capture the block
                    Block block = cursorLocation.getBlock();
                    Material type = block.getType();
                    BlockState state = block.getState();

                    addBlock(new BlockSave(block, type, cursorLocation, state));
                }
            }

        }
        return this;
    }

    private void computeSides(double startX, double startY, double startZ) {
        this.leftSideX = startX;
        this.frontSideZ = startZ;
        this.topSideY = startY;

        this.rightSideX = this.leftSideX + this.width;
        this.backSideZ = this.frontSideZ + this.depth;
        this.bottomSideY = this.topSideY - this.height;
    }

    private double getSquareStartX(Location location, int width) {
        assert (width % 2) == 0;

        int startOffset = (width - 1) / 2;

        Location startLocation = location.clone();
        startLocation.setX(startLocation.getX() - startOffset);
        return  startLocation.getX();
    }

    private double getSquareStartY(Location location, int height) {
        assert (height % 2) == 0;

        int startOffset = (height - 1) / 2;

        Location startLocation = location.clone();
        startLocation.setY(startLocation.getY() + startOffset);
        return startLocation.getY();
    }

    private double getSquareStartZ(Location location, int depth) {
        assert (depth % 2) == 0;

        int startOffset = (depth - 1) / 2;

        Location startLocation = location.clone();
        startLocation.setZ(startLocation.getZ() - startOffset);
        return startLocation.getZ();
    }

    public void restoreAll(BlockSaveRecord blocksToChange) {
        assert getBlocks().size() == blocksToChange.getBlocks().size();

        ArrayList<BlockSave> currentBlocks = blocksToChange.getBlocks();
        ArrayList<BlockSave> restoreBlocks = getBlocks();


        for (int i = 0; i < blocksToChange.getBlocks().size(); i++) {
            BlockSave currentBlock = currentBlocks.get(i);
            BlockSave restoreBlock = restoreBlocks.get(i);

            currentBlock.getBlock().setType(restoreBlock.getType());
            restoreBlock.getState().update();
        }

    }

    public void setWorld(World world) {
        this.world = world;
    }
}
