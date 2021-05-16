package com.github.shaneyu.playground.common.item;

import com.github.shaneyu.playground.common.PlaygroundLang;
import com.github.shaneyu.playground.common.block.attribute.Attribute;
import com.github.shaneyu.playground.common.block.attribute.AttributeStateFacing;
import com.github.shaneyu.playground.common.item.interfaces.IModeItem;
import com.github.shaneyu.playground.common.item.interfaces.IRadialModeItem;
import com.github.shaneyu.playground.common.item.interfaces.IRadialSelectorEnum;
import com.github.shaneyu.playground.common.localization.ILanguageEntry;
import com.github.shaneyu.playground.common.tile.TileEntityBase;
import com.github.shaneyu.playground.common.tile.WrenchResult;
import com.github.shaneyu.playground.common.util.*;
import com.github.shaneyu.playground.lib.item.IWrench;
import com.github.shaneyu.playground.lib.text.IHasTextComponent;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Rarity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class ItemWrench extends Item implements IWrench, IModeItem {
    public ItemWrench(Properties properties) {
        super(properties.rarity(Rarity.UNCOMMON));
    }

    @Nonnull
    @Override
    public ITextComponent getDisplayName(@Nonnull ItemStack stack) {
        return TextComponentUtil.build(NamedColor.AQUA, super.getDisplayName(stack));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(@Nonnull ItemStack stack, World world, @Nonnull List<ITextComponent> tooltip, @Nonnull ITooltipFlag flag) {
        super.addInformation(stack, world, tooltip, flag);
        tooltip.add(PlaygroundLang.WRENCH_STATE.translateColored(NamedColor.PINK, getMode(stack)));
    }

    @Nonnull
    @Override
    public ActionResultType onItemUse(@Nonnull ItemUseContext context) {
        PlayerEntity player = context.getPlayer();
        World world = context.getWorld();

        if (!world.isRemote() && player != null) {
            BlockPos pos = context.getPos();
            Direction side = context.getFace();
            ItemStack stack = context.getItem();
            WrenchMode mode = getMode(stack);
            TileEntity tile = WorldUtil.getTileEntity(world, pos);

            if (tile instanceof TileEntityBase) {
                TileEntityBase tileEntityBase = (TileEntityBase) tile;

                if (mode == WrenchMode.ROTATE) {
                    if (Attribute.get(tileEntityBase.getBlockType(), AttributeStateFacing.class).canRotate()) {
                        if (!player.isSneaking()) {
                            tileEntityBase.setFacing(side);
                        } else if (player.isSneaking()) {
                            tileEntityBase.setFacing(side.getOpposite());
                        }
                    }

                    return ActionResultType.SUCCESS;
                }
            }

            if (mode == WrenchMode.DISMANTLE) {
                return ActionResultType.PASS;
            }
        }

        return ActionResultType.PASS;
    }

    @Override
    public boolean canUseWrench(ItemStack stack, PlayerEntity player, BlockPos pos) {
        return getMode(stack) == WrenchMode.DISMANTLE;
    }

    @Override
    public boolean doesSneakBypassUse(ItemStack stack, IWorldReader world, BlockPos pos, PlayerEntity player) {
        return getMode(stack) == WrenchMode.DISMANTLE;
    }

    @Override
    public void changeMode(@Nonnull PlayerEntity player, @Nonnull ItemStack stack, int shift, boolean displayChangeMessage) {
        WrenchMode mode = getMode(stack);
        WrenchMode newMode = mode.adjust(shift);

        if (mode != newMode) {
            setMode(stack, player, newMode);

            if (displayChangeMessage) {
                player.sendMessage(PlaygroundLang.LOG_FORMAT.translateColored(NamedColor.DARK_BLUE, PlaygroundLang.PLAYGROUND, NamedColor.GRAY,
                        PlaygroundLang.WRENCH_CONFIGURE_STATE.translate(newMode)), Util.DUMMY_UUID);
            }
        }
    }

    @Nonnull
    @Override
    public ITextComponent getScrollTextComponent(@Nonnull ItemStack stack) {
        return getMode(stack).getTextComponent();
    }

    public void setMode(ItemStack stack, PlayerEntity player, WrenchMode mode) {
        ItemDataUtil.setInt(stack, NBTConstants.STATE, mode.ordinal());
    }

    public WrenchMode getMode(ItemStack stack) {
        return WrenchMode.byIndexStatic(ItemDataUtil.getInt(stack, NBTConstants.STATE));
    }

    @ParametersAreNonnullByDefault
    @MethodsReturnNonnullByDefault
    public enum WrenchMode implements IRadialSelectorEnum<WrenchMode>, IHasTextComponent {
        DISMANTLE(PlaygroundLang.WRENCH_MODE_DISMANTLE, NamedColor.DARK_RED),
        ROTATE(PlaygroundLang.WRENCH_MODE_ROTATE, NamedColor.DARK_GREEN);

        public static final WrenchMode[] MODES = values();
        private final ILanguageEntry langEntry;
        private final NamedColor color;

        WrenchMode(ILanguageEntry langEntry, NamedColor color) {
            this.langEntry = langEntry;
            this.color = color;
        }

        @Override
        public ITextComponent getTextComponent() {
            return langEntry.translateColored(color);
        }

        @Override
        public NamedColor getColor() {
            return color;
        }

        @Nonnull
        @Override
        public WrenchMode byIndex(int index) {
            return byIndexStatic(index);
        }

        public static WrenchMode byIndexStatic(int index) {
            return MathUtil.getByIndexMod(MODES, index);
        }
    }
}
