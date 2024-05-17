package io.github.hashibutogarasu.mla.overlay;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.hashibutogarasu.mla.MojangLogoAnimation;
import io.github.hashibutogarasu.mla.config.ModConfig;
import io.github.hashibutogarasu.mla.sound.ModSounds;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ReloadInstance;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.IntSupplier;

public class CustomLoadingOverlay extends LoadingOverlay {
    static final ResourceLocation MOJANG_STUDIOS_LOGO_LOCATION = new ResourceLocation("textures/gui/title/mojangstudios.png");
    private static final int LOGO_BACKGROUND_COLOR = FastColor.ARGB32.color(255, 239, 50, 61);
    private static final int LOGO_BACKGROUND_COLOR_DARK = FastColor.ARGB32.color(255, 0, 0, 0);
    private static final IntSupplier BRAND_BACKGROUND = () -> {
        return (Boolean)Minecraft.getInstance().options.darkMojangStudiosBackground().get() ? LOGO_BACKGROUND_COLOR_DARK : LOGO_BACKGROUND_COLOR;
    };
    private static final int LOGO_SCALE = 240;
    private static final float LOGO_QUARTER_FLOAT = 60.0F;
    private static final int LOGO_QUARTER = 60;
    private static final int LOGO_HALF = 120;
    private static final float LOGO_OVERLAP = 0.0625F;
    private static final float SMOOTHING = 0.95F;
    public static final long FADE_OUT_TIME = 1000L;
    public static final long FADE_IN_TIME = 500L;
    private final Minecraft minecraft;
    private final ReloadInstance reload;
    private final Consumer<Optional<Throwable>> onFinish;
    private final boolean fadeIn;
    private float currentProgress;
    private long fadeOutStart = -1L;
    private long fadeInStart = -1L;

    private final ModConfig config;
    private int animProgress = 0;
    private boolean reloading = true;

    public CustomLoadingOverlay(Minecraft minecraft, ReloadInstance reload, Consumer<Optional<Throwable>> onFinish, boolean fadeIn) {
        super(minecraft, reload, onFinish, fadeIn);
        this.minecraft = minecraft;
        this.reload = reload;
        this.onFinish = onFinish;
        this.fadeIn = fadeIn;
        this.config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

        if (!MojangLogoAnimation.firstLoad) {
            this.animProgress = this.config.mode.getMax();
            this.reloading = false;
        } else {
            this.reload.done().thenAccept(o -> this.getAnimthread().start());
        }
    }

    private static int replaceAlpha(int p_169325_, int p_169326_) {
        return p_169325_ & 16777215 | p_169326_ << 24;
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int i = guiGraphics.guiWidth();
        int j = guiGraphics.guiHeight();
        long k = Util.getMillis();
        if (this.fadeIn && this.fadeInStart == -1L) {
            this.fadeInStart = k;
        }

        float f = this.fadeOutStart > -1L ? (float)(k - this.fadeOutStart) / 1000.0F : -1.0F;
        float f1 = this.fadeInStart > -1L ? (float)(k - this.fadeInStart) / 500.0F : -1.0F;
        float f2;
        int l1;
        if (f >= 1.0F) {
            if (this.minecraft.screen != null) {
                this.minecraft.screen.render(guiGraphics, 0, 0, partialTick);
            }

            l1 = Mth.ceil((1.0F - Mth.clamp(f - 1.0F, 0.0F, 1.0F)) * 255.0F);
            guiGraphics.fill(RenderType.guiOverlay(), 0, 0, i, j, replaceAlpha(BRAND_BACKGROUND.getAsInt(), l1));
            f2 = 1.0F - Mth.clamp(f - 1.0F, 0.0F, 1.0F);
        } else if (this.fadeIn) {
            if (this.minecraft.screen != null && f1 < 1.0F) {
                this.minecraft.screen.render(guiGraphics, mouseX, mouseY, partialTick);
            }

            l1 = Mth.ceil(Mth.clamp((double)f1, 0.15, 1.0) * 255.0);
            guiGraphics.fill(RenderType.guiOverlay(), 0, 0, i, j, replaceAlpha(BRAND_BACKGROUND.getAsInt(), l1));
            f2 = Mth.clamp(f1, 0.0F, 1.0F);
        } else {
            l1 = BRAND_BACKGROUND.getAsInt();
            float f3 = (float)(l1 >> 16 & 255) / 255.0F;
            float f4 = (float)(l1 >> 8 & 255) / 255.0F;
            float f5 = (float)(l1 & 255) / 255.0F;
            GlStateManager._clearColor(f3, f4, f5, 1.0F);
            GlStateManager._clear(16384, Minecraft.ON_OSX);
            f2 = 1.0F;
        }

        l1 = (int)((double)guiGraphics.guiWidth() * 0.5);
        int k2 = (int)((double)guiGraphics.guiHeight() * 0.5);
        double d1 = Math.min((double)guiGraphics.guiWidth() * 0.75, (double)guiGraphics.guiHeight()) * 0.25;
        int i1 = (int)(d1 * 0.5);
        double d0 = d1 * 4.0;
        int j1 = (int)(d0 * 0.5);
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(770, 1);
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, f2);

