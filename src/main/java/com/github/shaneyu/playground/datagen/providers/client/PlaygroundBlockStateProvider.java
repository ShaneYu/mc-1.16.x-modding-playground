package com.github.shaneyu.playground.datagen.providers.client;

import com.github.shaneyu.playground.Playground;
import com.github.shaneyu.playground.common.block.interfaces.IHasBlockType;
import com.github.shaneyu.playground.common.block.state.BlockStateProperties;
import com.github.shaneyu.playground.common.block.type.BlockType;
import com.github.shaneyu.playground.common.block.type.Generator;
import com.github.shaneyu.playground.common.registration.PlaygroundBlocks;
import com.github.shaneyu.playground.common.util.EnumUtil;
import com.github.shaneyu.playground.common.util.RelativeSide;
import com.github.shaneyu.playground.lib.provider.IBlockProvider;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class PlaygroundBlockStateProvider extends BlockStateProvider {
    private final PlaygroundBlockModelProvider blockModelProvider;

    public PlaygroundBlockStateProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Playground.MOD_ID, existingFileHelper);
        this.blockModelProvider = new PlaygroundBlockModelProvider(generator, existingFileHelper);
    }

    @Nonnull
    @Override
    public String getName() {
        return Playground.MOD_NAME + " - Block states";
    }

    @Override
    protected void registerStatesAndModels() {
        registerGenerators();
    }

    private void registerGenerators() {
        horizontalBlockWithActiveState(PlaygroundBlocks.SOLAR_GENERATOR);
    }

    @Override
    public PlaygroundBlockModelProvider models() {
        return blockModelProvider;
    }

    private void horizontalBlockWithActiveState(@Nonnull IBlockProvider blockProvider) {
        horizontalBlockWithActiveState(blockProvider, "_on");
    }

    private void horizontalBlockWithActiveState(@Nonnull IBlockProvider blockProvider, @Nonnull String activeStateSuffix) {
        Block block = blockProvider.getBlock();
        String blockName = "block/" + getBlockTypeSubDirectory(block) + blockProvider.getName();
        String itemName = "item/" + blockProvider.getName();
        ResourceLocation defaultTexture = modLoc(blockName);

        HashMap<Direction, ResourceLocation> defaultStateTextures = getDirectionalTexturesWithFallback(blockName, (direction) -> defaultTexture, null);
        HashMap<Direction, ResourceLocation> onTextures = getDirectionalTexturesWithFallback(blockName, defaultStateTextures::get, activeStateSuffix);

        ModelFile defaultStateModel = horizontalBlockCubeModel(blockName, defaultStateTextures);
        ModelFile onModelFile = horizontalBlockCubeModel(blockName + activeStateSuffix, onTextures);

        horizontalBlock(block, state -> state.get(BlockStateProperties.ACTIVE) ? onModelFile : defaultStateModel);

        models().withExistingParent(itemName, modLoc(blockName));
    }

    private HashMap<Direction, ResourceLocation> getDirectionalTexturesWithFallback(String basePath, Function<Direction, ResourceLocation> defaultTexture, @Nullable String stateSuffix) {
        return Arrays.stream(EnumUtil.DIRECTIONS).collect(HashMap::new, (textureMap, direction) -> {
            RelativeSide side = RelativeSide.fromDirections(Direction.NORTH, direction);
            ResourceLocation texture = modLoc(basePath + "_" + side.toString().toLowerCase() + (stateSuffix != null ? stateSuffix.toLowerCase() : ""));
            textureMap.put(direction, models().textureExists(texture) ? texture : defaultTexture.apply(direction));
        }, Map::putAll);
    }

    private ModelFile horizontalBlockCubeModel(@Nonnull String name, @Nonnull HashMap<Direction, ResourceLocation> textures) {
        return models().cube(name, textures.get(Direction.DOWN), textures.get(Direction.UP), textures.get(Direction.NORTH), textures.get(Direction.SOUTH), textures.get(Direction.EAST), textures.get(Direction.WEST));
    }

    private String getBlockTypeSubDirectory(Block block) {
        if (block instanceof IHasBlockType) {
            BlockType blockType = ((IHasBlockType) block).getBlockType();

            if (blockType instanceof Generator<?>) {
                return "generator/";
            }
        }

        return "";
    }
}
