package com.github.shaneyu.playground.lib.config.value;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class FloatValue implements Supplier<Float> {
    private final DoubleValue doubleValue;

    private FloatValue(DoubleValue doubleValue) {
        this.doubleValue = doubleValue;
    }

    public static FloatValue wrap(DoubleValue doubleValue) {
        return new FloatValue(doubleValue);
    }

    @Override
    public Float get() {
        double val = doubleValue.get();

        if (val > Float.MAX_VALUE) {
            return Float.MAX_VALUE;
        }

        if (val < -Float.MAX_VALUE) {
            // Note: Float.MIN_VALUE is the smallest positive value a float can represent
            // the smallest value a float can represent overall is -Float.MAX_VALUE
            return -Float.MAX_VALUE;
        }

        return (float) val;
    }

    public static FloatValueBuilder build(ForgeConfigSpec.Builder builder) {
        return new FloatValueBuilder(builder);
    }

    public static class FloatValueBuilder {
        private final ForgeConfigSpec.Builder builder;
        @Nullable
        private String comment;

        public FloatValueBuilder(ForgeConfigSpec.Builder builder) {
            this.builder = builder;
        }

        public FloatValueBuilder comment(String comment) {
            this.comment = comment;
            return this;
        }

        public FloatValue defineInRange(String path, Float defaultValue, Float minValue, Float maxValue) {
            if (comment != null) {
                return FloatValue.wrap(builder.comment(comment).defineInRange(path, defaultValue, minValue.doubleValue(), maxValue.doubleValue()));
            }

            return FloatValue.wrap(builder.defineInRange(path, defaultValue, minValue.doubleValue(), maxValue.doubleValue()));
        }
    }
}
