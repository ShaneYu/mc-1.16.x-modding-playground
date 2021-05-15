package com.github.shaneyu.playground.lib.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public interface IWrench {
    boolean canUseWrench(ItemStack stack, PlayerEntity player, BlockPos pos);
}
