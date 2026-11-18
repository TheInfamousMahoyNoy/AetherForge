package com.example.aetherforged.menu;

import com.example.aetherforged.Aetherforged;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import java.util.function.Supplier;

public final class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS =
        DeferredRegister.create(Registries.MENU, Aetherforged.MOD_ID);
    public static final Supplier<MenuType<InfusionCoreMenu>> INFUSION_CORE =
        MENUS.register("infusion_core", () -> IMenuTypeExtension.create(InfusionCoreMenu::new));
    private ModMenus() {}
}
