package com.github.shaneyu.playground.common.network.packet;

import com.github.shaneyu.playground.Playground;
import com.github.shaneyu.playground.common.tile.TileEntityUpdatable;
import com.github.shaneyu.playground.common.util.WorldUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent.Context;

import java.util.Objects;
import java.util.function.Supplier;

public class PacketUpdateTile {
    private final CompoundNBT updateTag;
    private final BlockPos pos;

    public PacketUpdateTile(TileEntityUpdatable tile) {
        this(tile.getPos(), tile.getReducedUpdateTag());
    }

    private PacketUpdateTile(BlockPos pos, CompoundNBT updateTag) {
        this.pos = pos;
        this.updateTag = updateTag;
    }

    public static void handle(PacketUpdateTile message, Supplier<Context> context) {
        Context ctx = context.get();

        ctx.enqueueWork(() -> {
            ClientWorld world = Minecraft.getInstance().world;

            if (world != null) {
                TileEntityUpdatable tile = WorldUtil.getTileEntity(TileEntityUpdatable.class, world, message.pos, true);
                if (tile == null) {
                    Playground.logger.info(
                            "Update tile packet received for position: {} in world: {}, but no valid tile was found.",
                            message.pos, world.getDimensionKey().getLocation());
                } else {
                    tile.handleUpdatePacket(message.updateTag);
                }
            }
        });

        ctx.setPacketHandled(true);
    }

    public static void encode(PacketUpdateTile pkt, PacketBuffer buf) {
        buf.writeBlockPos(pkt.pos);
        buf.writeCompoundTag(pkt.updateTag);
    }

    public static PacketUpdateTile decode(PacketBuffer buf) {
        return new PacketUpdateTile(buf.readBlockPos(), Objects.requireNonNull(buf.readCompoundTag(), "Packet buffer compound tag is null"));
    }
}
