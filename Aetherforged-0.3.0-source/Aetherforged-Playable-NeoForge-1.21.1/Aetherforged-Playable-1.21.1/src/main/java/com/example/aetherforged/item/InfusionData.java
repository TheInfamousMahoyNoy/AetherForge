package com.example.aetherforged.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import java.util.*;

public final class InfusionData {
    private static final String KEY = "aetherforged_infusion";
    private InfusionData() {}

    public static List<String> getPrograms(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        if (data == null) return List.of();
        CompoundTag root = data.copyTag();
        if (!root.contains(KEY, CompoundTag.TAG_COMPOUND)) return List.of();
        CompoundTag infusion = root.getCompound(KEY);
        if (!infusion.contains("programs", CompoundTag.TAG_LIST)) return List.of();
        ListTag list = infusion.getList("programs", CompoundTag.TAG_STRING);
        List<String> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) result.add(list.getString(i));
        return List.copyOf(result);
    }

    public static void addProgram(ItemStack stack, String id) {
        Set<String> ids = new TreeSet<>(getPrograms(stack));
        ids.add(id);
        setPrograms(stack, ids);
    }

    public static void removeProgram(ItemStack stack, String id) {
        Set<String> ids = new TreeSet<>(getPrograms(stack));
        ids.remove(id);
        setPrograms(stack, ids);
    }

    private static void setPrograms(ItemStack stack, Collection<String> ids) {
        CompoundTag root = new CompoundTag();
        CustomData old = stack.get(DataComponents.CUSTOM_DATA);
        if (old != null) root = old.copyTag();
        ListTag list = new ListTag();
        for (String id : new TreeSet<>(ids)) list.add(StringTag.valueOf(id));
        CompoundTag infusion = new CompoundTag();
        infusion.put("programs", list);
        root.put(KEY, infusion);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(root));
    }
}
