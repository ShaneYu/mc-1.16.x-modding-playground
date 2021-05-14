package com.github.shaneyu.playground.common.block.attribute;

import com.github.shaneyu.playground.lib.registration.registries.SoundEventRegistryObject;
import net.minecraft.util.SoundEvent;

public class AttributeSound implements Attribute {
    private final SoundEventRegistryObject<SoundEvent> soundRegistrar;

    public AttributeSound(SoundEventRegistryObject<SoundEvent> soundRegistrar) {
        this.soundRegistrar = soundRegistrar;
    }

    public SoundEvent getSoundEvent() {
        return soundRegistrar.get();
    }
}
