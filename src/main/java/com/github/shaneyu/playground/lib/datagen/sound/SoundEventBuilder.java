package com.github.shaneyu.playground.lib.datagen.sound;

import com.github.shaneyu.playground.lib.datagen.DataConstants;
import com.github.shaneyu.playground.lib.localization.IHasTranslationKey;
import com.github.shaneyu.playground.lib.registration.registries.SoundEventRegistryObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class SoundEventBuilder {
    public static SoundEventBuilder create(SoundEventRegistryObject<?> soundEventRO) {
        return new SoundEventBuilder(soundEventRO);
    }

    private final String path;
    private boolean replace;
    @Nullable
    private String translationKey;
    private final Map<ResourceLocation, SoundBuilder> soundBuilders = new LinkedHashMap<>();

    private SoundEventBuilder(SoundEventRegistryObject<?> soundEventRO) {
        // noinspection ConstantConditions
        path = soundEventRO.getSoundEvent().getRegistryName().getPath();
    }

    public String getPath() {
        return path;
    }

    public SoundEventBuilder replace() {
        this.replace = true;
        return this;
    }

    public SoundEventBuilder subtitle(IHasTranslationKey langEntry) {
        this.translationKey = Objects.requireNonNull(langEntry).getTranslationKey();
        return this;
    }

    public SoundEventBuilder addSounds(Function<ResourceLocation, SoundBuilder> builderFunction, ResourceLocation... locations) {
        for (ResourceLocation location : locations) {
            addSounds(builderFunction.apply(location));
        }

        return this;
    }

    public SoundEventBuilder addSounds(SoundBuilder... soundBuilders) {
        for (SoundBuilder soundBuilder : soundBuilders) {
            ResourceLocation location = soundBuilder.getLocation();

            if (this.soundBuilders.containsKey(location)) {
                throw new RuntimeException("Sound '" + location + "' has already been added to this sound event (" + getPath() + "). Increase the weight on the sound instead.");
            }

            this.soundBuilders.put(location, soundBuilder);
        }

        return this;
    }

    public JsonElement toJson() {
        JsonObject jsonObject = new JsonObject();

        if (replace) {
            jsonObject.addProperty(DataConstants.REPLACE, true);
        }

        if (translationKey != null) {
            jsonObject.addProperty(DataConstants.SUBTITLE, translationKey);
        }

        if (!soundBuilders.isEmpty()) {
            JsonArray sounds = new JsonArray();

            for (SoundBuilder soundBuilder : soundBuilders.values()) {
                sounds.add(soundBuilder.toJson());
            }

            jsonObject.add(DataConstants.SOUNDS, sounds);
        }

        return jsonObject;
    }
}
