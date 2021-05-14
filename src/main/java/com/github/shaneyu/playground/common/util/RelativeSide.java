package com.github.shaneyu.playground.common.util;

import com.github.shaneyu.playground.common.PlaygroundLang;
import com.github.shaneyu.playground.common.localization.ILanguageEntry;
import com.github.shaneyu.playground.lib.localization.IHasTranslationKey;
import net.minecraft.util.Direction;

public enum RelativeSide implements IHasTranslationKey {
    FRONT(PlaygroundLang.FRONT),
    LEFT(PlaygroundLang.LEFT),
    RIGHT(PlaygroundLang.RIGHT),
    BACK(PlaygroundLang.BACK),
    TOP(PlaygroundLang.TOP),
    BOTTOM(PlaygroundLang.BOTTOM);

    private static final RelativeSide[] SIDES = values();

    public static RelativeSide byIndex(int index) {
        return MathUtil.getByIndexMod(SIDES, index);
    }

    private final ILanguageEntry langEntry;

    RelativeSide(ILanguageEntry langEntry) {
        this.langEntry = langEntry;
    }

    @Override
    public String getTranslationKey() {
        return langEntry.getTranslationKey();
    }

    /**
     * Gets the {@link Direction} from the block based on what side it is facing.
     *
     * @param facing The direction the block is facing.
     *
     * @return The direction representing which side of the block this RelativeSide is actually representing based on the direction it is facing.
     */
    public Direction getDirection(Direction facing) {
        if (this == FRONT) {
            return facing;
        }

        if (this == BACK) {
            return facing.getOpposite();
        }

        if (this == LEFT) {
            if (facing == Direction.DOWN || facing == Direction.UP) {
                return Direction.EAST;
            }

            return facing.rotateY();
        }

        if (this == RIGHT) {
            if (facing == Direction.DOWN || facing == Direction.UP) {
                return Direction.WEST;
            }

            return facing.rotateYCCW();
        }

        if (this == TOP) {
            if (facing == Direction.DOWN) {
                return Direction.NORTH;
            }

            if (facing == Direction.UP) {
                return Direction.SOUTH;
            }

            return Direction.UP;
        }

        if (this == BOTTOM) {
            if (facing == Direction.DOWN) {
                return Direction.SOUTH;
            }

            if (facing == Direction.UP) {
                return Direction.NORTH;
            }

            return Direction.DOWN;
        }

        return Direction.NORTH;
    }

    /**
     * Gets the {@link RelativeSide} based on a side, and the facing direction of a block.
     *
     * @param facing The direction the block is facing.
     * @param side   The side of the block we want to know what {@link RelativeSide} it is.
     *
     * @return the {@link RelativeSide} based on a side, and the facing direction of a block.
     *
     * @apiNote The calculations for what side is what when facing upwards or downwards, is done as if it was facing NORTH and rotated around the X-axis
     */
    public static RelativeSide fromDirections(Direction facing, Direction side) {
        if (side == facing) {
            return FRONT;
        }

        if (side == facing.getOpposite()) {
            return BACK;
        }

        if (facing == Direction.DOWN || facing == Direction.UP) {
            if (side == Direction.NORTH) {
                return facing == Direction.DOWN ? TOP : BOTTOM;
            }

            if (side == Direction.SOUTH) {
                return facing == Direction.DOWN ? BOTTOM : TOP;
            }

            if (side == Direction.WEST) {
                return RIGHT;
            }

            if (side == Direction.EAST) {
                return LEFT;
            }
        }

        if (side == Direction.DOWN) {
            return BOTTOM;
        }

        if (side == Direction.UP) {
            return TOP;
        }

        if (side == facing.rotateYCCW()) {
            return RIGHT;
        }

        if (side == facing.rotateY()) {
            return LEFT;
        }

        return FRONT;
    }
}
