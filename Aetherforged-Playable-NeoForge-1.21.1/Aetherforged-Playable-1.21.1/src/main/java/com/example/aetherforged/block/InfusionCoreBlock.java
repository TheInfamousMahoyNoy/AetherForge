package com.example.aetherforged.block;

import com.example.aetherforged.client.InfusionCoreScreen;
import com.example.aetherforged.menu.InfusionCoreMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class InfusionCoreBlock extends Block {
    public InfusionCoreBlock() {
        super(BlockBehaviour.Properties.of().strength(4.0F).requiresCorrectToolForDrops().lightLevel(s -> 8));
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                                Player player, BlockHitResult hit) {
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            MenuProvider provider = new SimpleMenuProvider(
                (id, inventory, p) -> new InfusionCoreMenu(id, inventory, ContainerLevelAccess.create(level, pos)),
                Component.translatable("container.aetherforged.infusion_core"));
            serverPlayer.openMenu(provider);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
