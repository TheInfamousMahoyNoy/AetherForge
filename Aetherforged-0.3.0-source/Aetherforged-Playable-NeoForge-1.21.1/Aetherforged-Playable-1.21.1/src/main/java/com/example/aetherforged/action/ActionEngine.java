package com.example.aetherforged.action;

import com.example.aetherforged.item.InfusionData;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class ActionEngine {
    private static final Map<String, Long> COOLDOWNS = new ConcurrentHashMap<>();
    private ActionEngine() {}

    public static void onHit(Player player, LivingEntity target, ItemStack weapon) {
        if (!(player.level() instanceof ServerLevel level)) return;
        for (String id : InfusionData.getPrograms(weapon)) {
            ActionRegistry.get(id).filter(p -> "on_hit".equals(p.trigger())).ifPresent(program -> {
                String key = player.getUUID() + ":" + id;
                long now = level.getGameTime();
                if (COOLDOWNS.getOrDefault(key, 0L) > now) return;
                COOLDOWNS.put(key, now + program.cooldownTicks());
                for (ActionProgram.ActionStep step : program.actions()) execute(step, player, target, level);
            });
        }
    }

    private static void execute(ActionProgram.ActionStep step, Player player, LivingEntity target, ServerLevel level) {
        double amount = step.amount() == null ? 0.0 : step.amount();
        switch (step.type()) {
            case "builtin:bonus_damage" -> target.hurt(player.damageSources().playerAttack(player), (float)Math.max(0, amount));
            case "builtin:ignite" -> target.igniteForTicks(     (int)Math.max(0, Math.min(1200, amount)) );
            case "builtin:heal_wielder" -> player.heal((float)Math.max(0, amount));
            case "builtin:knockback" -> {
                Vec3 direction = target.position().subtract(player.position()).normalize();
                target.push(direction.x * amount, 0.25 * amount, direction.z * amount);
                target.hurtMarked = true;
            }
            case "builtin:lightning" -> {
                LightningBolt bolt = net.minecraft.world.entity.EntityType.LIGHTNING_BOLT.create(level);
                if (bolt != null) {
                    bolt.moveTo(target.getX(), target.getY(), target.getZ());
                    bolt.setVisualOnly(true);
                    level.addFreshEntity(bolt);
                }
            }
            case "builtin:particle_burst" -> {
                ParticleOptions particle = ParticleTypes.PORTAL;
                if (step.particle() != null) {
                    ResourceLocation key = ResourceLocation.tryParse(step.particle());
                    if (key != null) {
                        var type = BuiltInRegistries.PARTICLE_TYPE.getOptional(key).orElse(null);
                        if (type instanceof net.minecraft.core.particles.SimpleParticleType simple) particle = simple;
                    }
                }
                level.sendParticles(particle, target.getX(), target.getY() + target.getBbHeight() * 0.5,
                        target.getZ(), step.count() == null ? 16 : step.count(), 0.35, 0.35, 0.35, 0.04);
            }
        }
    }
}
