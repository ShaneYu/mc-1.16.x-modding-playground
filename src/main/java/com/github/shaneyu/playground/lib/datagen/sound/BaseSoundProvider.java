package com.github.shaneyu.playground.lib.datagen.sound;

import com.github.shaneyu.playground.lib.localization.IHasTranslationKey;
import com.github.shaneyu.playground.lib.registration.registries.SoundEventRegistryObject;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class BaseSoundProvider implements IDataProvider {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final Map<String, SoundEventBuilder> soundEventBuilders = new LinkedHashMap<>();
    private final ExistingFileHelper existingFileHelper;
    private final DataGenerator gen;
    private final String modId;

    protected BaseSoundProvider(DataGenerator gen, String modId, ExistingFileHelper existingFileHelper) {
        this.gen = gen;
        this.existingFileHelper = existingFileHelper;
        this.modId = modId;
    }

    protected abstract void addSoundEvents();

    protected SoundBuilder createSoundBuilder(ResourceLocation location) {
        Preconditions.checkArgument(existingFileHelper.exists(location, ResourcePackType.CLIENT_RESOURCES, ".ogg", "sounds"),
                "Sound %s does not exist in any known resource pack", location);

        return new SoundBuilder(location);
    }

    protected void addSoundEvent(SoundEventBuilder soundEventBuilder) {
        String path = soundEventBuilder.getPath();

        if (soundEventBuilders.containsKey(path)) {
            throw new RuntimeException("Sound event '" + path + "' has already been added.");
        }

        soundEventBuilders.put(path, soundEventBuilder);
    }

    protected void addSoundEvent(SoundEventRegistryObject<?> soundEventRO, ResourceLocation location) {
        addSoundEvent(SoundEventBuilder.create(soundEventRO).addSounds(createSoundBuilder(location)));
    }

    protected void addSoundEventWithSubtitle(SoundEventRegistryObject<?> soundEventRO, ResourceLocation location) {
        addSoundEvent(SoundEventBuilder.create(soundEventRO).subtitle(soundEventRO).addSounds(createSoundBuilder(location)));
    }

    protected void addSoundEvent(SoundEventRegistryObject<?> soundEventRO, ResourceLocation location, IHasTranslationKey subtitle) {
        addSoundEvent(SoundEventBuilder.create(soundEventRO).subtitle(subtitle).addSounds(createSoundBuilder(location)));
    }

    @Override
    public void act(DirectoryCache cache) {
        soundEventBuilders.clear();
        addSoundEvents();

        if (!soundEventBuilders.isEmpty()) {
            JsonObject jsonObject = new JsonObject();

            for (Map.Entry<String, SoundEventBuilder> entry : soundEventBuilders.entrySet()) {
                jsonObject.add(entry.getKey(), entry.getValue().toJson());
            }

            try {
                IDataProvider.save(GSON, cache, jsonObject, gen.getOutputFolder().resolve("assets/" + modId + "/sounds.json"));
            } catch (IOException e) {
                throw new RuntimeException("Couldn't save sounds.json for mod: " + modId, e);
            }
        }
    }
}