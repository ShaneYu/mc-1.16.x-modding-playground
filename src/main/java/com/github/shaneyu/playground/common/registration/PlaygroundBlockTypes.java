package com.github.shaneyu.playground.common.registration;

import com.github.shaneyu.playground.common.PlaygroundLang;
import com.github.shaneyu.playground.common.block.type.Generator;
import com.github.shaneyu.playground.common.tile.generators.TileEntitySolarGenerator;

public final class PlaygroundBlockTypes {
    private PlaygroundBlockTypes() {}

    // Solar generator
    public static final Generator<TileEntitySolarGenerator> SOLAR_GENERATOR = Generator.GeneratorBuilder
            .createGenerator(() -> PlaygroundTileTypes.SOLAR_GENERATOR, PlaygroundLang.DESCRIPTION_SOLAR_GENERATOR)
            .withSound(PlaygroundSounds.SOLAR_GENERATOR)
            .build();
}
