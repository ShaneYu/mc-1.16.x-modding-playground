package com.github.shaneyu.playground.common.util;

import com.github.shaneyu.playground.common.PlaygroundLang;
import com.github.shaneyu.playground.common.localization.ILanguageEntry;
import com.github.shaneyu.playground.lib.enums.IIncrementalEnum;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.Tags;

import javax.annotation.Nullable;

public enum NamedColor implements IIncrementalEnum<NamedColor> {
    BLACK("\u00a70", PlaygroundLang.COLOR_BLACK, "Black", "Black", "black", new int[]{64, 64, 64}, DyeColor.BLACK),
    DARK_BLUE("\u00a71", PlaygroundLang.COLOR_DARK_BLUE, "Blue", "Blue", "blue", new int[]{54, 107, 208}, DyeColor.BLUE),
    DARK_GREEN("\u00a72", PlaygroundLang.COLOR_DARK_GREEN, "Green", "Green", "green", new int[]{89, 193, 95}, DyeColor.GREEN),
    DARK_AQUA("\u00a73", PlaygroundLang.COLOR_DARK_AQUA, "Cyan", "Cyan", "cyan", new int[]{0, 243, 208}, DyeColor.CYAN),
    DARK_RED("\u00a74", PlaygroundLang.COLOR_DARK_RED, "Dark Red", null, "dark_red", new int[]{201, 7, 31}, MaterialColor.NETHERRACK, Tags.Items.DYES_RED),
    PURPLE("\u00a75", PlaygroundLang.COLOR_PURPLE, "Purple", "Purple", "purple", new int[]{164, 96, 217}, DyeColor.PURPLE),
    ORANGE("\u00a76", PlaygroundLang.COLOR_ORANGE, "Orange", "Orange", "orange", new int[]{255, 161, 96}, DyeColor.ORANGE),
    GRAY("\u00a77", PlaygroundLang.COLOR_GRAY, "Light Gray", "LightGray", "light_gray", new int[]{207, 207, 207}, DyeColor.LIGHT_GRAY),
    DARK_GRAY("\u00a78", PlaygroundLang.COLOR_DARK_GRAY, "Gray", "Gray", "gray", new int[]{122, 122, 122}, DyeColor.GRAY),
    INDIGO("\u00a79", PlaygroundLang.COLOR_INDIGO, "Light Blue", "LightBlue", "light_blue", new int[]{85, 158, 255}, DyeColor.LIGHT_BLUE),
    BRIGHT_GREEN("\u00a7a", PlaygroundLang.COLOR_BRIGHT_GREEN, "Lime", "Lime", "lime", new int[]{117, 255, 137}, DyeColor.LIME),
    AQUA("\u00a7b", PlaygroundLang.COLOR_AQUA, "Aqua", null, "aqua", new int[]{48, 255, 249}, MaterialColor.LIGHT_BLUE, Tags.Items.DYES_LIGHT_BLUE),
    RED("\u00a7c", PlaygroundLang.COLOR_RED, "Red", "Red", "red", new int[]{255, 56, 60}, DyeColor.RED),
    PINK("\u00a7d", PlaygroundLang.COLOR_PINK, "Magenta", "Magenta", "magenta", new int[]{213, 94, 203}, DyeColor.MAGENTA),
    YELLOW("\u00a7e", PlaygroundLang.COLOR_YELLOW, "Yellow", "Yellow", "yellow", new int[]{255, 221, 79}, DyeColor.YELLOW),
    WHITE("\u00a7f", PlaygroundLang.COLOR_WHITE, "White", "White", "white", new int[]{255, 255, 255}, DyeColor.WHITE),
    //Extras for dye-completeness
    BROWN("\u00a76", PlaygroundLang.COLOR_BROWN, "Brown", "Brown", "brown", new int[]{161, 118, 73}, DyeColor.BROWN),
    BRIGHT_PINK("\u00a7d", PlaygroundLang.COLOR_BRIGHT_PINK, "Pink", "Pink", "pink", new int[]{255, 188, 196}, DyeColor.PINK);

    private static final NamedColor[] COLORS = values();

    /** The color code that will be displayed */
    public final String code;

    private int[] rgbCode;
    private Color color;
    private final ILanguageEntry langEntry;
    private final String englishName;
    private final String registryPrefix;
    @Nullable
    private final String dyeName;
    private final MaterialColor mapColor;
    private final ITag<Item> dyeTag;

    NamedColor(String s, ILanguageEntry langEntry, String englishName, @Nullable String dyeName, String registryPrefix, int[] rgbCode, DyeColor dyeColor) {
        this(s, langEntry, englishName, dyeName, registryPrefix, rgbCode, dyeColor.getMapColor(), dyeColor.getTag());
    }

    NamedColor(String code, ILanguageEntry langEntry, String englishName, @Nullable String dyeName, String registryPrefix, int[] rgbCode, MaterialColor mapColor, ITag<Item> dyeTag) {
        this.code = code;
        this.langEntry = langEntry;
        this.englishName = englishName;
        this.dyeName = dyeName;
        this.registryPrefix = registryPrefix;
        setColorFromAtlas(rgbCode);
        this.mapColor = mapColor;
        this.dyeTag = dyeTag;
    }

    public String getRegistryPrefix() {
        return registryPrefix;
    }

    public String getEnglishName() {
        return englishName;
    }

    public MaterialColor getMapColor() {
        return mapColor;
    }

    public ITag<Item> getDyeTag() {
        return dyeTag;
    }

    public boolean hasDyeName() {
        return dyeName != null;
    }

    /**
     * Gets the name of this color with it's color prefix code.
     *
     * @return the color's name and color prefix
     */
    public IFormattableTextComponent getColoredName() {
        return TextComponentUtil.build(this, getName());
    }

    /**
     * Gets the name of this color without coloring the returned result
     *
     * @return the color's name
     */
    public IFormattableTextComponent getName() {
        return new TranslationTextComponent(langEntry.getTranslationKey());
    }

    public ILanguageEntry getLangEntry() {
        return langEntry;
    }

    /**
     * Gets the 0-1 of this color's RGB value by dividing by 255 (used for OpenGL coloring).
     *
     * @param index - R:0, G:1, B:2
     * @return the color value
     */
    public float getColor(int index) {
        return rgbCode[index] / 255F;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public String toString() {
        return code;
    }

    public static NamedColor byIndexStatic(int index) {
        return MathUtil.getByIndexMod(COLORS, index);
    }

    @Override
    public NamedColor byIndex(int index) {
        return byIndexStatic(index);
    }

    public void setColorFromAtlas(int[] color) {
        rgbCode = color;
        this.color = Color.fromInt(rgbCode[0] << 16 | rgbCode[1] << 8 | rgbCode[2]);
    }
}
