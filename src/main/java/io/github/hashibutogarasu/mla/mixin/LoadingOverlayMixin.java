package io.github.hashibutogarasu.mla.mixin;

import io.github.hashibutogarasu.mla.MojangLogoAnimation;
import io.github.hashibutogarasu.mla.overlay.CustomLoadingOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.server.packs.resources.ReloadInstance;
import net.minecraftforge.client.loading.ForgeLoadingOverlay;
import net.minecraftforge.fml.earlydisplay.DisplayWindow;
import net.minecraftforge.fml.loading.progress.ProgressMeter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Mixin(ForgeLoadingOverlay.class)
public abstract class LoadingOverlayMixin extends Overlay {
    @Shadow(remap = false) @Final private DisplayWindow displayWindow;

    @Shadow(remap = false) @Final private Minecraft minecraft;

    @Shadow(remap = false) @Final private ProgressMeter progress;

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    private void init(Minecraft mc, ReloadInstance reloader, Consumer<Optional<Throwable>> errorConsumer, DisplayWindow displayWindow, CallbackInfo ci) {

    }

    @Redirect(remap = false, method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/earlydisplay/DisplayWindow;render(I)V"))
    private void render(DisplayWindow instance, int alpha) {
        MojangLogoAnimation.currentProgress = progress;
    }

    @Inject(remap = false, method = "lambda$newInstance$0", at = @At(value = "NEW", target = "(Lnet/minecraft/client/Minecraft;Lnet/minecraft/server/packs/resources/ReloadInstance;Ljava/util/function/Consumer;Lnet/minecraftforge/fml/earlydisplay/DisplayWindow;)Lnet/minecraftforge/client/loading/ForgeLoadingOverlay;"), cancellable = true)
    private static void newInstance(Supplier<Minecraft> mc, Supplier<ReloadInstance> ri, Consumer<Optional<Throwable>> handler, DisplayWindow window, CallbackInfoReturnable<LoadingOverlay> cir) {
        var overlay = new CustomLoadingOverlay(mc.get(), ri.get(), handler, false);
        cir.setReturnValue(overlay);
    }
}
