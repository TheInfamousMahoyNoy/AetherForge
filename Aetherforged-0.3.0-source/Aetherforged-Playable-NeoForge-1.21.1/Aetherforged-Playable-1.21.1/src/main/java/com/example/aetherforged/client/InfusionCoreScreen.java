package com.example.aetherforged.client;

import com.example.aetherforged.action.ActionProgram;
import com.example.aetherforged.menu.InfusionCoreMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class InfusionCoreScreen extends AbstractContainerScreen<InfusionCoreMenu> {
    private static final int LIST_TOP = 44;
    private static final int ROW_HEIGHT = 18;
    private static final int APPLY_TOP = 120;
    private static final int APPLY_BOTTOM = 140;

    public InfusionCoreScreen(InfusionCoreMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 230;
        this.imageHeight = 240;
        this.inventoryLabelY = 146;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        int x = leftPos, y = topPos;
        graphics.fill(x, y, x + imageWidth, y + imageHeight, 0xF0181028);
        graphics.fill(x + 4, y + 4, x + imageWidth - 4, y + 34, 0xFF29143D);
        graphics.fill(x + 8, y + 40, x + imageWidth - 8, y + 116, 0xFF100D1B);
        graphics.fill(x + 8, y + 118, x + imageWidth - 8, y + 142, 0xFF352047);
        graphics.fill(x + 8, y + 156, x + imageWidth - 8, y + imageHeight - 4, 0xFF21182C);
        graphics.renderOutline(x, y, imageWidth, imageHeight, 0xFF8D4BCA);

        int visible = Math.min(4, menu.programs().size());
        for (int index = 0; index < visible; index++) {
            int yy = y + LIST_TOP + index * ROW_HEIGHT;
            boolean hovered = mouseX >= x + 12 && mouseX <= x + imageWidth - 12
                    && mouseY >= yy && mouseY <= yy + ROW_HEIGHT - 2;
            int color = index == menu.selected() ? 0xFF633A85 : (hovered ? 0xFF49345D : 0xFF30243F);
            graphics.fill(x + 12, yy, x + imageWidth - 12, yy + ROW_HEIGHT - 2, color);
        }

        boolean applyHovered = mouseX >= x + 12 && mouseX <= x + imageWidth - 12
                && mouseY >= y + APPLY_TOP && mouseY <= y + APPLY_BOTTOM;
        graphics.fill(x + 12, y + APPLY_TOP, x + imageWidth - 12, y + APPLY_BOTTOM,
                applyHovered ? 0xFF8B55B4 : 0xFF633A85);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(font, "VOID INFUSION CORE", 12, 13, 0xFFE5C8FF, false);
        graphics.drawString(font, "Select a program", 12, 38, 0xFFBBA5D0, false);
        int visible = Math.min(4, menu.programs().size());
        for (int i = 0; i < visible; i++) {
            ActionProgram program = menu.programs().get(i);
            graphics.drawString(font, (i == menu.selected() ? "> " : "  ") + program.name(),
                    17, 48 + i * ROW_HEIGHT, 0xFFE9E0F2, false);
        }
        graphics.drawCenteredString(font, "INFUSE HELD AETHERBLADE", imageWidth / 2, 126, 0xFFFFFFFF);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        double localX = mouseX - leftPos;
        double localY = mouseY - topPos;
        if (button == 0 && localX >= 12 && localX <= imageWidth - 12
                && localY >= LIST_TOP && localY < LIST_TOP + 4 * ROW_HEIGHT) {
            int index = (int) ((localY - LIST_TOP) / ROW_HEIGHT);
            if (index < menu.programs().size()) {
                minecraft.gameMode.handleInventoryButtonClick(menu.containerId, 100 + index);
                return true;
            }
        }
        if (button == 0 && localX >= 12 && localX <= imageWidth - 12
                && localY >= APPLY_TOP && localY <= APPLY_BOTTOM) {
            minecraft.gameMode.handleInventoryButtonClick(menu.containerId, 1);
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
