package io.github.hashibutogarasu.mla.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ReloadInstance;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.function.Consumer;

@Mixin(LoadingOverlay.class)
public class LoadingOverlayMixin {
    @Unique
    private static boolean firstLoad = true;

    @Shadow @Final private ReloadInstance reload;

    @Shadow @Final private Minecraft minecraft;
    @Shadow private long fadeOutStart;

    @Unique
    private int animProgress = 0;

    @Unique
    private boolean reloading = true;

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    private void init(Minecraft p_96172_, ReloadInstance p_96173_, Consumer<Optional<Throwable>> p_96174_, boolean p_96175_, CallbackInfo ci) {
        if(!firstLoad){
            this.animProgress = 38;
            reloading = false;
        }
        this.reload.done().thenAccept(o -> {
            if(firstLoad){
                var thread = getAnimationThread();
                thread.start();
            }
            else{
                reloading = false;
            }
        });
    }

    @Inject(method = "render", at = @At(value = "HEAD"))
    private void render(GuiGraphics p_281839_, int p_282704_, int p_283650_, float p_283394_, CallbackInfo ci) {

    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIFFIIII)V", ordinal = 0))
    private void blit0(GuiGraphics instance, ResourceLocation p_282034_, int p_283671_, int p_282377_, int p_282058_, int p_281939_, float p_282285_, float p_283199_, int p_282186_, int p_282322_, int p_282481_, int p_281887_) {
        instance.blit(getMojang(animProgress), p_283671_, p_282377_, p_282058_, -0.0625F, 0.0F, 240, 80, 240, 80);
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIFFIIII)V", ordinal = 1))
    private void blit1(GuiGraphics instance, ResourceLocation p_282034_, int p_283671_, int p_282377_, int p_282058_, int p_281939_, float p_282285_, float p_283199_, int p_282186_, int p_282322_, int p_282481_, int p_281887_) {

    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;setOverlay(Lnet/minecraft/client/gui/screens/Overlay;)V"))
    private void setOverlay(Minecraft instance, Overlay p_91151_) {
        if(!this.reloading){
            this.minecraft.setOverlay(null);
        }
    }

    @Redirect(method = "render", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screens/LoadingOverlay;fadeOutStart:J", opcode = Opcodes.GETFIELD, ordinal = 2))
    private long fadeOutStart(LoadingOverlay instance) {
        return !this.reloading ? fadeOutStart : 0;
    }

    @Unique
    private ResourceLocation getMojang(int index){
        return firstLoad ? new ResourceLocation("mla", "textures/gui/title/mojang/mojang" + index + ".png") : new ResourceLocation("mla", "textures/gui/title/mojang/mojang38.png");
    }

    @Unique
    private ResourceLocation getAprilfool(int index){
        return firstLoad ? new ResourceLocation("mla", "textures/gui/title/mojang_april_fool/mojang" + index + ".png") : new ResourceLocation("mla", "textures/gui/title/mojang/mojang38.png");
    }

    @Unique
    private @NotNull Thread getAnimationThread() {
        var animthread = new Thread(()->{
            this.animProgress = 0;
            for(int i = 0; i < 38; i++){
                animProgress++;
                try {
                    Thread.sleep(70);
                } catch (InterruptedException ignored) {

                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {

            }
            reloading = false;
            firstLoad = false;
        });

        animthread.setName("animthread");
        return animthread;
    }
}
