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

    /** Suppress the default container labels - we draw our own themed title. */
    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 256, 256);

        // Themed title, centered in the title bar.
        Component title = Component.translatable("container.frontier.relics").withStyle(ChatFormatting.BOLD);
        graphics.drawString(this.font, title,
                this.leftPos + this.imageWidth / 2 - this.font.width(title) / 2, this.topPos + 7, COLOR_GOLD, false);

        // Living portrait: vanilla uses a Z-flip so the explorer faces the viewer.
        if (this.minecraft.player != null) {
            InventoryScreen.renderEntityInInventory(graphics, this.leftPos + 28, this.topPos + 82, 30,
                    new org.joml.Vector3f(0.0F, 0.0F, 0.0F),
                    new org.joml.Quaternionf().rotateZ((float) Math.PI),
                    null, this.minecraft.player);
        }

        // Socket captions.
        graphics.drawCenteredString(this.font, Component.translatable("gui.frontier.relic.slot"),
                this.leftPos + 69, this.topPos + 50, 0xFF9A8F7C);
        graphics.drawCenteredString(this.font, Component.translatable("gui.frontier.relic.slot"),
                this.leftPos + 109, this.topPos + 50, 0xFF9A8F7C);

        // Active relics parchment panel: header + one compact line per relic.
        graphics.drawString(this.font, Component.translatable("gui.frontier.relics.active"),
                this.leftPos + 64, this.topPos + 66, COLOR_INK, false);
        List<ItemStack> equipped = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            ItemStack stack = this.menu.relics.getItem(i);
            if (!stack.isEmpty()) {
                equipped.add(stack);
            }
        }
        if (equipped.isEmpty()) {
            graphics.drawString(this.font, Component.translatable("gui.frontier.relics.none").withStyle(ChatFormatting.ITALIC),
                    this.leftPos + 64, this.topPos + 78, 0xFF8A7A60, false);
        } else {
            int y = this.topPos + 78;
            for (ItemStack stack : equipped) {
                String effect = Component.translatable(stack.getDescriptionId() + ".effect").getString();
                String line = "\u25C6 " + stack.getHoverName().getString() + " \u00B7 " + effect;
                graphics.drawString(this.font, line, this.leftPos + 64, y, 0xFF6B5A40, false);
                y += 11;
            }
        }
    }
}
