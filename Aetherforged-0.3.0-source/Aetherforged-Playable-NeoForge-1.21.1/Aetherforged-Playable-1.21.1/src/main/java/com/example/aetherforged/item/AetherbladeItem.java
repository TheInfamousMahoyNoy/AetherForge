package com.example.aetherforged.item;

import com.example.aetherforged.action.ActionRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.TooltipContext;
import net.minecraft.world.level.Level;

import java.util.List;

public class AetherbladeItem extends SwordItem {
    public AetherbladeItem(Tier tier, Item.Properties properties) {
        super(tier, properties);
    }

@Override
public void appendHoverText(
        ItemStack stack,
        TooltipContext context,
        List<Component> tooltip,
        TooltipFlag flag
) {
    super.appendHoverText(stack, context, tooltip, flag);

    List<String> programs = InfusionData.getPrograms(stack);

    if (programs.isEmpty()) {
        tooltip.add(Component.literal("No active infusions")
                .withStyle(ChatFormatting.GRAY));
    } else {
        tooltip.add(Component.literal("Active Infusions:")
                .withStyle(ChatFormatting.AQUA));

        for (String program : programs) {
            tooltip.add(Component.literal(" • " + program)
                    .withStyle(ChatFormatting.LIGHT_PURPLE));
        }
    }
}

    private static String humanize(String id) {
        int separator = id.indexOf(':');
        String path = separator >= 0 ? id.substring(separator + 1) : id;
        String[] words = path.replace('_', ' ').replace('-', ' ').split(" ");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (word.isBlank()) continue;
            if (!result.isEmpty()) result.append(' ');
            result.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1));
        }
        return result.toString();
    }
}
