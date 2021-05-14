package com.github.shaneyu.playground.common.localization;

import com.github.shaneyu.playground.common.util.NamedColor;
import com.github.shaneyu.playground.common.util.TextComponentUtil;
import com.github.shaneyu.playground.lib.localization.IHasTranslationKey;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public interface ILanguageEntry extends IHasTranslationKey {
    /**
     * Translates this {@link ILanguageEntry} using a "smart" replacement scheme to allow for automatic replacements, and coloring to take place
     */
    default TranslationTextComponent translate(Object... args) {
        return TextComponentUtil.smartTranslate(getTranslationKey(), args);
    }

    /**
     * Translates this {@link ILanguageEntry} and applies the {@link net.minecraft.util.text.Color} of the given {@link NamedColor} to the {@link ITextComponent}
     */
    default IFormattableTextComponent translateColored(NamedColor color, Object... args) {
        return TextComponentUtil.build(color, translate(args));
    }
}
