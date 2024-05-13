package io.github.hashibutogarasu.mla.config;

import io.github.hashibutogarasu.mla.MojangLogoAnimation;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.Pair;

import java.util.function.Function;

@Config(name = "mla")
@Mod.EventBusSubscriber(modid = MojangLogoAnimation.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModConfig implements ConfigData {
    public ModConfig(ForgeConfigSpec.Builder builder){

    }


    public Mode mode = Mode.MOJANG;
}
