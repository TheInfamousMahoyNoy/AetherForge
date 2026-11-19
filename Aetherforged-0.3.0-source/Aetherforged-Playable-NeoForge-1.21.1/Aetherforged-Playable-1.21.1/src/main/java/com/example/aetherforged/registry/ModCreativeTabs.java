package com.example.aetherforged.registry;

import com.example.aetherforged.Aetherforged;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Aetherforged.MOD_ID);

    public static final Supplier<CreativeModeTab> AETHERFORGED = TABS.register("aetherforged",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.aetherforged"))
                    .icon(() -> new ItemStack(ModItems.AETHERBLADE.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.AETHERBLADE.get());
                        output.accept(ModBlocks.INFUSION_CORE_ITEM.get());
                    })
                    .build());

    private ModCreativeTabs() {}
}
