package com.github.shaneyu.playground.common.block;

import com.github.shaneyu.playground.common.block.attribute.AttributeStateFacing;
import com.github.shaneyu.playground.common.block.interfaces.IHasTileEntity;
import com.github.shaneyu.playground.common.block.state.BlockStateHelper;
import com.github.shaneyu.playground.common.tile.TileEntityBase;
import com.github.shaneyu.playground.common.util.WorldUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class BaseBlock extends Block {

    protected BaseBlock(Properties properties) {
        super(properties);
        setDefaultState(BlockStateHelper.getDefaultState(stateContainer.getBaseState()));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return this instanceof IHasTileEntity;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        if (this instanceof IHasTileEntity) {
            return ((IHasTileEntity<?>) this).getTileType().create();
        }

        return null;
    }

    @Override
    protected void fillStateContainer(@Nonnull StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        BlockStateHelper.fillBlockStateContainer(this, builder);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@Nonnull BlockItemUseContext context) {
        return BlockStateHelper.getStateForPlacement(this, super.getStateForPlacement(context), context);
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    @Deprecated
    public PushReaction getPushReaction(@Nonnull BlockState state) {
        if (hasTileEntity(state)) {
            // Protect against mods like Quark that allow blocks with tile entities to be moved
            return PushReaction.BLOCK;
        }

        return super.getPushReaction(state);
    }

    @Override
    public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation rotation) {
        return AttributeStateFacing.rotate(state, world, pos, rotation);
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    @Deprecated
    public BlockState rotate(@Nonnull BlockState state, @Nonnull Rotation rotation) {
        return AttributeStateFacing.rotate(state, rotation);
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    @Deprecated
    public BlockState mirror(@Nonnull BlockState state, @Nonnull Mirror mirror) {
        return AttributeStateFacing.mirror(state, mirror);
    }

    @Override
    public void onBlockPlacedBy(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nullable LivingEntity placer, @Nonnull ItemStack stack) {
        TileEntityBase tile = WorldUtil.getTileEntity(TileEntityBase.class, worldIn, pos);

        if (tile == null) {
            return;
        }

        tile.onPlaced();
        setTileData(worldIn, pos, state, placer, stack, tile);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onBlockAdded(@Nonnull BlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull BlockState oldState, boolean isMoving) {
        if (state.hasTileEntity() && oldState.getBlock() != state.getBlock()) {
            TileEntityBase tile = WorldUtil.getTileEntity(TileEntityBase.class, worldIn, pos);

            if (tile != null) {
                tile.onAdded();
            }
        }

        super.onBlockAdded(state, worldIn, pos, oldState, isMoving);
    }

    protected ActionResultType genericClientActivated(PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        ItemStack stack = player.getHeldItem(hand);

        if (stack.getItem() instanceof BlockItem && new BlockItemUseContext(player, hand, stack, hit).canPlace()) {
            return ActionResultType.PASS;
        }

        return ActionResultType.SUCCESS;
    }

    /* Method to override for setting some simple tile specific stuff */
    public void setTileData(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack, TileEntityBase tile) {}
}
