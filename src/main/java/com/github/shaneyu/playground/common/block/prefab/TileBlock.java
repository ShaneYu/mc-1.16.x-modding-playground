package com.github.shaneyu.playground.common.block.prefab;

import com.github.shaneyu.playground.common.block.attribute.AttributeStateActive;
import com.github.shaneyu.playground.common.block.interfaces.IHasTileEntity;
import com.github.shaneyu.playground.common.block.type.BlockTypeTile;
import com.github.shaneyu.playground.common.config.PlaygroundConfig;
import com.github.shaneyu.playground.common.tile.TileEntityBase;
import com.github.shaneyu.playground.common.tile.interfaces.IHasActiveState;
import com.github.shaneyu.playground.common.util.WorldUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class TileBlock<TILE extends TileEntityBase, TYPE extends BlockTypeTile<TILE>> extends TypeBlock<TYPE> implements IHasTileEntity<TILE> {

    public TileBlock(TYPE blockType) {
        this(blockType, Properties
                .create(Material.IRON)
                .hardnessAndResistance(3.5f, 16f)
                .setRequiresTool());
    }

    public TileBlock(TYPE blockType, Properties properties) {
        super(blockType, properties);
    }

    @Override
    public TileEntityType<TILE> getTileType() {
        return blockType.getTileType();
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        return Math.max(super.getLightValue(state, world, pos), getTileLight(state, world, pos));
    }

    protected int getTileLight(BlockState state, IBlockReader world, BlockPos pos) {
        if (PlaygroundConfig.client.enableAmbientLighting.get() && blockType.has(AttributeStateActive.class)) {
            TileEntity tile = WorldUtil.getTileEntity(world, pos);

            if (tile instanceof IHasActiveState && ((IHasActiveState) tile).lightUpdate() && ((IHasActiveState) tile).getActive()) {
                return ((IHasActiveState) tile).getActiveLightValue();
            }
        }

        return 0;
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public ActionResultType onBlockActivated(@Nonnull BlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull PlayerEntity player, @Nonnull Hand handIn, @Nonnull BlockRayTraceResult hit) {
        TileEntityBase tile = WorldUtil.getTileEntity(TileEntityBase.class, worldIn, pos);

        if (tile == null) {
            return ActionResultType.PASS;
        }

        if (worldIn.isRemote) {
            return genericClientActivated(player, handIn, hit);
        }

        // TODO: When we had a wrench to rotate blocks etc, this is where we should wire it in

        return ActionResultType.PASS;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(@Nonnull BlockState state, World worldIn, @Nonnull BlockPos pos, @Nonnull Block blockIn, @Nonnull BlockPos fromPos, boolean isMoving) {
        if (!worldIn.isRemote) {
            TileEntityBase tile = WorldUtil.getTileEntity(TileEntityBase.class, worldIn, pos);

            if (tile != null) {
                tile.onNeighborChange(blockIn, fromPos);
            }
        }
    }
}
