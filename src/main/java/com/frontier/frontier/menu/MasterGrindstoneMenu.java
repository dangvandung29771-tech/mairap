package com.frontier.frontier.menu;

import com.frontier.frontier.block.entity.MasterGrindstoneBlockEntity;
import com.frontier.frontier.forge.ForgingLogic;
import com.frontier.frontier.init.FrontierMenus;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * Server-side container for the Master Grindstone: one weapon slot, one
 * whetstone slot, one crystal slot. The FORGE action itself is triggered by
 * a payload so the screen can show live before/after comparisons.
 */
public class MasterGrindstoneMenu extends AbstractContainerMenu {
    public final Container grindstone;
    public final net.minecraft.core.BlockPos pos;

    public MasterGrindstoneMenu(int containerId, Inventory playerInventory, MasterGrindstoneBlockEntity blockEntity) {
        this(containerId, playerInventory, (Container) blockEntity, blockEntity.getBlockPos());
    }

    public MasterGrindstoneMenu(int containerId, Inventory playerInventory, Container container) {
        this(containerId, playerInventory, container, net.minecraft.core.BlockPos.ZERO);
    }

    public MasterGrindstoneMenu(int containerId, Inventory playerInventory, Container container, net.minecraft.core.BlockPos pos) {
        super(FrontierMenus.MASTER_GRINDSTONE.get(), containerId);
        this.grindstone = container;
        this.pos = pos;
        container.startOpen(playerInventory.player);

        addSlot(new FilteredSlot(container, MasterGrindstoneBlockEntity.SLOT_WEAPON, 79, 25, SlotKind.WEAPON));
        addSlot(new FilteredSlot(container, MasterGrindstoneBlockEntity.SLOT_WHETSTONE, 52, 60, SlotKind.WHETSTONE));
        addSlot(new FilteredSlot(container, MasterGrindstoneBlockEntity.SLOT_CRYSTAL, 106, 60, SlotKind.CRYSTAL));

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 172 + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(playerInventory, col, 8 + col * 18, 230));
        }
    }

    public enum SlotKind {
        WEAPON, WHETSTONE, CRYSTAL
    }

    public static class FilteredSlot extends Slot {
        private final SlotKind kind;

        public FilteredSlot(Container container, int index, int x, int y, SlotKind kind) {
            super(container, index, x, y);
            this.kind = kind;
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return switch (kind) {
                case WEAPON -> ForgingLogic.isWeapon(stack);
                case WHETSTONE -> ForgingLogic.isWhetstone(stack);
                case CRYSTAL -> ForgingLogic.isCrystal(stack);
            };
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot.hasItem()) {
            ItemStack inSlot = slot.getItem();
            result = inSlot.copy();
            if (index < 3) {
                if (!moveItemStackTo(inSlot, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                boolean moved = false;
                if (ForgingLogic.isWeapon(inSlot)) {
                    moved = moveItemStackTo(inSlot, 0, 1, false);
                }
                if (!moved && ForgingLogic.isWhetstone(inSlot)) {
                    moved = moveItemStackTo(inSlot, 1, 2, false);
                }
                if (!moved && ForgingLogic.isCrystal(inSlot)) {
                    moved = moveItemStackTo(inSlot, 2, 3, false);
                }
                if (!moved && index < 30) {
                    moved = moveItemStackTo(inSlot, 30, 39, false);
                } else if (!moved) {
                    moved = moveItemStackTo(inSlot, 3, 30, false);
                }
                if (!moved) {
                    return ItemStack.EMPTY;
                }
            }
            if (inSlot.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            if (inSlot.getCount() == result.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(player, inSlot);
        }
        return result;
    }

    @Override
    public boolean stillValid(Player player) {
        return grindstone.stillValid(player);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        grindstone.stopOpen(player);
    }
}
