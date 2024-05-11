package io.github.hashibutogarasu.mla.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "mla")
public class ModConfig implements ConfigData {
    public Mode mode = Mode.MOJANG;
}
