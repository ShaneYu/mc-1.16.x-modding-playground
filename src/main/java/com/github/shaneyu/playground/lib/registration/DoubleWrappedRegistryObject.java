package com.github.shaneyu.playground.lib.registration;

import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class DoubleWrappedRegistryObject<PRIMARY extends IForgeRegistryEntry<? super PRIMARY>, SECONDARY extends IForgeRegistryEntry<? super SECONDARY>> implements IWrappedRegistryObject {
    private final RegistryObject<PRIMARY> primaryRegistryObject;
    private final RegistryObject<SECONDARY> secondaryRegistryObject;

    public DoubleWrappedRegistryObject(RegistryObject<PRIMARY> primaryRegistryObject, RegistryObject<SECONDARY> secondaryRegistryObject) {
        this.primaryRegistryObject = primaryRegistryObject;
        this.secondaryRegistryObject = secondaryRegistryObject;
    }

    public PRIMARY getPrimary() {
        return primaryRegistryObject.get();
    }

    public SECONDARY getSecondary() {
        return secondaryRegistryObject.get();
    }

    @Override
    public String getInternalRegistryName() {
        return primaryRegistryObject.getId().getPath();
    }
}
