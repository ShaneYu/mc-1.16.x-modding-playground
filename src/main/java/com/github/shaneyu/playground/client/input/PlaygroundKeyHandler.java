package com.github.shaneyu.playground.client.input;

import com.github.shaneyu.playground.common.PlaygroundLang;
import com.github.shaneyu.playground.lib.client.input.KeyHandler;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;

public class PlaygroundKeyHandler extends KeyHandler {
    public static final KeyBinding detailsKey = new KeyBinding(PlaygroundLang.KEY_DETAILS_MODE.getTranslationKey(), KeyConflictContext.GUI, InputMappings.Type.KEYSYM,
            GLFW.GLFW_KEY_LEFT_SHIFT, PlaygroundLang.PLAYGROUND.getTranslationKey());

    public static final KeyBinding descriptionKey = new KeyBinding(PlaygroundLang.KEY_DESCRIPTION_MODE.getTranslationKey(), KeyConflictContext.GUI,
            KeyModifier.SHIFT, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_N, PlaygroundLang.PLAYGROUND.getTranslationKey());

    private static final Builder BINDINGS = new Builder(10)
            .addBinding(detailsKey, false)
            .addBinding(descriptionKey, false);

    public PlaygroundKeyHandler() {
        super(BINDINGS);

        ClientRegistry.registerKeyBinding(detailsKey);
        ClientRegistry.registerKeyBinding(descriptionKey);

        MinecraftForge.EVENT_BUS.addListener(this::onTick);
    }

    private void onTick(InputEvent.KeyInputEvent event) {
        keyTick();
    }

    @Override
    public void keyDown(KeyBinding kb, boolean isRepeat) {}

    @Override
    public void keyUp(KeyBinding kb) {}
}
