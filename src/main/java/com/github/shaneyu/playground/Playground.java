package com.github.shaneyu.playground;

import com.github.shaneyu.playground.common.PlaygroundCreativeTab;
import com.github.shaneyu.playground.common.config.PlaygroundConfig;
import com.github.shaneyu.playground.common.network.PacketHandler;
import com.github.shaneyu.playground.common.registration.PlaygroundBlocks;
import com.github.shaneyu.playground.common.registration.PlaygroundItems;
import com.github.shaneyu.playground.common.registration.PlaygroundSounds;
import com.github.shaneyu.playground.common.registration.PlaygroundTileTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

@Mod(Playground.MOD_ID)
public class Playground
{
    public static final String MOD_ID = "playground";
    public static final String MOD_NAME = "Playground";

    public static Playground instance;
    public static final Logger logger = LogManager.getLogger(MOD_NAME);
    public static final PacketHandler packetHandler = new PacketHandler();
    public static final PlaygroundCreativeTab creativeTab = new PlaygroundCreativeTab();

    public Playground() {
        instance = this;
        PlaygroundConfig.registerConfigs(ModLoadingContext.get());

        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);
        PlaygroundBlocks.BLOCKS.register(modEventBus);
        PlaygroundItems.ITEMS.register(modEventBus);
        PlaygroundTileTypes.TILE_ENTITY_TYPES.register(modEventBus);
        PlaygroundSounds.SOUND_EVENTS.register(modEventBus);
    }

    public static String getVersion() {
        Optional<? extends ModContainer> modContainer = ModList.get().getModContainerById(MOD_ID);

        if (modContainer.isPresent()) {
            return modContainer.get().getModInfo().getVersion().toString();
        }

        return "NONE-dev";
    }

    public static boolean isDevBuild() {
        return getVersion().endsWith("-dev");
    }

    public static ResourceLocation modLoc(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Packet handler initialization
        packetHandler.initialize();

        // Initialization notification
        Playground.logger.info("Version {} initializing...", Playground.getVersion());

        // Completion notification
        Playground.logger.info("Loading complete.");
        Playground.logger.info("Mod loaded.");
    }
}
