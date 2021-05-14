package com.github.shaneyu.playground.lib.datagen.tag;

import net.minecraft.tags.ITag;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class ForgeRegistryTagBuilder<TYPE extends IForgeRegistryEntry<TYPE>> {
    private final ITag.Builder builder;
    private final String modId;

    public ForgeRegistryTagBuilder(String modId, ITag.Builder builder) {
        this.builder = builder;
        this.modId = modId;
    }

    public ForgeRegistryTagBuilder<TYPE> add(TYPE element) {
        this.builder.addItemEntry(element.getRegistryName(), modId);
        return this;
    }

    @SafeVarargs
    public final ForgeRegistryTagBuilder<TYPE> add(TYPE... elements) {
        for (TYPE element : elements) {
            add(element);
        }
        return this;
    }

    public ForgeRegistryTagBuilder<TYPE> add(ITag.INamedTag<TYPE> tag) {
        this.builder.addTagEntry(tag.getName(), modId);
        return this;
    }

    @SafeVarargs
    public final ForgeRegistryTagBuilder<TYPE> add(ITag.INamedTag<TYPE>... tags) {
        for (ITag.INamedTag<TYPE> tag : tags) {
            add(tag);
        }
        return this;
    }

    public ForgeRegistryTagBuilder<TYPE> add(ITag.ITagEntry tag) {
        builder.addTag(tag, modId);
        return this;
    }

    @SafeVarargs
    public final ForgeRegistryTagBuilder<TYPE> add(RegistryKey<TYPE>... keys) {
        for (RegistryKey<TYPE> key : keys) {
            builder.addItemEntry(key.getLocation(), modId);
        }
        return this;
    }

    public ForgeRegistryTagBuilder<TYPE> replace() {
        return replace(true);
    }

    public ForgeRegistryTagBuilder<TYPE> replace(boolean value) {
        builder.replace(value);
        return this;
    }

    public ForgeRegistryTagBuilder<TYPE> addOptional(ResourceLocation... locations) {
        for (ResourceLocation location : locations) {
            add(new ITag.OptionalItemEntry(location));
        }
        return this;
    }

    public ForgeRegistryTagBuilder<TYPE> addOptionalTag(ResourceLocation... locations) {
        for (ResourceLocation location : locations) {
            add(new ITag.OptionalTagEntry(location));
        }
        return this;
    }
}
