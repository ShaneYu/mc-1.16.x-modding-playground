package com.github.shaneyu.playground.common.block.state;

import net.minecraft.state.BooleanProperty;

public class BlockStateProperties extends net.minecraft.state.properties.BlockStateProperties {
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
}
