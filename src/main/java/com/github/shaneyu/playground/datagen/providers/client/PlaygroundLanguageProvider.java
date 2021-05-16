package com.github.shaneyu.playground.datagen.providers.client;

import com.github.shaneyu.playground.Playground;
import com.github.shaneyu.playground.common.PlaygroundLang;
import com.github.shaneyu.playground.common.registration.PlaygroundBlocks;
import com.github.shaneyu.playground.common.util.NamedColor;
import com.github.shaneyu.playground.lib.localization.IHasTranslationKey;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

import javax.annotation.Nonnull;

public class PlaygroundLanguageProvider extends LanguageProvider {
    public PlaygroundLanguageProvider(DataGenerator gen) {
        super(gen, Playground.MOD_ID, "en_us");
    }

    @Nonnull
    @Override
    public String getName() {
        return Playground.MOD_NAME + " - Language";
    }

    @Override
    protected void addTranslations() {
        addItems();
        addBlocks();
        addMisc();
    }

    private void addItems() {
        // TODO: Add mod item translates here
    }

    private void addBlocks() {
        add(PlaygroundBlocks.SOLAR_GENERATOR, "Solar Generator");
    }

    private void addMisc() {
        // Constants
        add(PlaygroundLang.PLAYGROUND, Playground.MOD_NAME);

        // Colors
        for (NamedColor color : NamedColor.values()) {
            add(color.getLangEntry(), color.getEnglishName());
        }

        // GUI
        add(PlaygroundLang.LOG_FORMAT, "[%s] %s");

        // Generic
        add(PlaygroundLang.GENERIC, "%s");

        // Directions
        add(PlaygroundLang.DOWN, "Down");
        add(PlaygroundLang.UP, "Up");
        add(PlaygroundLang.NORTH, "North");
        add(PlaygroundLang.SOUTH, "South");
        add(PlaygroundLang.WEST, "West");
        add(PlaygroundLang.EAST, "East");

        // Relative sides
        add(PlaygroundLang.FRONT, "Front");
        add(PlaygroundLang.LEFT, "Left");
        add(PlaygroundLang.RIGHT, "Right");
        add(PlaygroundLang.BACK, "Back");
        add(PlaygroundLang.TOP, "Top");
        add(PlaygroundLang.BOTTOM, "Bottom");

        // Hold for
        add(PlaygroundLang.HOLD_FOR_DETAILS, "Hold %s for details.");
        add(PlaygroundLang.HOLD_FOR_DESCRIPTION, "Hold %s for a description.");

        // Keys
        add(PlaygroundLang.KEY_DETAILS_MODE, "Show Details");
        add(PlaygroundLang.KEY_DESCRIPTION_MODE, "Show Description");

        // Wrench states
        add(PlaygroundLang.WRENCH_CONFIGURE_STATE, "Configure State: %s");
        add(PlaygroundLang.WRENCH_STATE, "State: %s");

        // Wrench modes
        add(PlaygroundLang.WRENCH_MODE_DISMANTLE, "Dismantle");
        add(PlaygroundLang.WRENCH_MODE_ROTATE, "Rotate");

        // Descriptions
        add(PlaygroundLang.DESCRIPTION_SOLAR_GENERATOR, "A generator that generates energy during the day from sun.");
    }

    private void add(IHasTranslationKey key, String value) {
        add(key.getTranslationKey(), value);
    }

    private static String formatAndCapitalize(String s) {
        boolean isFirst = true;
        StringBuilder ret = new StringBuilder();

        for (char c : s.toCharArray()) {
            if (c == '_') {
                isFirst = true;
                ret.append(' ');
            } else {
                ret.append(isFirst ? Character.toUpperCase(c) : c);
                isFirst = false;
            }
        }

        return ret.toString();
    }
}
