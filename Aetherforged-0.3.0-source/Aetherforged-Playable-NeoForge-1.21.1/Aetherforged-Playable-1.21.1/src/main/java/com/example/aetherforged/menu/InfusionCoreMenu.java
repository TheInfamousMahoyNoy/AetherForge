package com.example.aetherforged.menu;

import com.example.aetherforged.action.ActionProgram;
import com.example.aetherforged.action.ActionRegistry;
import com.example.aetherforged.item.InfusionData;
import com.example.aetherforged.registry.ModItems;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.network.chat.Component;
import java.util.List;

public class InfusionCoreMenu extends AbstractContainerMenu {
    private final ContainerLevelAccess access;
    private final DataSlot selected = DataSlot.standalone();

    public InfusionCoreMenu(int id, Inventory inventory, FriendlyByteBuf buf) {
        this(id, inventory, ContainerLevelAccess.NULL);
    }

    public InfusionCoreMenu(int id, Inventory inventory, ContainerLevelAccess access) {
        super(ModMenus.INFUSION_CORE.get(), id);
        this.access = access;
        addDataSlot(selected);
        for (int row = 0; row < 3; row++)
            for (int col = 0; col < 9; col++)
                addSlot(new Slot(inventory, col + row * 9 + 9, 8 + col * 18, 160 + row * 18));
        for (int col = 0; col < 9; col++)
            addSlot(new Slot(inventory, col, 8 + col * 18, 218));
    }

    public List<ActionProgram> programs() { return ActionRegistry.all(); }
    public int selected() { return selected.get(); }
    public void select(int index) { selected.set(Math.max(0, index)); }

    public boolean applySelected(Player player) {
        if (player.level().isClientSide) return false;
        ItemStack weapon = player.getMainHandItem();
        if (!weapon.is(ModItems.AETHERBLADE.get())) weapon = player.getOffhandItem();
        if (!weapon.is(ModItems.AETHERBLADE.get())) return false;
        List<ActionProgram> list = ActionRegistry.all();
        if (list.isEmpty() || selected.get() < 0 || selected.get() >= list.size()) return false;
        InfusionData.addProgram(weapon, list.get(selected.get()).id());
        return true;
    }

    @Override public boolean stillValid(Player player) {
        return access.evaluate((level, pos) -> player.distanceToSqr(
            pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 64, true);
    }

    @Override public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override public boolean clickMenuButton(Player player, int id) {
        if (id == 1) return applySelected(player);
        if (id >= 100 && id < 100 + ActionRegistry.all().size()) {
            select(id - 100);
            return true;
        }
        return false;
    }
}
