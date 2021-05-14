package com.github.shaneyu.playground.lib.provider;

import com.github.shaneyu.playground.lib.localization.IHasTranslationKey;
import com.github.shaneyu.playground.lib.text.IHasTextComponent;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public interface IBaseProvider extends IHasTextComponent, IHasTranslationKey {
    ResourceLocation getRegistryName();

    default String getName() {
        return getRegistryName().getPath();
    }

    @Override
    default ITextComponent getTextComponent() {
        return new TranslationTextComponent(getTranslationKey());
    }
}
