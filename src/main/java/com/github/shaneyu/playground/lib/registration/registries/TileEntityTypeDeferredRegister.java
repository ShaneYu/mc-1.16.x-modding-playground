package com.github.shaneyu.playground.lib.registration.registries;

import com.github.shaneyu.playground.lib.registration.WrappedDeferredRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class TileEntityTypeDeferredRegister extends WrappedDeferredRegister<TileEntityType<?>> {
    public TileEntityTypeDeferredRegister(String modId) {
        super(modId, ForgeRegistries.TILE_ENTITIES);
    }

    @SuppressWarnings("ConstantConditions")
    public <TILE extends TileEntity> TileEntityTypeRegistryObject<TILE> register(BlockRegistryObject<?, ?> block, Supplier<? extends TILE> factory) {
        return register(
                block.getInternalRegistryName(),
                () -> TileEntityType.Builder.<TILE>create(factory, block.getBlock()).build(null), TileEntityTypeRegistryObject::new);
    }
}
