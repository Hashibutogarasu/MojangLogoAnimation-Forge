package io.github.hashibutogarasu.mla.config;

import io.github.hashibutogarasu.mla.sound.ModSounds;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import static io.github.hashibutogarasu.mla.MojangLogoAnimation.MODID;

public enum Mode {
    MOJANG(Component.translatable("mla.mode.mojang"), "textures/gui/title/mojang/mojang", ModSounds.MOJANG_SOUND, 38),
    APRIL_FOOL(Component.translatable("mla.mode.april_fool"), "textures/gui/title/mojang_april_fool/mojang", ModSounds.APRILFOOL_SOUND, 38);

    private final int max;
    private final Component component;
    private final String resourcelocation;
    private final ResourceLocation sound;

    Mode(Component component, String resourcelocation, ResourceLocation sound, int max){
        this.component = component;
        this.resourcelocation = resourcelocation;
        this.sound = sound;
        this.max = max;
    }

    public Component getText(){
        return component;
    }

    public int getMax() {
        return max;
    }

    public ResourceLocation getSound() {
        return sound;
    }

    public ResourceLocation getResourcelocation(int index) {
        return new ResourceLocation(MODID, this.resourcelocation + index + ".png");
    }
}
