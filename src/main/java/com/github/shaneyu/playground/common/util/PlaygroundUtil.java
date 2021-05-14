package com.github.shaneyu.playground.common.util;

import com.github.shaneyu.playground.Playground;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;

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
}
