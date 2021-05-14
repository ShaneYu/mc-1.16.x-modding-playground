package com.github.shaneyu.playground.common.item;

import com.github.shaneyu.playground.client.input.PlaygroundKeyHandler;
import com.github.shaneyu.playground.common.PlaygroundLang;
import com.github.shaneyu.playground.common.block.interfaces.IHasDescription;
import com.github.shaneyu.playground.common.util.NamedColor;
import com.github.shaneyu.playground.lib.client.input.KeyHandler;
import com.github.shaneyu.playground.lib.item.BlockItem;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.List;

public class BlockItemTooltip<BLOCK extends Block & IHasDescription> extends BlockItem<BLOCK> {
    private final boolean hasDetails;

    public BlockItemTooltip(BLOCK block, Properties properties) {
        this(block, false, properties);
    }

    public BlockItemTooltip(BLOCK block, boolean hasDetails, Properties properties) {
        super(block, properties);
        this.hasDetails = hasDetails;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(@Nonnull ItemStack stack, World world, @Nonnull List<ITextComponent> tooltip, @Nonnull ITooltipFlag flag) {
        if (KeyHandler.getIsKeyPressed(PlaygroundKeyHandler.descriptionKey)) {
            tooltip.add(getBlock().getDescription().translate());
        } else if (hasDetails && KeyHandler.getIsKeyPressed(PlaygroundKeyHandler.detailsKey)) {
            addDetails(stack, world, tooltip, flag.isAdvanced());
        } else {
            addStats(stack, world, tooltip, flag.isAdvanced());

            if (hasDetails) {
                tooltip.add(PlaygroundLang.HOLD_FOR_DETAILS.translateColored(NamedColor.GRAY, NamedColor.INDIGO, PlaygroundKeyHandler.detailsKey.func_238171_j_()));
            }

            tooltip.add(PlaygroundLang.HOLD_FOR_DESCRIPTION.translateColored(NamedColor.GRAY, NamedColor.AQUA, PlaygroundKeyHandler.descriptionKey.func_238171_j_()));
        }
    }

    public void addStats(ItemStack stack, World world, List<ITextComponent> tooltip, boolean advanced) {}

    public void addDetails(ItemStack stack, World world, List<ITextComponent> tooltip, boolean advanced) {}
}
