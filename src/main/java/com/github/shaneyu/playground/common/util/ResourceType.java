package com.github.shaneyu.playground.common.util;

public enum ResourceType {
    GUI("gui"),
    SOUND("sound"),
    TEXTURE_BLOCK("textures/block"),
    TEXTURE_ITEM("textures/item");

    private final String prefix;

    ResourceType(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix + "/";
    }
}
