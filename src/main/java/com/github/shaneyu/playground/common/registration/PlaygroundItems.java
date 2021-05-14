package com.github.shaneyu.playground.common.registration;

import com.github.shaneyu.playground.Playground;
import com.github.shaneyu.playground.lib.registration.registries.ItemDeferredRegister;
import net.minecraft.item.Item;

public final class PlaygroundItems {
    private PlaygroundItems() {}

    public static Item.Properties getItemDefaultProperties() {
        return new Item.Properties().group(Playground.creativeTab);
    }

    public static final ItemDeferredRegister ITEMS = new ItemDeferredRegister(Playground.MOD_ID, PlaygroundItems::getItemDefaultProperties);

    // TODO: Register mod items here
}
