package com.github.shaneyu.playground.datagen.providers.client;

import com.github.shaneyu.playground.Playground;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nonnull;

public class PlaygroundBlockModelProvider extends BlockModelProvider {
    public PlaygroundBlockModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Playground.MOD_ID, existingFileHelper);
    }

    @Nonnull
    @Override
    public String getName() {
        return Playground.MOD_NAME + " - Block models";
    }

    @Override
    protected void registerModels() {
        // TODO: Register mod block models here
    }

    public boolean textureExists(ResourceLocation texture) {
        return existingFileHelper.exists(texture, ResourcePackType.CLIENT_RESOURCES, ".png", "textures");
    }
}
