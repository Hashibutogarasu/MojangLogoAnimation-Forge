package io.github.hashibutogarasu.mla.mixin;

import io.github.hashibutogarasu.mla.MojangLogoAnimation;
import io.github.hashibutogarasu.mla.config.ModConfig;
import io.github.hashibutogarasu.mla.config.Mode;
import io.github.hashibutogarasu.mla.sound.ModSounds;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.client.gui.screens.Screen;
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
    @Shadow @Final private ReloadInstance reload;

    @Shadow @Final private Minecraft minecraft;
    @Shadow private long fadeOutStart;

    @Unique
    private int mojangLogoAnimation_Forge$animProgress = 0;

    @Unique
    private boolean mojangLogoAnimation_Forge$reloading = true;

    @Unique
    private ModConfig mojangLogoAnimation_Forge$config;

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    private void init(Minecraft p_96172_, ReloadInstance p_96173_, Consumer<Optional<Throwable>> p_96174_, boolean p_96175_, CallbackInfo ci) {
        mojangLogoAnimation_Forge$config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        if(!MojangLogoAnimation.firstLoad){
            this.mojangLogoAnimation_Forge$animProgress = this.mojangLogoAnimation_Forge$config.mode.getMax();
            this.mojangLogoAnimation_Forge$reloading = false;
        }
        this.reload.done().thenAccept(o -> {
            if(MojangLogoAnimation.firstLoad){
                var thread = getAnimationThread();
                thread.start();
            }
            else{
                this.mojangLogoAnimation_Forge$reloading = false;
            }
        });
    }

    @Inject(method = "render", at = @At(value = "HEAD"))
    private void render(GuiGraphics p_281839_, int p_282704_, int p_283650_, float p_283394_, CallbackInfo ci) {

    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIFFIIII)V", ordinal = 0))
    private void blit0(GuiGraphics instance, ResourceLocation p_282034_, int p_283671_, int p_282377_, int p_282058_, int p_281939_, float p_282285_, float p_283199_, int p_282186_, int p_282322_, int p_282481_, int p_281887_) {
        double d = Math.min((double)instance.guiWidth() * 0.75, instance.guiHeight()) * 0.25;
        double e = d * 4.0;
        int r = (int)(e);

        instance.blit(MojangLogoAnimation.firstLoad ? this.mojangLogoAnimation_Forge$config.mode.getResourcelocation(this.mojangLogoAnimation_Forge$animProgress) : this.mojangLogoAnimation_Forge$config.mode.getResourcelocation(mojangLogoAnimation_Forge$config.mode.getMax()), (instance.guiWidth() / 2) - (r / 2), p_282377_, p_282058_, -0.0625F, 0.0F, r, (int) d, r, (int) d);
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIFFIIII)V", ordinal = 1))
    private void blit1(GuiGraphics instance, ResourceLocation p_282034_, int p_283671_, int p_282377_, int p_282058_, int p_281939_, float p_282285_, float p_283199_, int p_282186_, int p_282322_, int p_282481_, int p_281887_) {

    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;setOverlay(Lnet/minecraft/client/gui/screens/Overlay;)V"))
    private void setOverlay(Minecraft instance, Overlay p_91151_) {
        if(!this.mojangLogoAnimation_Forge$reloading){
            this.minecraft.setOverlay(null);
        }
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;init(Lnet/minecraft/client/Minecraft;II)V", ordinal = 0))
    private void initScreen(Screen instance, Minecraft p_96607_, int p_96608_, int p_96609_) {
        if(!this.mojangLogoAnimation_Forge$reloading){
            if(this.minecraft.screen != null){
                this.minecraft.screen.init(p_96607_, p_96608_, p_96609_);
            }
        }
    }

    @Redirect(method = "render", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screens/LoadingOverlay;fadeOutStart:J", opcode = Opcodes.GETFIELD, ordinal = 2))
    private long fadeOutStart(LoadingOverlay instance) {
        return !this.mojangLogoAnimation_Forge$reloading ? fadeOutStart : 0;
    }

    @Unique
    private @NotNull Thread getAnimationThread() {
        var animthread = new Thread(()->{
            this.mojangLogoAnimation_Forge$animProgress = 0;
            ModSounds.play(Minecraft.getInstance().getSoundManager(), mojangLogoAnimation_Forge$config.mode.getSound());
            for(int i = 0; i < 38; i++){
                mojangLogoAnimation_Forge$animProgress++;
                try {
                    Thread.sleep(70);
                } catch (InterruptedException ignored) {

                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {

            }

            if(MojangLogoAnimation.currentmusic != null && MojangLogoAnimation.firstLoad){
                minecraft.getMusicManager().startPlaying(MojangLogoAnimation.currentmusic);
            }

            MojangLogoAnimation.isLoading = false;
            mojangLogoAnimation_Forge$reloading = false;
            MojangLogoAnimation.firstLoad = false;
        });

        animthread.setName("animthread");
        return animthread;
    }
}
