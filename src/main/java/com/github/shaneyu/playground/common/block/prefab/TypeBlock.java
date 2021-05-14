package com.github.shaneyu.playground.common.block.prefab;

import com.github.shaneyu.playground.common.block.BaseBlock;
import com.github.shaneyu.playground.common.block.interfaces.IHasBlockType;
import com.github.shaneyu.playground.common.block.interfaces.IHasDescription;
import com.github.shaneyu.playground.common.block.type.BlockType;
import com.github.shaneyu.playground.common.localization.ILanguageEntry;
import net.minecraft.block.material.Material;

public class TypeBlock<TYPE extends BlockType> extends BaseBlock implements IHasBlockType, IHasDescription {
    protected static BlockType blockTypeCache;
    protected final TYPE blockType;

    protected TypeBlock(TYPE blockType) {
        this(blockType, Properties
                .create(Material.IRON)
                .hardnessAndResistance(3.5f, 9f)
                .setRequiresTool());
    }

    protected TypeBlock(TYPE blockType, Properties properties) {
        super(applyPropertyChanges(blockType, properties));
        this.blockType = blockType;

        setDefaultState(stateContainer.getBaseState());
    }

    @Override
    public BlockType getBlockType() {
        return blockType == null ? blockTypeCache : blockType;
    }

    @Override
    public ILanguageEntry getDescription() {
        return blockType.getDescription();
    }

    private static <TYPE extends BlockType> Properties applyPropertyChanges(TYPE type, Properties props) {
        blockTypeCache = type;
        type.getAll().forEach(a -> a.applyPropertyChanges(props));

        return props;
    }
}
