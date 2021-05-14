package com.github.shaneyu.playground.common.block.attribute;

import net.minecraft.block.AbstractBlock;

public class AttributeLight implements Attribute {
    private final int lightValue;

    public AttributeLight(int lightValue) {
        this.lightValue = lightValue;
    }

    @Override
    public void applyPropertyChanges(AbstractBlock.Properties props) {
        props.setLightLevel(state -> lightValue);
    }
}
