package com.example.aetherforged.registry;

import com.example.aetherforged.Aetherforged;
import com.example.aetherforged.block.InfusionCoreBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;
import java.util.function.Supplier;

public final class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Aetherforged.MOD_ID);
    public static final Supplier<InfusionCoreBlock> INFUSION_CORE = BLOCKS.register("infusion_core",
            () -> new InfusionCoreBlock());
    public static final Supplier<Item> INFUSION_CORE_ITEM = ModItems.ITEMS.register("infusion_core",
            () -> new BlockItem(INFUSION_CORE.get(), new Item.Properties()));
    private ModBlocks() {}
}
