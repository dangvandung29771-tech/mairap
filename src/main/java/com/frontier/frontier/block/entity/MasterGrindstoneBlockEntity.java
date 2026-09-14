package com.frontier.frontier.block.entity;

import com.frontier.frontier.block.MasterGrindstoneBlock;
import com.frontier.frontier.forge.ForgingLogic;
import com.frontier.frontier.init.FrontierBlockEntities;
import com.frontier.frontier.init.FrontierSounds;
import com.frontier.frontier.menu.MasterGrindstoneMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Holds the grindstone's three input slots and drives the forging cycle:
 * wheel spins up, sparks fly, the finished weapon replaces the input.
 */
public class MasterGrindstoneBlockEntity extends BlockEntity implements MenuProvider, net.minecraft.world.Container {
    public static final int SLOT_WEAPON = 0;
    public static final int SLOT_WHETSTONE = 1;
    public static final int SLOT_CRYSTAL = 2;
    public static final int FORGE_DURATION = 46;

    private final NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);
    private int forgeTicks = 0;
    /** Monotonically increasing wheel angle (degrees) used by the renderer. */
    public float wheelAngle;
    private float wheelSpeed;

    public MasterGrindstoneBlockEntity(BlockPos pos, BlockState state) {
        super(FrontierBlockEntities.MASTER_GRINDSTONE.get(), pos, state);
    }

    public NonNullList<ItemStack> getItems() {
        return items;
    }

    public int getForgeTicks() {
        return forgeTicks;
    }

    public boolean isActive() {
        return forgeTicks > 0;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.frontier.master_grindstone");
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new MasterGrindstoneMenu(containerId, playerInventory, this);
    }

    /** Called from the FORGE button payload. Server-authoritative. */
    public void tryForge(ServerPlayer player) {
        if (level == null || forgeTicks > 0) {
            return;
        }
        ItemStack weapon = items.get(SLOT_WEAPON);
        ItemStack whetstone = items.get(SLOT_WHETSTONE);
        ItemStack crystal = items.get(SLOT_CRYSTAL);
        ForgingLogic.Result result = ForgingLogic.tryForge(weapon, whetstone, crystal);
        if (!result.ok()) {
            player.displayClientMessage(result.error(), true);
            return;
        }

        items.set(SLOT_WEAPON, result.result());
        if (!whetstone.isEmpty()) {
            whetstone.shrink(1);
        }
        if (!crystal.isEmpty()) {
            crystal.shrink(1);
        }
        forgeTicks = FORGE_DURATION;
        setBlockStateFlag(true);
        level.playSound(null, worldPosition, FrontierSounds.GRINDSTONE_GRIND.get(), SoundSource.BLOCKS, 0.9F, 1.0F);
        setChanged();
        syncToClients();
        if (player.containerMenu instanceof MasterGrindstoneMenu menu) {
            menu.broadcastChanges();
        }
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, MasterGrindstoneBlockEntity entity) {
        if (entity.forgeTicks > 0) {
            entity.forgeTicks--;
            if (level instanceof ServerLevel serverLevel) {
                spawnSparks(serverLevel, pos, level.random, entity.forgeTicks > FORGE_DURATION / 2);
            }
            if (entity.forgeTicks == 0) {
                entity.setBlockStateFlag(false);
                level.playSound(null, pos, FrontierSounds.FORGE_COMPLETE.get(), SoundSource.BLOCKS, 0.8F, 1.0F);
                level.playSound(null, pos, FrontierSounds.GRINDSTONE_IMPACT.get(), SoundSource.BLOCKS, 0.6F, 1.5F);
                entity.setChanged();
                entity.syncToClients();
            }
        }
    }

    private static void spawnSparks(ServerLevel level, BlockPos pos, RandomSource random, boolean intense) {
        int count = intense ? 3 : 1;
        for (int i = 0; i < count; i++) {
            level.sendParticles(ParticleTypes.CRIT,
                    pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.7,
                    pos.getY() + 1.02,
                    pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.7,
                    1, (random.nextDouble() - 0.5) * 0.15, random.nextDouble() * 0.12, (random.nextDouble() - 0.5) * 0.15, 0.05);
            if (random.nextInt(4) == 0) {
                level.sendParticles(ParticleTypes.SMOKE,
                        pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.5,
                        pos.getY() + 1.05,
                        pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.5,
                        1, 0, 0.03, 0, 0.01);
            }
        }
    }

    private void setBlockStateFlag(boolean active) {
        if (level != null && getBlockState().hasProperty(MasterGrindstoneBlock.ACTIVE)) {
            level.setBlock(worldPosition, getBlockState().setValue(MasterGrindstoneBlock.ACTIVE, active), 3);
        }
    }

    private void syncToClients() {
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        ContainerHelper.saveAllItems(tag, items, registries);
        tag.putInt("ForgeTicks", forgeTicks);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        ContainerHelper.loadAllItems(tag, items, registries);
        forgeTicks = tag.getInt("ForgeTicks");
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("ForgeTicks", forgeTicks);
        return tag;
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    // === Container implementation ===
    @Override
    public int getContainerSize() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        return items.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getItem(int slot) {
        return items.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        return ContainerHelper.removeItem(items, slot, amount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(items, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        items.set(slot, stack);
    }

    @Override
    public boolean stillValid(net.minecraft.world.entity.player.Player player) {
        return net.minecraft.world.Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        items.clear();
    }
}
