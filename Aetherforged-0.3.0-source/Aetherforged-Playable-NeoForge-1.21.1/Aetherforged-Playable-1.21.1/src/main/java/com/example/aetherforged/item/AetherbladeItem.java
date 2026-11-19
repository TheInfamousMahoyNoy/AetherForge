
package com.example.aetherforged.item;

import com.example.aetherforged.action.ActionRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class AetherbladeItem extends SwordItem {

    public AetherbladeItem(Tier tier, Item.Properties properties) {
        super(tier, properties);
    }

    @Override
    public void appendHoverText(
            ItemStack stack,
            Item.TooltipContext context,
            List<Component> tooltip,
            TooltipFlag flag
    ) {
        super.appendHoverText(stack, context, tooltip, flag);

        List<String> programs = InfusionData.getPrograms(stack);

        if (programs.isEmpty()) {
            tooltip.add(
                    Component.translatable("tooltip.aetherforged.no_infusions")
                            .withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC)
            );
            return;
        }

        tooltip.add(
                Component.translatable("tooltip.aetherforged.infusions")
                        .withStyle(ChatFormatting.LIGHT_PURPLE)
        );

        for (String id : programs) {
            String displayName = ActionRegistry.get(id)
                    .map(program -> program.name())
                    .orElseGet(() -> humanize(id));

            tooltip.add(
                    Component.literal(" • " + displayName)
                            .withStyle(ChatFormatting.AQUA)
            );
        }
    }

    private static String humanize(String id) {
        int separator = id.indexOf(':');
        String path = separator >= 0
                ? id.substring(separator + 1)
                : id;

        String[] words = path
                .replace('_', ' ')
                .replace('-', ' ')
                .split(" ");

        StringBuilder result = new StringBuilder();

        for (String word : words) {
            if (word.isBlank()) {
                continue;
            }

            if (!result.isEmpty()) {
                result.append(' ');
            }

            result.append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(1));
        }

        return result.toString();
    }
}
