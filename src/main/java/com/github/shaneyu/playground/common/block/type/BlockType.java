package com.github.shaneyu.playground.common.block.type;

import com.github.shaneyu.playground.common.block.attribute.Attribute;
import com.github.shaneyu.playground.common.block.attribute.AttributeLight;
import com.github.shaneyu.playground.common.block.interfaces.IHasBlockType;
import com.github.shaneyu.playground.common.localization.ILanguageEntry;
import net.minecraft.block.Block;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BlockType {
    private final ILanguageEntry description;
    private final Map<Class<? extends Attribute>, Attribute> attributeMap = new HashMap<>();

    public BlockType(ILanguageEntry description) {
        this.description = description;
    }

    public ILanguageEntry getDescription() {
        return description;
    }

    public static boolean is(Block block, BlockType... types) {
        if (block instanceof IHasBlockType) {
            for (BlockType type : types) {
                if (((IHasBlockType) block).getBlockType() == type) {
                    return true;
                }
            }
        }

        return false;
    }

    @Nullable
    public static BlockType get(Block block) {
        return block instanceof IHasBlockType ? ((IHasBlockType) block).getBlockType() : null;
    }

    public boolean has(Class<? extends Attribute> type) {
        return attributeMap.containsKey(type);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public <T extends Attribute> T get(Class<T> type) {
        return (T) attributeMap.get(type);
    }

    @SafeVarargs
    protected final void setFrom(BlockTypeTile<?> tile, Class<? extends Attribute>... types) {
        for (Class<? extends Attribute> type : types) {
            attributeMap.put(type, tile.get(type));
        }
    }

    public void add(Attribute... attrs) {
        for (Attribute attr : attrs) {
            attributeMap.put(attr.getClass(), attr);
        }
    }

    @SafeVarargs
    public final void remove(Class<? extends Attribute>... attrs) {
        for (Class<? extends Attribute> attr : attrs) {
            attributeMap.remove(attr);
        }
    }

    public Collection<Attribute> getAll() {
        return attributeMap.values();
    }

    public static class BlockTypeBuilder<BLOCK extends BlockType, T extends BlockTypeBuilder<BLOCK, T>> {
        protected final BLOCK blockType;

        protected BlockTypeBuilder(BLOCK blockType) {
            this.blockType = blockType;
        }

        public static BlockTypeBuilder<BlockType, ?> createBlock(ILanguageEntry description) {
            return new BlockTypeBuilder<>(new BlockType(description));
        }

        @SuppressWarnings("unchecked")
        public T getThis() {
            return (T) this;
        }

        public final T with(Attribute... attrs) {
            blockType.add(attrs);
            return getThis();
        }

        @SafeVarargs
        public final T without(Class<? extends Attribute>... attrs) {
            blockType.remove(attrs);
            return getThis();
        }

        public T withLight(int light) {
            return with(new AttributeLight(light));
        }

        public BLOCK build() {
            return blockType;
        }
    }
}
