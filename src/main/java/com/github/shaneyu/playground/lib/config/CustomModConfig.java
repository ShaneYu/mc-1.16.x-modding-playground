package com.github.shaneyu.playground.lib.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.config.ConfigFileTypeHandler;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;
import java.util.function.Function;

public class CustomModConfig extends ModConfig {
    private static final CustomConfigFileTypeHandler FILE_TYPE_HANDLER = new CustomConfigFileTypeHandler();

    public CustomModConfig(String modId, ModContainer container, IConfig config) {
        super(config.getConfigType(), config.getConfigSpec(), container, modId.toLowerCase() + "/" + config.getFileName() + ".toml");
    }

    @Override
    public ConfigFileTypeHandler getHandler() {
        return FILE_TYPE_HANDLER;
    }

    private static class CustomConfigFileTypeHandler extends ConfigFileTypeHandler {
        private static Path getPath(Path configBasePath) {
            // Intercept server config path reading and reroute it to the normal config directory
            if (configBasePath.endsWith("serverconfig")) {
                return FMLPaths.CONFIGDIR.get();
            }

            return configBasePath;
        }

        @Override
        public Function<ModConfig, CommentedFileConfig> reader(Path configBasePath) {
            return super.reader(getPath(configBasePath));
        }

        @Override
        public void unload(Path configBasePath, ModConfig config) {
            super.unload(getPath(configBasePath), config);
        }
    }
}
