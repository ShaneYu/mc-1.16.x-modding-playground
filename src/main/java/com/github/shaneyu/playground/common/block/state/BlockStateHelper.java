package com.github.shaneyu.playground.common.block.state;

import com.github.shaneyu.playground.common.block.attribute.Attribute;
import com.github.shaneyu.playground.common.block.attribute.AttributeState;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public final class BlockStateHelper {
    private BlockStateHelper() {}

    public static BlockState getDefaultState(BlockState state) {
        Block block = state.getBlock();

        for (Attribute attr : Attribute.getAll(block)) {
            if (attr instanceof AttributeState) {
                state = ((AttributeState) attr).getDefaultState(state);
            }
        }

        return state;
    }

    public static void fillBlockStateContainer(Block block, StateContainer.Builder<Block, BlockState> builder) {
        List<Property<?>> properties = new ArrayList<>();
        for (Attribute attr : Attribute.getAll(block)) {
            if (attr instanceof AttributeState) {
                ((AttributeState) attr).fillBlockStateContainer(block, properties);
            }
        }

        if (!properties.isEmpty()) {
            builder.add(properties.toArray(new Property[0]));
        }
    }

    @Nullable
    public static BlockState getStateForPlacement(Block block, @Nullable BlockState state, BlockItemUseContext context) {
        return getStateForPlacement(block, state, context.getWorld(), context.getPos(), context.getPlayer(), context.getFace());
    }

    @Nullable
    public static BlockState getStateForPlacement(Block block, @Nullable BlockState state, IWorld world, BlockPos pos, @Nullable PlayerEntity player, Direction face) {
        if (state == null) {
            return null;
        }

        for (Attribute attr : Attribute.getAll(block)) {
            if (attr instanceof AttributeState) {
                state = ((AttributeState) attr).getStateForPlacement(block, state, world, pos, player, face);
            }
        }

        return state;
    }
}
