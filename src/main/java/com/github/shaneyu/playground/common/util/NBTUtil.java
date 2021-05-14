package com.github.shaneyu.playground.common.util;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.Constants;

import java.util.function.IntConsumer;

public final class NBTUtil {
    private NBTUtil() {}

    public static void setBooleanIfPresent(CompoundNBT nbt, String key, BooleanConsumer setter) {
        if (nbt.contains(key, Constants.NBT.TAG_BYTE)) {
            setter.accept(nbt.getBoolean(key));
        }
    }

    public static void setIntIfPresent(CompoundNBT nbt, String key, IntConsumer setter) {
        if (nbt.contains(key, Constants.NBT.TAG_INT)) {
            setter.accept(nbt.getInt(key));
        }
    }
}
