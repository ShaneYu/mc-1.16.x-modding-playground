package com.github.shaneyu.playground.lib.registration;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Function;
import java.util.function.Supplier;

public class WrappedDeferredRegister<T extends IForgeRegistryEntry<T>> {
    protected final DeferredRegister<T> internal;

    protected WrappedDeferredRegister(String modId, IForgeRegistry<T> registry) {
        internal = DeferredRegister.create(registry, modId);
    }

    protected WrappedDeferredRegister(String modId, Class<T> base) {
        internal = DeferredRegister.create(base, modId);
    }

    protected <I extends T, N extends WrappedRegistryObject<I>> N register(String name, Supplier<? extends I> sup, Function<RegistryObject<I>, N> namedObject) {
        return namedObject.apply(internal.register(name, sup));
    }

    public void createAndRegister(IEventBus bus, String name) {
        internal.makeRegistry(name, RegistryBuilder::new);
        register(bus);
    }

    public void createAndRegisterWithTags(IEventBus bus, String name, String tagFolder) {
        internal.makeRegistry(name, () -> new RegistryBuilder<T>().tagFolder(tagFolder));
        register(bus);
    }

    public void register(IEventBus bus) {
        internal.register(bus);
    }
}
