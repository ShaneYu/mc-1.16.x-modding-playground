package com.github.shaneyu.playground.datagen.providers.common;

import com.github.shaneyu.playground.Playground;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class PlaygroundRecipeProvider extends RecipeProvider {
    public PlaygroundRecipeProvider(DataGenerator generator) {
        super(generator);
    }

    @Nonnull
    @Override
    public String getName() {
        return Playground.MOD_NAME + " - Recipes";
    }

    @Override
    protected void registerRecipes(@Nonnull Consumer<IFinishedRecipe> consumer) {
        // TODO: Register mod recipes here
    }
}
