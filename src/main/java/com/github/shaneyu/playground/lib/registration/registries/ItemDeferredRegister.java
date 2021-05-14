package com.github.shaneyu.playground.lib.registration.registries;

import com.github.shaneyu.playground.common.util.NamedColor;
import com.github.shaneyu.playground.common.util.TextComponentUtil;
import com.github.shaneyu.playground.lib.provider.IItemProvider;
import com.github.shaneyu.playground.lib.registration.WrappedDeferredRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class ItemDeferredRegister extends WrappedDeferredRegister<Item> {
    private final List<IItemProvider> allItems = new ArrayList<>();
    private final Supplier<Item.Properties> defaultItemProperties;

    public ItemDeferredRegister(String modId, Supplier<Item.Properties> defaultItemProperties) {
        super(modId, ForgeRegistries.ITEMS);
        this.defaultItemProperties = defaultItemProperties;
    }

    public ItemRegistryObject<Item> register(String name) {
        return register(name, Item::new);
    }

    public ItemRegistryObject<Item> registerUnburnable(String name) {
        return registerUnburnable(name, Item::new);
    }

    public ItemRegistryObject<Item> register(String name, Rarity rarity) {
        return register(name, properties -> new Item(properties.rarity(rarity)));
    }

    public ItemRegistryObject<Item> register(String name, NamedColor color) {
        return register(name, properties -> new Item(properties) {
            @Nonnull
            @Override
            public IFormattableTextComponent getDisplayName(@Nonnull ItemStack stack) {
                return TextComponentUtil.build(color, super.getDisplayName(stack));
            }
        });
    }

    public <ITEM extends Item> ItemRegistryObject<ITEM> register(String name, Function<Item.Properties, ITEM> sup) {
        return register(name, () -> sup.apply(defaultItemProperties.get()));
    }

    public <ITEM extends Item> ItemRegistryObject<ITEM> registerUnburnable(String name, Function<Item.Properties, ITEM> sup) {
        return register(name, () -> sup.apply(defaultItemProperties.get().isImmuneToFire()));
    }

    public <ITEM extends Item> ItemRegistryObject<ITEM> register(String name, Supplier<? extends ITEM> sup) {
        ItemRegistryObject<ITEM> registeredItem = register(name, sup, ItemRegistryObject::new);
        allItems.add(registeredItem);
        return registeredItem;
    }

    public List<IItemProvider> getAllItems() {
        return allItems;
    }
}
