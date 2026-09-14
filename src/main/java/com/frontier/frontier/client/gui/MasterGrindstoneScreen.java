package com.frontier.frontier.client.gui;

import com.frontier.frontier.FrontierMod;
import com.frontier.frontier.forge.ForgingLogic;
import com.frontier.frontier.menu.MasterGrindstoneMenu;
import com.frontier.frontier.network.FrontierPayloads;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

/**
 * The Master Grindstone interface: dark stone frame, parchment comparison
 * panel, live BEFORE → AFTER stat readout and a FORGE action.
 */
public class MasterGrindstoneScreen extends AbstractContainerScreen<MasterGrindstoneMenu> {
    private static final ResourceLocation TEXTURE = FrontierMod.id("textures/gui/master_grindstone.png");

    private static final int COLOR_PARCHMENT_TEXT = 0xFF3E2F1C;
    private static final int COLOR_VALUE = 0xFF5A4A32;
    private static final int COLOR_IMPROVED = 0xFF1F6B2E;
    private static final int COLOR_GOLD = 0xFFE6C777;

    private ForgeButton forgeButton;
    private float slotHoverGlow = 0.0F;
    private Slot hoveredSlotCached;

    public MasterGrindstoneScreen(MasterGrindstoneMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 250;
        this.inventoryLabelY = 160;
        this.titleLabelX = 44;
    }

    @Override
    protected void init() {
        super.init();
        this.forgeButton = new ForgeButton(this.leftPos + 58, this.topPos + 146, this::onForgePressed);
        this.addRenderableWidget(this.forgeButton);
    }

    private void onForgePressed() {
        PacketDistributor.sendToServer(new FrontierPayloads.ForgeButtonPayload(this.menu.pos));
    }

    @Override
    public void containerTick() {
        super.containerTick();
        boolean hovered = this.hoveredSlot != null && this.hoveredSlot.isActive();
        if (hovered) {
            this.slotHoverGlow = Math.min(1.0F, this.slotHoverGlow + 0.2F);
            this.hoveredSlotCached = this.hoveredSlot;
        } else {
            this.slotHoverGlow = Math.max(0.0F, this.slotHoverGlow - 0.2F);
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        this.renderTooltip(graphics, mouseX, mouseY);

        ForgingLogic.Result preview = this.preview();
        if (!preview.ok() && !preview.error().getString().isEmpty()
                && this.hasAnyInput() && mouseX > this.leftPos + 8 && mouseX < this.leftPos + 168
                && mouseY > this.topPos + 90 && mouseY < this.topPos + 144) {
            graphics.renderTooltip(this.font, preview.error(), mouseX, mouseY);
        }
    }

    private boolean hasAnyInput() {
        return !this.menu.grindstone.getItem(0).isEmpty()
                || !this.menu.grindstone.getItem(1).isEmpty()
                || !this.menu.grindstone.getItem(2).isEmpty();
    }

    private ForgingLogic.Result preview() {
        return ForgingLogic.tryForge(
                this.menu.grindstone.getItem(MasterGrindstoneMenuSlot.WEAPON),
                this.menu.grindstone.getItem(MasterGrindstoneMenuSlot.WHETSTONE),
                this.menu.grindstone.getItem(MasterGrindstoneMenuSlot.CRYSTAL));
    }

    private static final class MasterGrindstoneMenuSlot {
        static final int WEAPON = 0;
        static final int WHETSTONE = 1;
        static final int CRYSTAL = 2;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 256, 256);

        // Smooth golden highlight on the hovered slot.
        if (this.slotHoverGlow > 0.01F && this.hoveredSlotCached != null) {
            int x = this.leftPos + this.hoveredSlotCached.x;
            int y = this.topPos + this.hoveredSlotCached.y;
            int alpha = (int) (90 * this.slotHoverGlow) << 24;
            graphics.fill(x - 1, y - 1, x + 17, y + 17, alpha | 0xE6C777);
            graphics.fill(x, y, x + 16, y + 16, 0x00000000);
        }

        int cx = this.leftPos + this.imageWidth / 2;
        graphics.drawString(this.font, Component.translatable("container.frontier.master_grindstone").withStyle(ChatFormatting.BOLD),
                cx - 60, this.topPos + 8, COLOR_GOLD, false);

        // Slot captions.
        graphics.drawString(this.font, Component.translatable("gui.frontier.weapon"), this.leftPos + 100, this.topPos + 29, 0xFF9A8F7C, false);
        graphics.drawString(this.font, Component.translatable("gui.frontier.whetstone"), this.leftPos + 24, this.topPos + 73, 0xFF9A8F7C, false);
        graphics.drawString(this.font, Component.translatable("gui.frontier.crystal"), this.leftPos + 118, this.topPos + 73, 0xFF9A8F7C, false);

        this.renderComparison(graphics);
    }

