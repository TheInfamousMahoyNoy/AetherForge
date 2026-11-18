package com.example.aetherforged.events;

import com.example.aetherforged.action.ActionRegistry;
import net.minecraft.commands.Commands;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;

@EventBusSubscriber
public final class ModLifecycleEvents {
    private ModLifecycleEvents() {}
    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        try { ActionRegistry.reload(); }
        catch (Exception ex) { System.err.println("[Aetherforged] Action load failed: " + ex.getMessage()); }
    }
    @SubscribeEvent
    public static void onCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("aetherforged")
            .requires(source -> source.hasPermission(2))
            .then(Commands.literal("reload").executes(ctx -> {
                try {
                    ActionRegistry.reload();
                    ctx.getSource().sendSuccess(() -> net.minecraft.network.chat.Component.literal(
                        "Aetherforged: reloaded " + ActionRegistry.all().size() + " action programs."), true);
                    return 1;
                } catch (Exception ex) {
                    ctx.getSource().sendFailure(net.minecraft.network.chat.Component.literal("Reload failed: " + ex.getMessage()));
                    return 0;
                }
            })));
    }
}
