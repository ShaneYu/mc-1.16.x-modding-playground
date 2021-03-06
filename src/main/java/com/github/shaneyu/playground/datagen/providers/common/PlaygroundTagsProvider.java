package com.github.shaneyu.playground.datagen.providers.common;

import com.github.shaneyu.playground.Playground;
import com.github.shaneyu.playground.common.registration.PlaygroundItems;
import com.github.shaneyu.playground.common.tag.PlaygroundTags;
import com.github.shaneyu.playground.lib.datagen.tag.BaseTagProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PlaygroundTagsProvider extends BaseTagProvider {
    public PlaygroundTagsProvider(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, Playground.MOD_ID, existingFileHelper);
    }

    @Nonnull
    @Override
    public String getName() {
        return Playground.MOD_NAME + " - Tags";
    }

    @Override
    protected void registerTags() {
        addWrenches();
    }

    private void addWrenches() {
        addToTag(PlaygroundTags.Items.WRENCHES, PlaygroundItems.WRENCH);
        getItemBuilder(PlaygroundTags.Items.TOOLS).add(PlaygroundTags.Items.TOOLS_WRENCH);
        addToTag(PlaygroundTags.Items.TOOLS_WRENCH, PlaygroundItems.WRENCH);
    }
}
