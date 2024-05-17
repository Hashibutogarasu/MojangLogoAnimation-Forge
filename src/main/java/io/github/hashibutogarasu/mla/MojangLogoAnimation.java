package io.github.hashibutogarasu.mla;

import com.mojang.logging.LogUtils;
import io.github.hashibutogarasu.mla.config.ModConfig;
import io.github.hashibutogarasu.mla.screen.ModConfigScreen;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.minecraft.sounds.Music;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.progress.ProgressMeter;
import org.slf4j.Logger;

@Mod(MojangLogoAnimation.MODID)
public class MojangLogoAnimation {
    public static final String MODID = "mla";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static boolean firstLoad = true;
    public static boolean isLoading = true;
    public static Music currentmusic;
    public static ProgressMeter currentProgress;

    public MojangLogoAnimation() {
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::onFMLClientSetupEvent);
    }

    @SubscribeEvent
    public void onFMLClientSetupEvent(FMLClientSetupEvent event) {
        ModLoadingContext.get().registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory((client, parent) -> new ModConfigScreen(parent))
        );
    }
}
