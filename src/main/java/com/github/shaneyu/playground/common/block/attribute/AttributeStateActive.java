package com.github.shaneyu.playground.common.block.attribute;

import com.github.shaneyu.playground.common.block.state.BlockStateProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.Property;

import java.util.List;

public class AttributeStateActive implements AttributeState {
    private static final BooleanProperty activeProperty = BlockStateProperties.ACTIVE;

    AttributeStateActive() {}

    public boolean isActive(BlockState state) {
        return state.get(activeProperty);
    }

    public BlockState setActive(BlockState state, boolean active) {
        return state.with(activeProperty, active);
    }

    @Override
    public BlockState copyStateData(BlockState oldState, BlockState newState) {
        if (Attribute.has(newState.getBlock(), AttributeStateActive.class)) {
            newState = newState.with(activeProperty, oldState.get(activeProperty));
        }

        return newState;
    }

    @Override
    public BlockState getDefaultState(BlockState state) {
        return state.with(activeProperty, false);
    }

    @Override
    public void fillBlockStateContainer(Block block, List<Property<?>> properties) {
        properties.add(activeProperty);
    }
}
