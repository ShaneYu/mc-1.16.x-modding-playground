package com.github.shaneyu.playground.common.tile.interfaces;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IBlockTile {
    BlockPos getTilePos();

    World getTileWorld();
}
