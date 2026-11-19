package com.example.aetherforged;

import com.example.aetherforged.menu.ModMenus;
import com.example.aetherforged.registry.ModBlocks;
import com.example.aetherforged.registry.ModItems;\nimport com.example.aetherforged.registry.ModCreativeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Aetherforged.MOD_ID)
public final class Aetherforged {
    public static final String MOD_ID = "aetherforged";
    public Aetherforged(IEventBus modBus) {
        ModItems.ITEMS.register(modBus);
        ModBlocks.BLOCKS.register(modBus);
        ModMenus.MENUS.register(modBus);\n        ModCreativeTabs.TABS.register(modBus);
    }
}
