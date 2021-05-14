package com.github.shaneyu.playground.lib.registration.registries;

import com.github.shaneyu.playground.lib.provider.IItemProvider;
import com.github.shaneyu.playground.lib.registration.WrappedRegistryObject;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;

public class ItemRegistryObject<ITEM extends Item> extends WrappedRegistryObject<ITEM> implements IItemProvider {
    public ItemRegistryObject(RegistryObject<ITEM> registryObject) {
        super(registryObject);
    }

    @Override
    public ITEM getItem() {
        return get();
    }
}

