package com.github.shaneyu.playground.common.registration;

import com.github.shaneyu.playground.Playground;
import com.github.shaneyu.playground.common.item.ItemWrench;
import com.github.shaneyu.playground.lib.registration.registries.ItemDeferredRegister;
import com.github.shaneyu.playground.lib.registration.registries.ItemRegistryObject;
import net.minecraft.item.Item;

public final class PlaygroundItems {
    private PlaygroundItems() {}

    public static Item.Properties getItemDefaultProperties() {
        return new Item.Properties().group(Playground.creativeTab);
    }

    public static final ItemDeferredRegister ITEMS = new ItemDeferredRegister(Playground.MOD_ID, PlaygroundItems::getItemDefaultProperties);

    public static final ItemRegistryObject<ItemWrench> WRENCH = ITEMS.register("wrench", ItemWrench::new);
}
