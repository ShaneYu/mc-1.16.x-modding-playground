package com.github.shaneyu.playground.lib.registration.registries;

import com.github.shaneyu.playground.common.block.interfaces.IHasDescription;
import com.github.shaneyu.playground.common.item.BlockItemTooltip;
import com.github.shaneyu.playground.lib.provider.IBlockProvider;
import com.github.shaneyu.playground.lib.registration.DoubleWrappedDeferredRegister;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.github.shaneyu.playground.common.registration.PlaygroundBlocks.BLOCKS;

public class BlockDeferredRegister extends DoubleWrappedDeferredRegister<Block, Item> {
    private final List<IBlockProvider> allBlocks = new ArrayList<>();
    private final Supplier<Item.Properties> defaultItemProperties;

    public BlockDeferredRegister(String modId, Supplier<Item.Properties> defaultItemProperties) {
        super(modId, ForgeRegistries.BLOCKS, ForgeRegistries.ITEMS);
        this.defaultItemProperties = defaultItemProperties;
    }

    public <BLOCK extends Block, ITEM extends BlockItem> BlockRegistryObject<BLOCK, ITEM> register(
            String name, Supplier<? extends BLOCK> blockSupplier, Function<BLOCK, ITEM> itemCreator) {

        BlockRegistryObject<BLOCK, ITEM> registeredBlock = register(name, blockSupplier, itemCreator, BlockRegistryObject::new);
        allBlocks.add(registeredBlock);

        return registeredBlock;
    }

    public BlockRegistryObject<Block, BlockItem> register(String name, AbstractBlock.Properties properties) {
        return registerDefaultProperties(name, () -> new Block(properties), BlockItem::new);
    }

    public <BLOCK extends Block> BlockRegistryObject<BLOCK, BlockItem> register(String name, Supplier<? extends BLOCK> blockSupplier) {
        return registerDefaultProperties(name, blockSupplier, BlockItem::new);
    }

    public <BLOCK extends Block, ITEM extends BlockItem> BlockRegistryObject<BLOCK, ITEM> registerDefaultProperties(
            String name, Supplier<? extends BLOCK> blockSupplier, BiFunction<BLOCK, Item.Properties, ITEM> itemCreator) {

        return register(name, blockSupplier, block -> itemCreator.apply(block, defaultItemProperties.get()));
    }

    public <BLOCK extends Block & IHasDescription> BlockRegistryObject<BLOCK, BlockItemTooltip<BLOCK>> registerBlock(
            String name, Supplier<? extends BLOCK> blockSupplier) {

        return BLOCKS.registerDefaultProperties(name, blockSupplier, BlockItemTooltip::new);
    }

    public List<IBlockProvider> getAllBlocks() {
        return allBlocks;
    }
}
