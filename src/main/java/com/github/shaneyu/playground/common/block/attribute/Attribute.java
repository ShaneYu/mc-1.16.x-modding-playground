package com.github.shaneyu.playground.common.block.attribute;

import com.github.shaneyu.playground.common.block.interfaces.IHasBlockType;
import com.github.shaneyu.playground.common.tile.TileEntityBase;
import com.google.common.collect.Lists;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.function.Consumer;

public interface Attribute {
    interface TileAttribute<TILE extends TileEntityBase> extends Attribute {}

    default void applyPropertyChanges(AbstractBlock.Properties props) {}

    static boolean has(Block block, Class<? extends Attribute> type) {
        return block instanceof IHasBlockType && ((IHasBlockType) block).getBlockType().has(type);
    }

    @Nullable
    static <T extends Attribute> T get(Block block, Class<T> type) {
        return block instanceof IHasBlockType ? ((IHasBlockType) block).getBlockType().get(type) : null;
    }

    static boolean has(Block block1, Block block2, Class<? extends Attribute> type) {
        return has(block1, type) && has(block2, type);
    }

    static Collection<Attribute> getAll(Block block) {
        return block instanceof IHasBlockType ? ((IHasBlockType) block).getBlockType().getAll() : Lists.newArrayList();
    }

    static <T extends Attribute> void ifHas(Block block, Class<T> type, Consumer<T> run) {
        if (block instanceof IHasBlockType) {
            T attribute = ((IHasBlockType) block).getBlockType().get(type);
            if (attribute != null) {
                run.accept(attribute);
            }
        }
    }

    @Nullable
    static Direction getFacing(BlockState state) {
        AttributeStateFacing attr = get(state.getBlock(), AttributeStateFacing.class);

        return attr == null ? null : attr.getDirection(state);
    }

    @Nullable
    static BlockState setFacing(BlockState state, Direction facing) {
        AttributeStateFacing attr = get(state.getBlock(), AttributeStateFacing.class);

        return attr == null ? null : attr.setDirection(state, facing);
    }

    static boolean isActive(BlockState state) {
        AttributeStateActive attr = get(state.getBlock(), AttributeStateActive.class);

        return attr != null && attr.isActive(state);
    }

    static BlockState setActive(BlockState state, boolean active) {
        AttributeStateActive attr = get(state.getBlock(), AttributeStateActive.class);

        return attr == null ? state : attr.setActive(state, active);
    }
}
