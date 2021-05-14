package com.github.shaneyu.playground.common.block.generators;

import com.github.shaneyu.playground.common.block.prefab.TileBlock;
import com.github.shaneyu.playground.common.block.type.Generator;
import com.github.shaneyu.playground.common.tile.generators.TileEntityGenerator;

public abstract class GeneratorBlock<TILE extends TileEntityGenerator, TYPE extends Generator<TILE>> extends TileBlock<TILE, TYPE> {
    public GeneratorBlock(TYPE blockType) {
        super(blockType);
    }

    public GeneratorBlock(TYPE blockType, Properties properties) {
        super(blockType, properties);
    }
}
