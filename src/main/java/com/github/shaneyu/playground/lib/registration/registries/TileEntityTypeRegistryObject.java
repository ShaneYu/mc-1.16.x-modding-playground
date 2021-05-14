package com.github.shaneyu.playground.lib.registration.registries;

import com.github.shaneyu.playground.lib.registration.WrappedRegistryObject;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;

public class TileEntityTypeRegistryObject<TILE extends TileEntity> extends WrappedRegistryObject<TileEntityType<TILE>> {
    public TileEntityTypeRegistryObject(RegistryObject<TileEntityType<TILE>> registryObject) {
        super(registryObject);
    }

    public TileEntityType<TILE> getTileEntityType() {
        return get();
    }
}
