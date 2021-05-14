package com.github.shaneyu.playground.common.tile.generators;

import com.github.shaneyu.playground.common.tile.TileEntityBase;
import com.github.shaneyu.playground.lib.provider.IBlockProvider;

public abstract class TileEntityGenerator extends TileEntityBase {
    protected TileEntityGenerator(IBlockProvider blockProvider) {
        super(blockProvider);
    }

    @Override
    public int getActiveLightValue() {
        return 8;
    }

    @Override
    public boolean renderUpdate() {
        return true;
    }

    @Override
    public boolean lightUpdate() {
        return true;
    }
}
