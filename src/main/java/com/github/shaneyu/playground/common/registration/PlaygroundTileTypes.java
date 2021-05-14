package com.github.shaneyu.playground.common.registration;

import com.github.shaneyu.playground.Playground;
import com.github.shaneyu.playground.common.tile.generators.TileEntitySolarGenerator;
import com.github.shaneyu.playground.lib.registration.registries.TileEntityTypeDeferredRegister;
import com.github.shaneyu.playground.lib.registration.registries.TileEntityTypeRegistryObject;

public final class PlaygroundTileTypes {
    private PlaygroundTileTypes() {}

    public static final TileEntityTypeDeferredRegister TILE_ENTITY_TYPES = new TileEntityTypeDeferredRegister(Playground.MOD_ID);

    public static final TileEntityTypeRegistryObject<TileEntitySolarGenerator> SOLAR_GENERATOR =
            TILE_ENTITY_TYPES.register(PlaygroundBlocks.SOLAR_GENERATOR, TileEntitySolarGenerator::new);
}
