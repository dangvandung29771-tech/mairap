package com.frontier.frontier.client.gui;

import com.frontier.frontier.FrontierMod;
import com.frontier.frontier.menu.RelicMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

/**
 * The relic pouch: living portrait of the explorer, two relic sockets and a
 * parchment panel describing what each relic does.
 */
public class RelicScreen extends AbstractContainerScreen<RelicMenu> {
    private static final ResourceLocation TEXTURE = FrontierMod.id("textures/gui/relic_pouch.png");

    private static final int COLOR_INK = 0xFF3E2F1C;
    private static final int COLOR_GOLD = 0xFFE6C777;

    public RelicScreen(RelicMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 184;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        this.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 256, 256);

        int cx = this.leftPos + this.imageWidth / 2;
        graphics.drawString(this.font, Component.translatable("container.frontier.relics").withStyle(ChatFormatting.BOLD),
                cx - 30, this.topPos + 6, COLOR_GOLD, false);

        // Player portrait.
        if (this.minecraft.player != null) {
            InventoryScreen.renderEntityInInventory(graphics, this.leftPos + 26, this.topPos + 84, 28,
                    new com.mojang.math.Vector3f(0.0F, 0.0F, 0.0F), 225.0F, 0.0F, this.minecraft.player);
        }

        // Slot captions.
        graphics.drawCenteredString(this.font, Component.translatable("gui.frontier.relic.slot"),
                this.leftPos + 52, this.topPos + 54, 0xFF9A8F7C);
        graphics.drawCenteredString(this.font, Component.translatable("gui.frontier.relic.slot"),
                this.leftPos + 124, this.topPos + 54, 0xFF9A8F7C);

        // Active relics panel.
        List<ItemStack> equipped = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            ItemStack stack = this.menu.relics.getItem(i);
            if (!stack.isEmpty()) {
                equipped.add(stack);
            }
        }
        int y = this.topPos + 30;
        graphics.drawString(this.font, Component.translatable("gui.frontier.relics.active"), this.leftPos + 66, y, COLOR_INK, false);
        y += 12;
        if (equipped.isEmpty()) {
            graphics.drawString(this.font, Component.translatable("gui.frontier.relics.none").withStyle(ChatFormatting.ITALIC),
                    this.leftPos + 66, y, 0xFF8A7A60, false);
        } else {
            for (ItemStack stack : equipped) {
                graphics.drawString(this.font, "\u25C6 " + stack.getHoverName().getString(), this.leftPos + 66, y, COLOR_INK, false);
                y += 11;
                graphics.drawString(this.font, Component.translatable(stack.getDescriptionId() + ".effect").getString(),
                        this.leftPos + 72, y, 0xFF6B5A40, false);
                y += 14;
            }
        }
    }
}