    private void renderComparison(GuiGraphics graphics) {
        ItemStack weapon = this.menu.grindstone.getItem(0);
        ForgingLogic.Result preview = this.preview();
        ForgingLogic.Stats before = ForgingLogic.statsOf(weapon);
        ForgingLogic.Stats after = ForgingLogic.statsOf(preview.result());
        boolean hasChange = preview.ok();

        graphics.drawString(this.font, Component.translatable("gui.frontier.before"), this.leftPos + 30, this.topPos + 95, COLOR_PARCHMENT_TEXT, false);
        graphics.drawString(this.font, Component.translatable("gui.frontier.after"), this.leftPos + 106, this.topPos + 95, COLOR_PARCHMENT_TEXT, false);
        graphics.drawString(this.font, "\u2192", this.leftPos + 86, this.topPos + 95, COLOR_PARCHMENT_TEXT, false);

        drawStatRow(graphics, "gui.frontier.stat.damage", format(before.damage()), format(hasChange ? after.damage() : before.damage()), hasChange && after.damage() > before.damage(), 108);
        drawStatRow(graphics, "gui.frontier.stat.crit", pct(before.crit()), pct(hasChange ? after.crit() : before.crit()), hasChange && after.crit() > before.crit(), 118);
        drawStatRow(graphics, "gui.frontier.stat.reach", format2(before.reach()), format2(hasChange ? after.reach() : before.reach()), hasChange && after.reach() > before.reach(), 128);
        drawStatRow(graphics, "gui.frontier.stat.durability", String.valueOf(before.durability()), String.valueOf(hasChange ? after.durability() : before.durability()), hasChange && after.durability() > before.durability(), 138);

        if (!preview.ok() && this.hasAnyInput()) {
            graphics.drawCenteredString(this.font, preview.error(), this.leftPos + 88, this.topPos + 150, 0xFFC25454);
        } else if (preview.ok() && this.hasAnyInput()) {
            graphics.drawCenteredString(this.font, Component.translatable("gui.frontier.ready"), this.leftPos + 88, this.topPos + 150, 0xFF7FA86B);
        }

        this.forgeButton.active = preview.ok();
    }

    private void drawStatRow(GuiGraphics graphics, String labelKey, String before, String after, boolean improved, int y) {
        graphics.drawString(this.font, Component.translatable(labelKey), this.leftPos + 14, this.topPos + y, COLOR_PARCHMENT_TEXT, false);
        graphics.drawString(this.font, before, this.leftPos + 46, this.topPos + y, COLOR_VALUE, false);
        graphics.drawString(this.font, "\u2192", this.leftPos + 86, this.topPos + y, COLOR_PARCHMENT_TEXT, false);
        graphics.drawString(this.font, after, this.leftPos + 112, this.topPos + y, improved ? COLOR_IMPROVED : COLOR_VALUE, false);
        if (improved) {
            graphics.drawString(this.font, "+", this.leftPos + 148, this.topPos + y, COLOR_IMPROVED, false);
        }
    }

    private static String format(float f) {
        return String.format("%.1f", f);
    }

    private static String format2(float f) {
        return String.format("%.2f", f);
    }

    private static String pct(float f) {
        return String.format("%.0f%%", f);
    }
}
