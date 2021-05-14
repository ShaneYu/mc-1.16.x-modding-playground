package com.github.shaneyu.playground.common.block.type;

import com.github.shaneyu.playground.common.block.attribute.AttributeStateFacing;
import com.github.shaneyu.playground.common.block.attribute.Attributes;
import com.github.shaneyu.playground.common.localization.ILanguageEntry;
import com.github.shaneyu.playground.common.tile.TileEntityBase;
import com.github.shaneyu.playground.lib.registration.registries.TileEntityTypeRegistryObject;

import java.util.function.Supplier;

public class Generator<TILE extends TileEntityBase> extends BlockTypeTile<TILE> { public Generator(Supplier<TileEntityTypeRegistryObject<TILE>> tileEntityRegistrar, ILanguageEntry description) {
    super(tileEntityRegistrar, description);
    add(Attributes.ACTIVE, new AttributeStateFacing());
}

    public static class GeneratorBuilder<GENERATOR extends Generator<TILE>, TILE extends TileEntityBase, T extends GeneratorBuilder<GENERATOR, TILE, T>> extends BlockTileBuilder<GENERATOR, TILE, T> {

        protected GeneratorBuilder(GENERATOR generatorType) {
            super(generatorType);
        }

        public static <TILE extends TileEntityBase> GeneratorBuilder<Generator<TILE>, TILE, ?> createGenerator(
                Supplier<TileEntityTypeRegistryObject<TILE>> tileEntityRegistrar, ILanguageEntry description) {

            return new GeneratorBuilder<>(new Generator<>(tileEntityRegistrar, description));
        }
    }
}
