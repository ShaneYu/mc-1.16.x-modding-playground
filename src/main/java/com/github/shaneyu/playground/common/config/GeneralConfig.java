package com.github.shaneyu.playground.common.config;

import com.github.shaneyu.playground.lib.config.IConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.fml.config.ModConfig;

public class GeneralConfig implements IConfig {
    private final ForgeConfigSpec configSpec;

    public final IntValue blockDeactivationDelay;

    GeneralConfig() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.comment("General config. This config is synced from server to client.").push("general");

        blockDeactivationDelay = builder
                .comment("How many ticks must pass until a block's active state is synced with the client, if it has been rapidly changing.")
                .defineInRange("blockDeactivationDelay", 60, 0, Integer.MAX_VALUE);

        builder.pop();
        configSpec = builder.build();
    }

    @Override
    public String getFileName() {
        return "general";
    }

    @Override
    public ForgeConfigSpec getConfigSpec() {
        return configSpec;
    }

    @Override
    public ModConfig.Type getConfigType() {
        return ModConfig.Type.SERVER;
    }
}
