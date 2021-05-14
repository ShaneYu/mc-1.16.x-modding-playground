package com.github.shaneyu.playground.datagen.providers.client;

import com.github.shaneyu.playground.Playground;
import com.github.shaneyu.playground.common.registration.PlaygroundSounds;
import com.github.shaneyu.playground.lib.datagen.sound.BaseSoundProvider;
import com.github.shaneyu.playground.lib.datagen.sound.SoundEventBuilder;
import com.github.shaneyu.playground.lib.registration.registries.SoundEventRegistryObject;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nonnull;

public class PlaygroundSoundProvider extends BaseSoundProvider {
    public PlaygroundSoundProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Playground.MOD_ID, existingFileHelper);
    }

    @Nonnull
    @Override
    public String getName() {
        return Playground.MOD_NAME + " - Sound events";
    }

    @Override
    protected void addSoundEvents() {
        addGeneratorSoundEvents();
    }

    private void addGeneratorSoundEvents() {
        String basePath = "generator/";

        // Use a reduced attenuation range for passive generators
        addSoundEventWithSubtitle(PlaygroundSounds.SOLAR_GENERATOR, Playground.modLoc(basePath + "solar"), 8);
    }

    private void addSoundEventWithSubtitle(SoundEventRegistryObject<?> soundEventRO, ResourceLocation location, int attenuationDistance) {
        addSoundEvent(SoundEventBuilder.create(soundEventRO).subtitle(soundEventRO).addSounds(createSoundBuilder(location).attenuationDistance(attenuationDistance)));
    }
}
