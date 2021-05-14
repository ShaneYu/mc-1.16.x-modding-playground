package com.github.shaneyu.playground.common.block.generators;

import com.github.shaneyu.playground.common.block.type.Generator;
import com.github.shaneyu.playground.common.registration.PlaygroundBlockTypes;
import com.github.shaneyu.playground.common.tile.generators.TileEntitySolarGenerator;

public class SolarGeneratorBlock extends GeneratorBlock<TileEntitySolarGenerator, Generator<TileEntitySolarGenerator>> {
    public SolarGeneratorBlock() {
        super(PlaygroundBlockTypes.SOLAR_GENERATOR);
    }
}
