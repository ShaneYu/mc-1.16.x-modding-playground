package com.github.shaneyu.playground.common.block.type;

import com.github.shaneyu.playground.common.block.attribute.Attribute;
import com.github.shaneyu.playground.common.block.attribute.AttributeSound;
import com.github.shaneyu.playground.common.localization.ILanguageEntry;
import com.github.shaneyu.playground.common.tile.TileEntityBase;
import com.github.shaneyu.playground.lib.registration.registries.SoundEventRegistryObject;
import com.github.shaneyu.playground.lib.registration.registries.TileEntityTypeRegistryObject;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundEvent;

import java.util.function.Supplier;

public class BlockTypeTile<TILE extends TileEntityBase> extends BlockType {
    private final Supplier<TileEntityTypeRegistryObject<TILE>> tileEntityRegistrar;

    public BlockTypeTile(Supplier<TileEntityTypeRegistryObject<TILE>> tileEntityRegistrar, ILanguageEntry description) {
        super(description);
        this.tileEntityRegistrar = tileEntityRegistrar;
    }

    public TileEntityType<TILE> getTileType() {
        return tileEntityRegistrar.get().getTileEntityType();
    }

    public static class BlockTileBuilder<BLOCK extends BlockTypeTile<TILE>, TILE extends TileEntityBase, T extends BlockTileBuilder<BLOCK, TILE, T>> extends BlockTypeBuilder<BLOCK, T> {

        protected BlockTileBuilder(BLOCK holder) {
            super(holder);
        }

        public static <TILE extends TileEntityBase> BlockTileBuilder<BlockTypeTile<TILE>, TILE, ?> createBlock(Supplier<TileEntityTypeRegistryObject<TILE>> tileEntityRegistrar, ILanguageEntry description) {
            return new BlockTileBuilder<>(new BlockTypeTile<>(tileEntityRegistrar, description));
        }

        public T withSound(SoundEventRegistryObject<SoundEvent> soundRegistrar) {
            return with(new AttributeSound(soundRegistrar));
        }

        @SafeVarargs
        public final T with(Attribute.TileAttribute<TILE>... attrs) {
            blockType.add(attrs);

            return getThis();
        }
    }
}