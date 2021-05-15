package com.github.shaneyu.playground.common.registration;

import com.github.shaneyu.playground.Playground;
import com.github.shaneyu.playground.common.block.generators.SolarGeneratorBlock;
import com.github.shaneyu.playground.common.item.BlockItemTooltip;
import com.github.shaneyu.playground.lib.registration.registries.BlockDeferredRegister;
import com.github.shaneyu.playground.lib.registration.registries.BlockRegistryObject;

public final class PlaygroundBlocks {
    private PlaygroundBlocks() {}

    public static final BlockDeferredRegister BLOCKS = new BlockDeferredRegister(Playground.MOD_ID, PlaygroundItems::getItemDefaultProperties);

    public static final BlockRegistryObject<SolarGeneratorBlock, BlockItemTooltip<SolarGeneratorBlock>> SOLAR_GENERATOR =
            BLOCKS.registerBlock("solar_generator", SolarGeneratorBlock::new);
}
