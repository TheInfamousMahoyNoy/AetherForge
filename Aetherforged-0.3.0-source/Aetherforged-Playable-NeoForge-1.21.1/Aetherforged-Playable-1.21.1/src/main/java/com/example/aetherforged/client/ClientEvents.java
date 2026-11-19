package com.example.aetherforged.client;

import com.example.aetherforged.Aetherforged;
import com.example.aetherforged.menu.ModMenus;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = Aetherforged.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public final class ClientEvents {
    private ClientEvents() {}
    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenus.INFUSION_CORE.get(), InfusionCoreScreen::new);
    }
}
