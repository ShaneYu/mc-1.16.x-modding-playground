package com.github.shaneyu.playground.lib.registration.registries;

import com.github.shaneyu.playground.lib.registration.WrappedDeferredRegister;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class SoundEventDeferredRegister extends WrappedDeferredRegister<SoundEvent> {
    private final String modId;

    public SoundEventDeferredRegister(String modId) {
        super(modId, ForgeRegistries.SOUND_EVENTS);
        this.modId = modId;
    }

    public SoundEventRegistryObject<SoundEvent> register(String name) {
        return register(name, () -> new SoundEvent(new ResourceLocation(modId, name)), SoundEventRegistryObject::new);
    }
}
