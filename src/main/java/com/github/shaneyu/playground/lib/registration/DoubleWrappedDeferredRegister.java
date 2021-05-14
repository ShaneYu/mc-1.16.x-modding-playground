package com.github.shaneyu.playground.lib.registration;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class DoubleWrappedDeferredRegister<PRIMARY extends IForgeRegistryEntry<PRIMARY>, SECONDARY extends IForgeRegistryEntry<SECONDARY>> {
    private final DeferredRegister<PRIMARY> primaryRegister;
    private final DeferredRegister<SECONDARY> secondaryRegister;

    public DoubleWrappedDeferredRegister(String modId, IForgeRegistry<PRIMARY> primaryRegistry, IForgeRegistry<SECONDARY> secondaryRegistry) {
        primaryRegister = DeferredRegister.create(primaryRegistry, modId);
        secondaryRegister = DeferredRegister.create(secondaryRegistry, modId);
    }

    public <P extends PRIMARY, S extends SECONDARY, N extends DoubleWrappedRegistryObject<P, S>> N register(
            String name, Supplier<? extends P> primarySupplier, Supplier<? extends S> secondarySupplier, BiFunction<RegistryObject<P>, RegistryObject<S>, N> namedObject) {

        return namedObject.apply(primaryRegister.register(name, primarySupplier), secondaryRegister.register(name, secondarySupplier));
    }

    public <P extends PRIMARY, S extends SECONDARY, N extends DoubleWrappedRegistryObject<P, S>> N register(
            String name, Supplier<? extends P> primarySupplier, Function<P, S> secondarySupplier, BiFunction<RegistryObject<P>, RegistryObject<S>, N> namedObject) {

        RegistryObject<P> primaryObject = primaryRegister.register(name, primarySupplier);

        return namedObject.apply(primaryObject, secondaryRegister.register(name, () -> secondarySupplier.apply(primaryObject.get())));
    }

    public void register(IEventBus bus) {
        primaryRegister.register(bus);
        secondaryRegister.register(bus);
    }
}
