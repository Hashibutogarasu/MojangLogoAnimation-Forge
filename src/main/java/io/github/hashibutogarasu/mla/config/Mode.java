package io.github.hashibutogarasu.mla.config;

import net.minecraft.network.chat.Component;

public enum Mode {
    MOJANG(Component.translatable("mla.mode.mojang")),
    APRIL_FOOL(Component.translatable("mla.mode.april_fool"));

    private Component component;

    Mode(Component component){
        this.component = component;
    }

    public Component getText(){
        return component;
    }
}
