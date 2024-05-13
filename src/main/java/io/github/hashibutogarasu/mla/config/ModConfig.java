package io.github.hashibutogarasu.mla.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import net.minecraftforge.common.ForgeConfigSpec;

@Config(name = "mla")
public class ModConfig implements ConfigData {
    public Mode mode = Mode.MOJANG;
}
