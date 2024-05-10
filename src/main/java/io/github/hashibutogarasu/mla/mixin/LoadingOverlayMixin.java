package io.github.hashibutogarasu.mla.mixin;

import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.LoadingOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LoadingOverlay.class)
public class LoadingOverlayMixin {
    @Inject(method = "render", at = @At(value = "HEAD"))
    private void render(GuiGraphics p_281839_, int p_282704_, int p_283650_, float p_283394_, CallbackInfo ci) {

    }
}
