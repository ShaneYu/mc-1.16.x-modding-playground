package com.github.shaneyu.playground.common;

import com.github.shaneyu.playground.Playground;
import com.github.shaneyu.playground.common.registration.PlaygroundBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;

public class PlaygroundCreativeTab extends ItemGroup {
    public PlaygroundCreativeTab() {
        super(Playground.MOD_ID);
    }

    @Nonnull
    @Override
    public ItemStack createIcon() {
        return new ItemStack(PlaygroundBlocks.SOLAR_GENERATOR);
    }

    @Nonnull
    @Override
    public ITextComponent getGroupName() {
        return PlaygroundLang.PLAYGROUND.translate();
    }
}
