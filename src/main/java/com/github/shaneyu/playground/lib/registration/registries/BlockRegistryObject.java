package com.github.shaneyu.playground.lib.registration.registries;

import com.github.shaneyu.playground.lib.provider.IBlockProvider;
import com.github.shaneyu.playground.lib.registration.DoubleWrappedRegistryObject;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;

public class BlockRegistryObject<BLOCK extends Block, ITEM extends Item> extends DoubleWrappedRegistryObject<BLOCK, ITEM> implements IBlockProvider {
    public BlockRegistryObject(RegistryObject<BLOCK> blockRegistryObject, RegistryObject<ITEM> itemRegistryObject) {
        super(blockRegistryObject, itemRegistryObject);
    }

    @Override
    public BLOCK getBlock() {
        return getPrimary();
    }

    @Override
    public ITEM getItem() {
        return getSecondary();
    }
}
