package com.github.shaneyu.playground.lib.registration.registries;

import com.github.shaneyu.playground.lib.localization.IHasTranslationKey;
import com.github.shaneyu.playground.lib.registration.WrappedRegistryObject;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.Util;
import net.minecraftforge.fml.RegistryObject;

public class SoundEventRegistryObject<SOUND extends SoundEvent> extends WrappedRegistryObject<SOUND> implements IHasTranslationKey {
    private final String translationKey;

    public SoundEventRegistryObject(RegistryObject<SOUND> registryObject) {
        super(registryObject);
        translationKey = Util.makeTranslationKey("sound_event", this.registryObject.getId());
    }

    public SOUND getSoundEvent() {
        return get();
    }

    @Override
    public String getTranslationKey() {
        return translationKey;
    }
}
