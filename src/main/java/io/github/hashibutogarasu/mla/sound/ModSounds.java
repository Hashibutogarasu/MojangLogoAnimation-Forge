package io.github.hashibutogarasu.mla.sound;

import io.github.hashibutogarasu.mla.config.ModConfig;
import io.github.hashibutogarasu.mla.config.Mode;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import static io.github.hashibutogarasu.mla.MojangLogoAnimation.MODID;

public class ModSounds {
    public static ResourceLocation MOJANG_SOUND = new ResourceLocation(MODID, "mojang_sound");
    public static ResourceLocation APRILFOOL_SOUND = new ResourceLocation(MODID, "mojang_april_fool_sound");

    public static SoundInstance playingInstance;

    public static ResourceLocation getSoundByConfig(){
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        return config.mode == Mode.MOJANG ? MOJANG_SOUND : APRILFOOL_SOUND;
    }

    public static SimpleSoundInstance getSoundInstance(ResourceLocation location){
        SoundEvent event = SoundEvent.createVariableRangeEvent(location);
        return SimpleSoundInstance.forUI(event,  1.0F);
    }

    public static void play(SoundManager soundManager, ResourceLocation location){
        ModSounds.playingInstance = getSoundInstance(location);
        soundManager.play(getSoundInstance(location));
    }
}
