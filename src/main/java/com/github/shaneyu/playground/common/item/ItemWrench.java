package com.github.shaneyu.playground.common.item;

import com.github.shaneyu.playground.common.block.attribute.Attribute;
import com.github.shaneyu.playground.common.block.attribute.AttributeStateFacing;
import com.github.shaneyu.playground.common.tile.TileEntityBase;
import com.github.shaneyu.playground.common.util.NamedColor;
import com.github.shaneyu.playground.common.util.TextComponentUtil;
import com.github.shaneyu.playground.common.util.WorldUtil;
import com.github.shaneyu.playground.lib.item.IWrench;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Rarity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ItemWrench extends Item implements IWrench {
    public ItemWrench(Properties properties) {
        super(properties.rarity(Rarity.UNCOMMON));
    }

    @Nonnull
    @Override
    public ITextComponent getDisplayName(@Nonnull ItemStack stack) {
        return TextComponentUtil.build(NamedColor.AQUA, super.getDisplayName(stack));
    }

    @Nonnull
    @Override
    public ActionResultType onItemUse(@Nonnull ItemUseContext context) {
        PlayerEntity player = context.getPlayer();
        World world = context.getWorld();

        if (!world.isRemote() && player != null) {
            BlockPos pos = context.getPos();
            Direction side = context.getFace();
            TileEntity tile = WorldUtil.getTileEntity(world, pos);

            if (tile instanceof TileEntityBase) {
                TileEntityBase tileEntityBase = (TileEntityBase) tile;

                if (Attribute.get(tileEntityBase.getBlockType(), AttributeStateFacing.class).canRotate()) {
                    if (!player.isSneaking()) {
                        tileEntityBase.setFacing(side);
                    } else if (player.isSneaking()) {
                        tileEntityBase.setFacing(side.getOpposite());
                    }
                }
            }
        }

        return ActionResultType.SUCCESS;
    }

    @Override
    public boolean canUseWrench(ItemStack stack, PlayerEntity player, BlockPos pos) {
        return true;
    }
}
