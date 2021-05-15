package com.github.shaneyu.playground.common.tag;

import com.github.shaneyu.playground.Playground;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.tags.ItemTags;

public final class PlaygroundTags {
    private PlaygroundTags() {}

    public static class Items {
        private Items() {}

        public static final INamedTag<Item> WRENCHES = forgeTag("wrenches");
        public static final INamedTag<Item> TOOLS = forgeTag("tools");
        public static final INamedTag<Item> TOOLS_WRENCH = forgeTag("tools/wrench");

        private static INamedTag<Item> forgeTag(String name) {
            return ItemTags.makeWrapperTag("forge:" + name);
        }

        private static INamedTag<Item> tag(String name) {
            return ItemTags.makeWrapperTag(Playground.modLoc(name).toString());
        }
    }
}
