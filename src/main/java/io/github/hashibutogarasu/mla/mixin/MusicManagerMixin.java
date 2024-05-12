package io.github.hashibutogarasu.mla.mixin;

import io.github.hashibutogarasu.mla.MojangLogoAnimation;
import io.github.hashibutogarasu.mla.sound.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.sounds.Music;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MusicManager.class)
public class MusicManagerMixin {
    @Inject(method = "startPlaying", at = @At(value = "HEAD"), cancellable = true)
    private void startPlaying(Music p_120185_, CallbackInfo ci) {
        if(MojangLogoAnimation.isLoading && ModSounds.playingInstance != null && MojangLogoAnimation.firstLoad){
            Minecraft.getInstance().getMusicManager().stopPlaying();
            MojangLogoAnimation.currentmusic = p_120185_;
            ci.cancel();
        }
    }
}
