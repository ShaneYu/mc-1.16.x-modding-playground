package com.github.shaneyu.playground.lib.item;

import net.minecraft.block.Block;

import javax.annotation.Nonnull;

public class BlockItem<BLOCK extends Block> extends net.minecraft.item.BlockItem {
    private final BLOCK block;

    public BlockItem(BLOCK block, Properties properties) {
        super(block, properties);
        this.block = block;
    }

    @Nonnull
    @Override
    public BLOCK getBlock() {
        return block;
    }
}
