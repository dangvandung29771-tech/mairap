package com.frontier.frontier.relic;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/** Container view over the player's relic attachment, used by the menu. */
public class RelicInventory implements Container {
    private final Player player;
    private final RelicData data;

    public RelicInventory(Player player) {
        this.player = player;
        this.data = player.getData(com.frontier.frontier.init.FrontierAttachments.RELICS.get());
    }

    @Override
    public int getContainerSize() {
        return 2;
    }

    @Override
    public boolean isEmpty() {
        return data.getSlot(0).isEmpty() && data.getSlot(1).isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        return data.getSlot(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack stack = data.getSlot(slot);
        data.setSlot(slot, ItemStack.EMPTY);
        return stack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return removeItem(slot, 1);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        data.setSlot(slot, stack);
    }

    @Override
    public boolean stillValid(Player player) {
        return !player.isRemoved();
    }

    @Override
    public void setChanged() {
        player.setData(com.frontier.frontier.init.FrontierAttachments.RELICS.get(), data);
    }

    @Override
    public void clearContent() {
        data.setSlot(0, ItemStack.EMPTY);
        data.setSlot(1, ItemStack.EMPTY);
    }
}
