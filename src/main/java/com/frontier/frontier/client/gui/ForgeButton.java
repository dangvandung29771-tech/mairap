package com.frontier.frontier.client.gui;

import com.frontier.frontier.FrontierMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

/** Dark-iron FORGE button with a subtle gold edge, drawn from the GUI atlas. */
public class ForgeButton extends AbstractWidget {
    private static final ResourceLocation TEXTURE = FrontierMod.id("textures/gui/master_grindstone.png");

    public ForgeButton(int x, int y, Runnable onPress) {
        super(x, y, 60, 20, Component.translatable("gui.frontier.forge"));
        this.active = true;
        this.onPressRunnable = onPress;
    }

    private final Runnable onPressRunnable;

    @Override
    public void onClick(double mouseX, double mouseY) {
        super.onClick(mouseX, mouseY);
        if (this.active) {
            this.onPressRunnable.run();
        }
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        boolean hovered = this.isHoveredOrFocused();
        int v = !this.active ? 20 : hovered ? 40 : 0;
        graphics.blit(TEXTURE, this.getX(), this.getY(), 176, v, this.width, this.height, 256, 256);

        int color = !this.active ? 0xFF7A7064 : hovered ? 0xFFF2E3B8 : 0xFFE6D5A8;
        graphics.drawCenteredString(net.minecraft.client.Minecraft.getInstance().font,
                this.getMessage(), this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2 + 1, color);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {
        this.defaultButtonNarrationText(output);
    }
}
