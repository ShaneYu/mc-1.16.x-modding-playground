package com.github.shaneyu.playground.common.tile.interfaces;

public interface ITileActive extends IHasActiveState {
    default boolean isActivatable() {
        return true;
    }

    @Override
    default boolean renderUpdate() {
        return false;
    }

    @Override
    default boolean lightUpdate() {
        return false;
    }
}
