package com.github.shaneyu.playground.common.registration;

import com.github.shaneyu.playground.Playground;
import com.github.shaneyu.playground.common.block.generators.SolarGeneratorBlock;
import com.github.shaneyu.playground.common.block.interfaces.IHasDescription;
import com.github.shaneyu.playground.common.item.BlockItemTooltip;
import com.github.shaneyu.playground.lib.registration.registries.BlockDeferredRegister;
import com.github.shaneyu.playground.lib.registration.registries.BlockRegistryObject;
import net.minecraft.block.Block;

import java.util.function.Supplier;

public final class PlaygroundBlocks {
    private PlaygroundBlocks() {}

    public static final BlockDeferredRegister BLOCKS = new BlockDeferredRegister(Playground.MOD_ID, PlaygroundItems::getItemDefaultProperties);

    public static final BlockRegistryObject<SolarGeneratorBlock, BlockItemTooltip<SolarGeneratorBlock>> SOLAR_GENERATOR =
            registerBlock("solar_generator", SolarGeneratorBlock::new);

    private static <BLOCK extends Block & IHasDescription> BlockRegistryObject<BLOCK, BlockItemTooltip<BLOCK>> registerBlock(
            String name, Supplier<? extends BLOCK> blockSupplier) {

        return BLOCKS.registerDefaultProperties(name, blockSupplier, BlockItemTooltip::new);
    }
}
