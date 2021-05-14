package com.github.shaneyu.playground.common.tile.interfaces;

import com.github.shaneyu.playground.common.config.PlaygroundConfig;

public interface IHasActiveState {
    boolean getActive();

    void setActive(boolean active);

    /** Whether or not this block has a visual effect when it is on it's active state. Used for rendering */
    default boolean renderUpdate() {
        return false;
    }

    /** Whether or not this block should use a different light value when it is on it's active state. */
    default boolean lightUpdate() {
        return false;
    }

    default int getActiveLightValue() {
        return PlaygroundConfig.client.ambientLightingLevel.get();
    }
}
