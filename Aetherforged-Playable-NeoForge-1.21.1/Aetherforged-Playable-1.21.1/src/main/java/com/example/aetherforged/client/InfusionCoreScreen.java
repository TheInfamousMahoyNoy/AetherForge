package com.example.aetherforged.client;

import com.example.aetherforged.action.ActionProgram;
import com.example.aetherforged.menu.InfusionCoreMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class InfusionCoreScreen extends AbstractContainerScreen<InfusionCoreMenu> {
    public InfusionCoreScreen(InfusionCoreMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 230;
        this.imageHeight = 220;
        this.inventoryLabelY = 126;
    }

    @Override protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        int x = leftPos, y = topPos;
        graphics.fill(x, y, x + imageWidth, y + imageHeight, 0xF0181028);
        graphics.fill(x + 4, y + 4, x + imageWidth - 4, y + 34, 0xFF29143D);
        graphics.fill(x + 8, y + 40, x + imageWidth - 8, y + 119, 0xFF100D1B);
        graphics.fill(x + 8, y + 124, x + imageWidth - 8, y + 218, 0xFF21182C);
        graphics.renderOutline(x, y, imageWidth, imageHeight, 0xFF8D4BCA);
        int index = 0;
        for (ActionProgram p : menu.programs()) {
            int yy = y + 44 + index * 18;
            if (yy > y + 112) break;
            graphics.fill(x + 12, yy, x + imageWidth - 12, yy + 16,
                index == menu.selected() ? 0xFF633A85 : 0xFF30243F);
            index++;
        }
    }

    @Override protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(font, "VOID INFUSION CORE", 12, 13, 0xFFE5C8FF, false);
        graphics.drawString(font, "Select a program", 12, 38, 0xFFBBA5D0, false);
        int i = 0;
        for (ActionProgram p : menu.programs()) {
            if (i >= 4) break;
            graphics.drawString(font, (i == menu.selected() ? "> " : "  ") + p.name(), 17, 48 + i * 18, 0xFFE9E0F2, false);
            i++;
        }
        graphics.drawString(font, "Apply to Aetherblade in hand", 12, 112, 0xFFBBA5D0, false);
    }

    @Override public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTooltip(graphics, mouseX, mouseY);
    }

    @Override public boolean mouseClicked(double mouseX, double mouseY, int button) {
        double localX = mouseX - leftPos, localY = mouseY - topPos;
        if (localX >= 12 && localX <= imageWidth - 12 && localY >= 44 && localY <= 116) {
            int index = (int)((localY - 44) / 18);
            if (index >= 0 && index < menu.programs().size()) {
                minecraft.gameMode.handleInventoryButtonClick(menu.containerId, 100 + index);
                return true;
            }
        }
        if (localX >= 12 && localX <= imageWidth - 12 && localY >= 92 && localY <= 121) {
            minecraft.gameMode.handleInventoryButtonClick(menu.containerId, 1);
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
