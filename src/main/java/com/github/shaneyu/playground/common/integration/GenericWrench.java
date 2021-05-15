package com.github.shaneyu.playground.common.integration;

import com.github.shaneyu.playground.lib.item.IWrench;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class GenericWrench implements IWrench {
    private GenericWrench() {}

    public static final GenericWrench INSTANCE = new GenericWrench();

    @Override
    public boolean canUseWrench(ItemStack stack, PlayerEntity player, BlockPos pos) {
        return true;
    }
}
