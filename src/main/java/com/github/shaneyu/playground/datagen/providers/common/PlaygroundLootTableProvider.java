package com.github.shaneyu.playground.datagen.providers.common;

import com.github.shaneyu.playground.Playground;
import com.github.shaneyu.playground.datagen.providers.common.loot.PlaygroundBlockLootTables;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.loot.*;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class PlaygroundLootTableProvider extends LootTableProvider {
    public PlaygroundLootTableProvider(DataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Nonnull
    @Override
    public String getName() {
        return Playground.MOD_NAME + " - Loot tables";
    }

    @Nonnull
    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables() {
        return ImmutableList.of(
                Pair.of(PlaygroundBlockLootTables::new, LootParameterSets.BLOCK)
        );
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, @Nonnull ValidationTracker validationtracker) {
        map.forEach((((resourceLocation, lootTable) -> LootTableManager.validateLootTable(validationtracker, resourceLocation, lootTable))));
    }
}
