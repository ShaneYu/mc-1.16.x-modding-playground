package com.github.shaneyu.playground.common.config;

import com.github.shaneyu.playground.Playground;
import com.github.shaneyu.playground.lib.config.ConfigUtils;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;

public final class PlaygroundConfig {
    private PlaygroundConfig() {}

    public static final ClientConfig client = new ClientConfig();
    public static final GeneralConfig general = new GeneralConfig();

    static {
        ConfigUtils.createConfigDirectory(Playground.MOD_ID);
    }

    public static void registerConfigs(ModLoadingContext modLoadingContext) {
        ModContainer modContainer = modLoadingContext.getActiveContainer();
        ConfigUtils.registerConfigs(Playground.MOD_ID, modContainer, client, general);
    }
}
