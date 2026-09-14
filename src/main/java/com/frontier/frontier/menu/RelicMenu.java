package com.frontier.frontier.menu;

import com.frontier.frontier.init.FrontierMenus;
import com.frontier.frontier.item.RelicItem;
import com.frontier.frontier.relic.RelicInventory;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/** Two relic slots backed by the player's relic attachment. */
public class RelicMenu extends AbstractContainerMenu {
    public final Container relics;

    public RelicMenu(int containerId, Inventory playerInventory) {
        super(FrontierMenus.RELIC_MENU.get(), containerId);
        this.relics = new RelicInventory(playerInventory.player);
        relics.startOpen(playerInventory.player);

        addSlot(new RelicSlot(relics, 0, 60, 30));
        addSlot(new RelicSlot(relics, 1, 100, 30));

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 102 + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(playerInventory, col, 8 + col * 18, 160));
        }
    }

    public static class RelicSlot extends Slot {
        public RelicSlot(Container container, int index, int x, int y) {
            super(container, index, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return stack.getItem() instanceof RelicItem;
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot.hasItem()) {
            ItemStack inSlot = slot.getItem();
            result = inSlot.copy();
            if (index < 2) {
                if (!moveItemStackTo(inSlot, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (inSlot.getItem() instanceof RelicItem) {
                if (!moveItemStackTo(inSlot, 0, 2, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index < 29) {
                if (!moveItemStackTo(inSlot, 29, 38, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!moveItemStackTo(inSlot, 2, 29, false)) {
                return ItemStack.EMPTY;
            }
            if (inSlot.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return result;
    }

    @Override
    public boolean stillValid(Player player) {
        return relics.stillValid(player);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        relics.stopOpen(player);
    }
}
