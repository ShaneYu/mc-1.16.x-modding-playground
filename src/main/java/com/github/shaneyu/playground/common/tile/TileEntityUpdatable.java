package com.github.shaneyu.playground.common.tile;

import com.github.shaneyu.playground.Playground;
import com.github.shaneyu.playground.common.config.PlaygroundConfig;
import com.github.shaneyu.playground.common.network.packet.PacketUpdateTile;
import com.github.shaneyu.playground.common.tile.interfaces.IBlockTile;
import com.github.shaneyu.playground.common.util.WorldUtil;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketDirection;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public abstract class TileEntityUpdatable extends TileEntity implements IBlockTile {
    protected TileEntityUpdatable(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    /**
     * Like getWorld(), but for when you _know_ world won't be null
     *
     * @return The world!
     */
    protected World getWorldNN() {
        return Objects.requireNonNull(getWorld(), "getWorldNN called before world set");
    }

    public boolean isRemote() {
        return getWorldNN().isRemote();
    }

    /**
     * Used for checking if we need to update comparators.
     * Only called on the server.
     */
    public void markDirtyComparator() {}

    @Override
    public void markDirty() {
        markDirty(true);
    }

    public void markDirty(boolean recheckBlockState) {
        // Copy of the base impl of markDirty in TileEntity, except only updates comparator state when something changed
        // and if our block supports having a comparator signal, instead of always doing it
        if (world != null) {
            if (recheckBlockState) {
                cachedBlockState = world.getBlockState(pos);
            }

            WorldUtil.markChunkDirty(world, pos);

            if (!isRemote()) {
                markDirtyComparator();
            }
        }
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(getPos(), 0, getUpdateTag());
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        // We don't want to do a full read from NBT so simply call the super's read method to let Forge do whatever
        // it wants, but don't treat this as if it was the full saved NBT data as not everything has to be synced to the client
        super.read(state, tag);
    }

    @Nonnull
    @Override
    public CompoundNBT getUpdateTag() {
        return getReducedUpdateTag();
    }

    /**
     * Similar to {@link #getUpdateTag()} but with reduced information for when we are doing our own syncing.
     */
    public CompoundNBT getReducedUpdateTag() {
        // Add the base update tag information
        return super.getUpdateTag();
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        if (isRemote() && net.getDirection() == PacketDirection.CLIENTBOUND) {
            // Handle the update tag when we are on the client
            handleUpdatePacket(pkt.getNbtCompound());
        }
    }

    public void handleUpdatePacket(CompoundNBT tag) {
        handleUpdateTag(getBlockState(), tag);
    }

    public void sendUpdatePacket() {
        sendUpdatePacket(this);
    }

    public void sendUpdatePacket(TileEntity tracking) {
        if (isRemote()) {
            Playground.logger.warn("Update packet call requested from client side", new IllegalStateException());
        } else if (isRemoved()) {
            Playground.logger.warn("Update packet call requested for removed tile", new IllegalStateException());
        } else {
            // Note: We use our own update packet/channel to avoid chunk trashing and minecraft attempting to re-render
            // the entire chunk when most often we are just updating a TileEntityRenderer, so the chunk itself
            // does not need to and should not be redrawn
            Playground.packetHandler.sendToAllTracking(new PacketUpdateTile(this), tracking);
        }
    }

    @Nullable
    @Override
    public World getTileWorld() {
        return getWorld();
    }

    @Override
    public BlockPos getTilePos() {
        return getPos();
    }
}
