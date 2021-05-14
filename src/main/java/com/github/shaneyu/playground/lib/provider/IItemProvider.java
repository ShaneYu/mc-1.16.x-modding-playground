package com.github.shaneyu.playground.lib.provider;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IItemProvider extends IBaseProvider, net.minecraft.util.IItemProvider {
    Item getItem();

    @Nonnull
    @Override
    default Item asItem() {
        return getItem();
    }

    default ItemStack getItemStack() {
        return getItemStack(1);
    }

    default ItemStack getItemStack(int size) {
        return new ItemStack(getItem(), size);
    }

    default boolean itemMatches(ItemStack otherStack) {
        return itemMatches(otherStack.getItem());
    }

    default boolean itemMatches(Item other) {
        return getItem() == other;
    }

    @Nullable
    @Override
    default ResourceLocation getRegistryName() {
        return getItem().getRegistryName();
    }

    @Override
    default String getTranslationKey() {
        return getItem().getTranslationKey();
    }
}
