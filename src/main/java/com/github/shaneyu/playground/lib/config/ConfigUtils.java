package com.github.shaneyu.playground.lib.config;

import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.loading.FMLPaths;

import java.util.Arrays;

public final class ConfigUtils {
    private ConfigUtils() {}

    public static void createConfigDirectory(String modId) {
        FMLPaths.getOrCreateGameRelativePath(FMLPaths.CONFIGDIR.get().resolve(modId.toLowerCase()), modId.toLowerCase());
    }

    public static void registerConfigs(String modId, ModContainer modContainer, IConfig... configs) {
        Arrays.stream(configs).forEach(config -> registerConfig(modId, modContainer, config));
    }

    public static void registerConfig(String modId, ModContainer modContainer, IConfig config) {
        CustomModConfig modConfig = new CustomModConfig(modId, modContainer, config);

        if (config.addToContainer()) {
            modContainer.addConfig(modConfig);
        }
    }
}
