package io.github.hashibutogarasu.mla.screen;

import io.github.hashibutogarasu.mla.MojangLogoAnimation;
import io.github.hashibutogarasu.mla.config.ConfigImpl;
import io.github.hashibutogarasu.mla.config.Mode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ModConfigScreen extends ConfigScreen {
    private final Minecraft minecraft;

    private CycleButton<Mode> modeCycleButton;
    private Mode currentMode;

    public ModConfigScreen(Screen parent){
        this(Component.translatable("text.autoconfig.mla.title"), parent);
    }

    public ModConfigScreen(Component component, Screen parent){
        super(component, parent);
        this.minecraft = Minecraft.getInstance();
        this.currentMode = this.getConfig().mode;
    }

    @Override
    protected void init() {
        super.init();

        this.modeCycleButton = this.addRenderableWidget(
                CycleButton.builder(Mode::getText).withInitialValue(currentMode).withValues(Mode.values()).create(
                        this.width / 2 - 75, 40, 150, 20,
                        Component.translatable("mla.configscreen.modecycle.text"))
        );

        this.addRenderableWidget(
                Button.builder(Component.translatable("mla.configscreen.preview.text"), button -> {
                    saveConfig();
                    MojangLogoAnimation.isLoading = true;
                    MojangLogoAnimation.firstLoad = true;
                    this.minecraft.reloadResourcePacks().thenAccept(unused -> {
                        currentMode = this.modeCycleButton.getValue();
                        this.getConfig().mode = currentMode;
                        saveConfig();
                        this.modeCycleButton.setValue(currentMode);
                    });
                })
                .size(150, 20)
                .pos(this.modeCycleButton.getX(), this.modeCycleButton.getY() + this.modeCycleButton.getHeight() + 5)
                .build()
        );
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int p_281550_, int p_282878_, float p_282465_) {
        renderDirtBackground(guiGraphics);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 8, 16777215);
        super.render(guiGraphics, p_281550_, p_282878_, p_282465_);
    }

    private void saveConfig(){
        this.getConfig().mode = this.modeCycleButton.getValue();
    }

    @Override
    public void save(Button button) {
        saveConfig();
        super.save(button);
    }
}
