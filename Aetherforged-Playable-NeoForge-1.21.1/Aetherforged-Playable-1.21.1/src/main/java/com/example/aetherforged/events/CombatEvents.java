package com.example.aetherforged.events;

import com.example.aetherforged.action.ActionEngine;
import com.example.aetherforged.registry.ModItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;

@EventBusSubscriber
public final class CombatEvents {
    private CombatEvents() {}
    @SubscribeEvent
    public static void onAttack(AttackEntityEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!(event.getTarget() instanceof LivingEntity target)) return;
        if (player.level().isClientSide) return;
        if (player.getMainHandItem().is(ModItems.AETHERBLADE.get()))
            ActionEngine.onHit(player, target, player.getMainHandItem());
    }
}
