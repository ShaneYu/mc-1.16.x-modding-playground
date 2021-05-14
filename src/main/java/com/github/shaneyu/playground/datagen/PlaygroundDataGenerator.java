package com.github.shaneyu.playground.datagen;

import com.github.shaneyu.playground.Playground;
import com.github.shaneyu.playground.datagen.providers.client.PlaygroundBlockStateProvider;
import com.github.shaneyu.playground.datagen.providers.client.PlaygroundItemModelProvider;
import com.github.shaneyu.playground.datagen.providers.client.PlaygroundLanguageProvider;
import com.github.shaneyu.playground.datagen.providers.client.PlaygroundSoundProvider;
import com.github.shaneyu.playground.datagen.providers.common.PlaygroundLootTableProvider;
import com.github.shaneyu.playground.datagen.providers.common.PlaygroundRecipeProvider;
import com.github.shaneyu.playground.datagen.providers.common.PlaygroundTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = Playground.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PlaygroundDataGenerator {
    private PlaygroundDataGenerator() {}

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        if (event.includeClient()) {
            gatherClientData(gen, existingFileHelper);
        }

        if (event.includeServer()) {
            gatherServerData(gen, existingFileHelper);
        }
    }

    private static void gatherClientData(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        generator.addProvider(new PlaygroundLanguageProvider(generator));
        generator.addProvider(new PlaygroundSoundProvider(generator, existingFileHelper));
        PlaygroundItemModelProvider itemModelProvider = new PlaygroundItemModelProvider(generator, existingFileHelper);
        generator.addProvider(itemModelProvider);
        generator.addProvider(new PlaygroundBlockStateProvider(generator, itemModelProvider.existingFileHelper));
    }

    private static void gatherServerData(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        generator.addProvider(new PlaygroundTagsProvider(generator, existingFileHelper));
        generator.addProvider(new PlaygroundLootTableProvider(generator));
        generator.addProvider(new PlaygroundRecipeProvider(generator));
    }
}

