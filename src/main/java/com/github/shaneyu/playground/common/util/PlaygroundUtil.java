package com.github.shaneyu.playground.common.util;

import com.github.shaneyu.playground.Playground;
import com.github.shaneyu.playground.common.integration.GenericWrench;
import com.github.shaneyu.playground.common.tag.PlaygroundTags;
import com.github.shaneyu.playground.lib.item.IWrench;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public final class PlaygroundUtil {
    private PlaygroundUtil() {}

    /**
     * Gets a ResourceLocation with a defined resource type and name.
     *
     * @param type - type of resource to retrieve
     * @param name - simple name of file to retrieve as a ResourceLocation
     *
     * @return the corresponding ResourceLocation
     */
    public static ResourceLocation getResourceLocation(ResourceType type, String name) {
        return Playground.modLoc(type.getPrefix() + name);
    }

    /**
     * Gets the direction to the left from the current orientation
     *
     * @param orientation current orientation to get left direction from
     *
     * @return the direction to the left from current orientation
     */
    public static Direction getLeft(Direction orientation) {
        return orientation.rotateY();
    }

    /**
     * Gets the direction to the right from the current orientation
     *
     * @param orientation current orientation to get right direction from
     *
     * @return the direction to the right from current orientation
     */
    public static Direction getRight(Direction orientation) {
        return orientation.rotateYCCW();
    }

    /**
     * Gets the wrench if the item is an IWrench, or a generic implementation if the item is in the forge wrenches tag
     */
    @Nullable
    public static IWrench getWrench(ItemStack it) {
        Item item = it.getItem();

        if (item instanceof IWrench) {
            return (IWrench) item;
        }

        if (item.isIn(PlaygroundTags.Items.WRENCHES)) {
            return GenericWrench.INSTANCE;
        }

        return null;
    }
}
