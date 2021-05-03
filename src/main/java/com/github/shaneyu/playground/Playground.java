package com.github.shaneyu.playground;

import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Playground.MOD_ID)
public class Playground
{
    public static final String MOD_ID = "playground";
    public static final String MOD_NAME = "Playground";
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    public static Playground INSTANCE;

    public Playground() {}
}
