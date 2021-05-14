package com.github.shaneyu.playground.datagen.providers.common.loot;

import com.github.shaneyu.playground.common.registration.PlaygroundBlocks;
import com.github.shaneyu.playground.lib.provider.IBlockProvider;
import net.minecraft.block.Block;
import net.minecraft.data.loot.BlockLootTables;

import javax.annotation.Nonnull;
import java.util.stream.Collectors;

public final class PlaygroundBlockLootTables extends BlockLootTables {
    @Override
    protected void addTables() {
        addGenerators();
    }

    private void addGenerators() {
        registerDropSelfLootTable(PlaygroundBlocks.SOLAR_GENERATOR.getBlock());
    }

    @Nonnull
    @Override
    protected Iterable<Block> getKnownBlocks() {
        return PlaygroundBlocks.BLOCKS.getAllBlocks().stream().map(IBlockProvider::getBlock).collect(Collectors.toList());
    }
}
