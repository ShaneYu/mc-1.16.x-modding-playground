package com.github.shaneyu.playground.common.network.packet;

import com.github.shaneyu.playground.common.item.interfaces.IModeItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;

import java.util.function.Supplier;

public class PacketModeChange {
    private final boolean displayChangeMessage;
    private final EquipmentSlotType slot;
    private final int shift;

    public PacketModeChange(EquipmentSlotType slot, boolean holdingShift) {
        this(slot, holdingShift ? -1 : 1, true);
    }

    public PacketModeChange(EquipmentSlotType slot, int shift) {
        this(slot, shift, false);
    }

    public PacketModeChange(EquipmentSlotType slot, int shift, boolean displayChangeMessage) {
        this.slot = slot;
        this.shift = shift;
        this.displayChangeMessage = displayChangeMessage;
    }

    public static void handle(PacketModeChange message, Supplier<Context> context) {
        Context ctx = context.get();

        ctx.enqueueWork(() -> {
            PlayerEntity player = ctx.getSender();

            if (player != null) {
                ItemStack stack = player.getItemStackFromSlot(message.slot);

                if (!stack.isEmpty() && stack.getItem() instanceof IModeItem) {
                    ((IModeItem) stack.getItem()).changeMode(player, stack, message.shift, message.displayChangeMessage);
                }
            }
        });

        ctx.setPacketHandled(true);
    }

    public static void encode(PacketModeChange pkt, PacketBuffer buf) {
        buf.writeEnumValue(pkt.slot);
        buf.writeVarInt(pkt.shift);
        buf.writeBoolean(pkt.displayChangeMessage);
    }

    public static PacketModeChange decode(PacketBuffer buf) {
        return new PacketModeChange(buf.readEnumValue(EquipmentSlotType.class), buf.readVarInt(), buf.readBoolean());
    }
}
