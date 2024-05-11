package io.github.hashibutogarasu.mla.sound;

import io.github.hashibutogarasu.mla.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

public class ModSounds {
    public static ResourceLocation MOJANG_SOUND = new ResourceLocation("mla", "mojang_sound");
    public static ResourceLocation APRILFOOL_SOUND = new ResourceLocation("mla", "mojang_april_fool_sound");

    public static ResourceLocation getSoundByConfig(){
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        return config.mode == ModConfig.Mode.MOJANG ? MOJANG_SOUND : APRILFOOL_SOUND;
    }

    public static void play(SoundManager soundManager, ResourceLocation location){
        SoundEvent event = SoundEvent.createVariableRangeEvent(location);
        soundManager.play(SimpleSoundInstance.forUI(event,  1.0F));
    }
}
