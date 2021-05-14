package com.github.shaneyu.playground.common.registration;

import com.github.shaneyu.playground.Playground;
import com.github.shaneyu.playground.lib.registration.registries.SoundEventDeferredRegister;
import com.github.shaneyu.playground.lib.registration.registries.SoundEventRegistryObject;
import net.minecraft.util.SoundEvent;

public final class PlaygroundSounds {
    private PlaygroundSounds() {}

    public static final SoundEventDeferredRegister SOUND_EVENTS = new SoundEventDeferredRegister(Playground.MOD_ID);

    public static final SoundEventRegistryObject<SoundEvent> SOLAR_GENERATOR = SOUND_EVENTS.register("tile.generator.solar");
}
