package com.github.shaneyu.playground.datagen.providers.client;

import com.github.shaneyu.playground.Playground;
import com.github.shaneyu.playground.lib.provider.IItemProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nonnull;

public class PlaygroundItemModelProvider extends ItemModelProvider {
    public PlaygroundItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Playground.MOD_ID, existingFileHelper);
    }

    @Nonnull
    @Override
    public String getName() {
        return Playground.MOD_NAME + " - Item models";
    }

    @Override
    protected void registerModels() {
        // TODO: Register mod item models here
    }

    public boolean textureExists(ResourceLocation texture) {
        return existingFileHelper.exists(texture, ResourcePackType.CLIENT_RESOURCES, ".png", "textures");
    }

    private ResourceLocation itemTexture(IItemProvider itemProvider) {
        return modLoc("item/" + itemProvider.getName());
    }

    private ItemModelBuilder generated(IItemProvider itemProvider) {
        return generated(itemProvider, itemTexture(itemProvider));
    }

    private ItemModelBuilder generated(IItemProvider itemProvider, ResourceLocation texture) {
        return withExistingParent(itemProvider.getName(), "item/generated").texture("layer0", texture);
    }
}
