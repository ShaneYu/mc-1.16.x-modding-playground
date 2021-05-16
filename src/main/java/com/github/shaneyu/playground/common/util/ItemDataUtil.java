package com.github.shaneyu.playground.common.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;

public final class ItemDataUtil {
    private ItemDataUtil() {}

    @Nonnull
    public static CompoundNBT getDataMap(ItemStack stack) {
        initStack(stack);

        return stack.getTag().getCompound(NBTConstants.PLAYGROUND_DATA);
    }

    public static boolean hasData(ItemStack stack, String key, int type) {
        return hasDataTag(stack) && getDataMap(stack).contains(key, type);
    }

    public static int getInt(ItemStack stack, String key) {
        return hasDataTag(stack) ? getDataMap(stack).getInt(key) : 0;
    }

    public static void setInt(ItemStack stack, String key, int i) {
        initStack(stack);
        getDataMap(stack).putInt(key, i);
    }

    private static boolean hasDataTag(ItemStack stack) {
        return stack.getTag() != null && stack.getTag().contains(NBTConstants.PLAYGROUND_DATA, Constants.NBT.TAG_COMPOUND);
    }

    private static void initStack(ItemStack stack) {
        CompoundNBT tag = stack.getOrCreateTag();

        if (!tag.contains(NBTConstants.PLAYGROUND_DATA, Constants.NBT.TAG_COMPOUND)) {
            tag.put(NBTConstants.PLAYGROUND_DATA, new CompoundNBT());
        }
    }
}
