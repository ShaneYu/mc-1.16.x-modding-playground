package com.github.shaneyu.playground.common.tile.interfaces;

import com.github.shaneyu.playground.common.util.PlaygroundUtil;
import net.minecraft.util.Direction;

public interface ITileDirectional {
    default boolean isDirectional() {
        return true;
    }

    void setFacing(Direction direction);

    Direction getDirection();

    default Direction getOppositeDirection() {
        return getDirection().getOpposite();
    }

    default Direction getRightSide() {
        return PlaygroundUtil.getRight(getDirection());
    }

    default Direction getLeftSide() {
        return PlaygroundUtil.getLeft(getDirection());
    }
}
