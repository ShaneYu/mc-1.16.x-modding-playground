package com.github.shaneyu.playground.common.config;

import com.github.shaneyu.playground.lib.config.IConfig;
import com.github.shaneyu.playground.lib.config.value.FloatValue;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.fml.config.ModConfig;

public class ClientConfig implements IConfig {
    private final ForgeConfigSpec configSpec;

    public final BooleanValue enableMachineSounds;
    public final FloatValue baseSoundVolume;
    public final BooleanValue machineEffects;
    public final BooleanValue enableAmbientLighting;
    public final IntValue ambientLightingLevel;
    public final BooleanValue allowModeScroll;

    ClientConfig() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.comment("Client config. This config only exists on the client.").push("client");

        enableMachineSounds = builder
                .comment("If enabled machines play their sounds while running.")
                .define("enableMachineSounds", true);

        baseSoundVolume = FloatValue.build(builder)
                .comment("Adjust Playground sounds' base volume. < 1 is softer, higher is louder.")
                .defineInRange("baseSoundVolume", 1F, 0F, Float.MAX_VALUE);

        machineEffects = builder
                .comment("Show particles when machines active.")
                .define("machineEffects", true);

        enableAmbientLighting = builder
                .comment("Should active machines produce block light.")
                .define("enableAmbientLighting", true);

        ambientLightingLevel = builder
                .comment("How much light to produce if ambient lighting is enabled.")
                .defineInRange("ambientLightingLevel", 15, 1, 15);

        allowModeScroll = builder
                .comment("Allow sneak + scroll to change item modes.")
                .define("allowModeScroll", true);

        builder.pop();
        configSpec = builder.build();
    }

    @Override
    public String getFileName() {
        return "client";
    }

    @Override
    public ForgeConfigSpec getConfigSpec() {
        return configSpec;
    }

    @Override
    public ModConfig.Type getConfigType() {
        return ModConfig.Type.CLIENT;
    }
}
