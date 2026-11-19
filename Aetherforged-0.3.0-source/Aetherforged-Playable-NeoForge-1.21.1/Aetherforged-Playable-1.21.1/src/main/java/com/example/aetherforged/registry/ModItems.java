
package com.example.aetherforged.registry;

import com.example.aetherforged.Aetherforged;
import com.example.aetherforged.item.AetherbladeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class ModItems {

    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(Aetherforged.MOD_ID);

    public static final Supplier<Item> AETHERBLADE =
            ITEMS.register("aetherblade", () ->
                    new AetherbladeItem(
                            Tiers.DIAMOND,
                            new Item.Properties()
                                    .stacksTo(1)
                                    .attributes(
                                            SwordItem.createAttributes(
                                                    Tiers.DIAMOND,
                                                    5,
                                                    -2.4F
                                            )
                                    )
                    )
            );

    private ModItems() {}
}