        double d = Math.min((double)guiGraphics.guiWidth() * 0.75, guiGraphics.guiHeight()) * 0.25;
        double e = d * 4.0;
        int r = (int)(e);

        guiGraphics.blit(MojangLogoAnimation.firstLoad ? this.config.mode.getResourcelocation(this.animProgress) : this.config.mode.getResourcelocation(this.config.mode.getMax()), (guiGraphics.guiWidth() / 2) - (r / 2), 100, 0, -0.0625F, 0.0F, r, (int) d, r, (int) d);

        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        int k1 = (int)((double)guiGraphics.guiHeight() * 0.8325);
        float f6 = this.reload.getActualProgress();
        this.currentProgress = Mth.clamp(this.currentProgress * 0.95F + f6 * 0.050000012F, 0.0F, 1.0F);
        if (f < 1.0F) {
            this.drawProgressBar(guiGraphics, i / 2 - j1, k1 - 5, i / 2 + j1, k1 + 5, 1.0F - Mth.clamp(f, 0.0F, 1.0F));
        }

        if (f >= 2.0F && !this.reloading) {
            this.minecraft.setOverlay(null);
        }

        if (this.fadeOutStart == -1L && this.reload.isDone() && (!this.fadeIn || f1 >= 2.0F) && !this.reloading) {
            this.fadeOutStart = Util.getMillis();

            try {
                this.reload.checkExceptions();
                this.onFinish.accept(Optional.empty());
            } catch (Throwable var23) {
                this.onFinish.accept(Optional.of(var23));
            }

            if (this.minecraft.screen != null) {
                this.minecraft.screen.init(this.minecraft, guiGraphics.guiWidth(), guiGraphics.guiHeight());
            }
        }
    }

    private void drawProgressBar(GuiGraphics guiGraphics, int p_96184_, int p_96185_, int p_96186_, int p_96187_, float p_96188_) {
        int i = Mth.ceil((float)(p_96186_ - p_96184_ - 2) * this.currentProgress);
        int j = Math.round(p_96188_ * 255.0F);
        int k = FastColor.ARGB32.color(j, 255, 255, 255);
        guiGraphics.fill(p_96184_ + 2, p_96185_ + 2, p_96184_ + i, p_96187_ - 2, k);
        guiGraphics.fill(p_96184_ + 1, p_96185_, p_96186_ - 1, p_96185_ + 1, k);
        guiGraphics.fill(p_96184_ + 1, p_96187_, p_96186_ - 1, p_96187_ - 1, k);
        guiGraphics.fill(p_96184_, p_96185_, p_96184_ + 1, p_96187_, k);
        guiGraphics.fill(p_96186_, p_96185_, p_96186_ - 1, p_96187_, k);
    }

    private @NotNull Thread getAnimthread() {
        var animthread = new Thread(()->{
            this.animProgress = 0;
            ModSounds.play(Minecraft.getInstance().getSoundManager(), config.mode.getSound());
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

            if(MojangLogoAnimation.currentmusic != null && MojangLogoAnimation.firstLoad){
                minecraft.getMusicManager().startPlaying(MojangLogoAnimation.currentmusic);
            }

            MojangLogoAnimation.isLoading = false;
            this.reloading = false;
            MojangLogoAnimation.firstLoad = false;
        });

        animthread.setName("animthread");
        return animthread;
    }
}
