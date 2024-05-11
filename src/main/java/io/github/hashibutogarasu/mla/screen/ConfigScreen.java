package io.github.hashibutogarasu.mla.screen;

import io.github.hashibutogarasu.mla.config.ConfigImpl;
import io.github.hashibutogarasu.mla.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class ConfigScreen extends Screen implements ConfigImpl {
    private static final Component CANCELBUTTON_TEXT = Component.translatable("text.mla.button.cancel");
    private static final Component DONEBUTTON_TEXT = Component.translatable("text.mla.button.done");
    private Button cancelButton;
    private Button doneButton;
    private final Screen parent;
    private final Minecraft minecraft;

    protected ConfigScreen(Component p_96550_, Screen parent) {
        super(p_96550_);
        this.parent = parent;
        this.minecraft = Minecraft.getInstance();
    }

    @Override
    protected void init() {
        super.init();
        this.doneButton = this.addRenderableWidget(Button.builder(DONEBUTTON_TEXT, this::save).size(120, 20).pos((this.width / 2) - 120, this.height - 25).build());

        this.cancelButton = this.addRenderableWidget(Button.builder(CANCELBUTTON_TEXT, button -> {
            this.onClose();
        }).size(120, 20).pos(this.doneButton.getX() + this.doneButton.getWidth() + 5, this.height - 25).build());
    }

    @Override
    public void save(Button button) {
        AutoConfig.getConfigHolder(ModConfig.class).save();
        this.onClose();
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }

    @Override
    public @NotNull Minecraft getMinecraft() {
        return minecraft;
    }
}
