package com.github.shaneyu.playground.common.block.attribute;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.Property;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import javax.annotation.Nullable;
import java.util.List;

public interface AttributeState extends Attribute {
    BlockState copyStateData(BlockState oldState, BlockState newState);

    void fillBlockStateContainer(Block block, List<Property<?>> properties);

    default BlockState getDefaultState(BlockState state) {
        return state;
    }

    @Nullable
    default BlockState getStateForPlacement(
            Block block, @Nullable BlockState state, IWorld world, BlockPos pos, @Nullable PlayerEntity player, Direction face) {

        return state;
    }
}
