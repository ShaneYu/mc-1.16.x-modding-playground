package com.github.shaneyu.playground.common.util;

import com.github.shaneyu.playground.Playground;
import com.github.shaneyu.playground.common.config.PlaygroundConfig;
import com.github.shaneyu.playground.common.tile.interfaces.IHasActiveState;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public final class WorldUtil {
    private WorldUtil() {}

    /** Check if a position is in bounds of the world, and is loaded */
    @SuppressWarnings("deprecation")
    public static boolean isBlockLoaded(@Nullable IBlockReader world, BlockPos pos) {
        if (world == null || !World.isValid(pos)) {
            return false;
        }

        if (world instanceof IWorldReader) {
            return ((IWorldReader) world).isBlockLoaded(pos);
        }

        return true;
    }

    /** Get a tile entity if the associated block is loaded */
    @Nullable
    public static TileEntity getTileEntity(@Nullable IBlockReader world, BlockPos pos) {
        if (!isBlockLoaded(world, pos)) {
            return null;
        }

        return world.getTileEntity(pos);
    }

    /** Get a tile entity if the associated block is loaded */
    @Nullable
    public static <T extends TileEntity> T getTileEntity(Class<T> clazz, @Nullable IBlockReader world, BlockPos pos) {
        return getTileEntity(clazz, world, pos, false);
    }

    /** Get a tile entity if the associated block is loaded */
    @Nullable
    public static <T extends TileEntity> T getTileEntity(Class<T> clazz, @Nullable IBlockReader world, BlockPos pos, boolean logWrongTileTypeFound) {
        TileEntity tile = getTileEntity(world, pos);

        if (tile == null) {
            return null;
        }

        if (clazz.isInstance(tile)) {
            return clazz.cast(tile);
        }

        if (logWrongTileTypeFound) {
            Playground.logger.warn("Unexpected TileEntity class at {}, expected {}, but found {}", pos, clazz, tile.getClass());
        }

        return null;
    }

    /** Updates a block's light value and marks it for a render update */
    public static void updateBlock(@Nullable World world, BlockPos pos, TileEntity tile) {
        if (!isBlockLoaded(world, pos)) {
            return;
        }

        BlockState blockState = world.getBlockState(pos);
        world.markBlockRangeForRenderUpdate(pos, Blocks.AIR.getDefaultState(), blockState);

        if (!(tile instanceof IHasActiveState) || ((IHasActiveState) tile).lightUpdate() && PlaygroundConfig.client.machineEffects.get()) {
            recheckLighting(world, pos);
        }
    }

    /** Rechecks the lighting at the specific block's position */
    public static void recheckLighting(IBlockDisplayReader world, BlockPos pos) {
        world.getLightManager().checkBlock(pos);
    }

    /** Marks a chunk as dirty if it is currently loaded */
    public static void markChunkDirty(World world, BlockPos pos) {
        if (isBlockLoaded(world, pos)) {
            world.getChunkAt(pos).markDirty();
        }

        // NOTE: This line below is now (1.16+) called by the mark chunk dirty method (without even validating if it is loaded).
        // And with it causes issues where chunks are easily ghost loaded.
    }
}
