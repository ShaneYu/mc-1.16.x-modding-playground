package com.github.shaneyu.playground.common.util;

public final class MathUtil {
    private MathUtil() {}

    public static <TYPE> TYPE getByIndexMod(TYPE[] elements, int index) {
        if (index < 0) {
            return elements[Math.floorMod(index, elements.length)];
        }

        return elements[index % elements.length];
    }
}
