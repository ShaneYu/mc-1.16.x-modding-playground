package com.github.shaneyu.playground.common;

import com.github.shaneyu.playground.Playground;
import com.github.shaneyu.playground.common.localization.ILanguageEntry;
import net.minecraft.util.Util;

public enum PlaygroundLang implements ILanguageEntry {
    // Constants
    PLAYGROUND("constant", "mod_name"),

    // Generic
    GENERIC("generic", "format"),

    // Directions
    DOWN("direction", "down"),
    UP("direction", "up"),
    NORTH("direction", "north"),
    SOUTH("direction", "south"),
    WEST("direction", "west"),
    EAST("direction", "east"),

    // Relative Sides
    FRONT("side", "front"),
    LEFT("side", "left"),
    RIGHT("side", "right"),
    BACK("side", "back"),
    TOP("side", "top"),
    BOTTOM("side", "bottom"),

    // Colors
    COLOR_BLACK("color", "black"),
    COLOR_DARK_BLUE("color", "dark_blue"),
    COLOR_DARK_GREEN("color", "dark_green"),
    COLOR_DARK_AQUA("color", "dark_aqua"),
    COLOR_DARK_RED("color", "dark_red"),
    COLOR_PURPLE("color", "purple"),
    COLOR_ORANGE("color", "orange"),
    COLOR_GRAY("color", "gray"),
    COLOR_DARK_GRAY("color", "dark_gray"),
    COLOR_INDIGO("color", "indigo"),
    COLOR_BRIGHT_GREEN("color", "bright_green"),
    COLOR_AQUA("color", "aqua"),
    COLOR_RED("color", "red"),
    COLOR_PINK("color", "pink"),
    COLOR_YELLOW("color", "yellow"),
    COLOR_WHITE("color", "white"),
    COLOR_BROWN("color", "brown"),
    COLOR_BRIGHT_PINK("color", "bright_pink"),

    // Hold for
    HOLD_FOR_DETAILS("tooltip", "hold_for_details"),
    HOLD_FOR_DESCRIPTION("tooltip", "hold_for_description"),

    // Keys
    KEY_DETAILS_MODE("key", "details"),
    KEY_DESCRIPTION_MODE("key", "description"),

    // Descriptions
    DESCRIPTION_SOLAR_GENERATOR("description", "solar_generator");

    private final String key;

    PlaygroundLang(String type, String path) {
        this(Util.makeTranslationKey(type, Playground.modLoc(path)));
    }

    PlaygroundLang(String key) {
        this.key = key;
    }

    @Override
    public String getTranslationKey() {
        return key;
    }
}
