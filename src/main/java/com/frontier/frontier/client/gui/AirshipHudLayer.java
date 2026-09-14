package com.frontier.frontier.client.gui;

import com.frontier.frontier.FrontierMod;
import com.frontier.frontier.entity.AirshipEntity;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

/**
 * Compact flight instrument shown while piloting the pocket airship:
 * fuel gauge, altitude, speed and engine state.
 */
public class AirshipHudLayer implements net.minecraft.client.gui.LayeredDraw.Layer {
    private static final ResourceLocation TEXTURE = FrontierMod.id("textures/gui/airship_hud.png");

    @Override
    public void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.options.hideGui || mc.player.isSpectator()) {
            return;
        }
        if (!(mc.player.getVehicle() instanceof AirshipEntity airship)) {
            return;
        }

        int w = graphics.guiWidth();
        int h = graphics.guiHeight();
        int x = w / 2 + 100;
        int y = h - 92;

        RenderSystem.enableBlend();
        graphics.blit(TEXTURE, x, y, 0, 0, 92, 54, 128, 64);

        // Fuel gauge (8 of 48 pixels fill).
        int fuelPixels = (int) ((float) airship.getFuel() / AirshipEntity.MAX_FUEL * 36.0F);
        graphics.blit(TEXTURE, x + 30, y + 8, 0, 54, Math.max(0, fuelPixels), 5, 128, 64);

        var font = mc.font;
        graphics.drawString(font, Component.translatable("hud.frontier.fuel"), x + 6, y + 8, 0xFFE6D5A8, false);
        graphics.drawString(font, String.format("%.0f%%", (float) airship.getFuel() / AirshipEntity.MAX_FUEL * 100.0F),
                x + 70, y + 8, 0xFFE6D5A8, false);

        Vec3 velocity = airship.getDeltaMovement();
        double speed = Math.sqrt(velocity.x * velocity.x + velocity.z * velocity.z) * 20.0;
        graphics.drawString(font, Component.translatable("hud.frontier.altitude", (int) airship.getY()),
                x + 6, y + 22, 0xFFC8BFA8, false);
        graphics.drawString(font, Component.translatable("hud.frontier.speed", String.format("%.1f", speed)),
                x + 6, y + 33, 0xFFC8BFA8, false);

        // Engine lamp.
        boolean engine = airship.isEngineOn();
        graphics.blit(TEXTURE, x + 74, y + 30, engine ? 100 : 92, 54, 8, 8, 128, 64);
        graphics.drawString(font, Component.translatable(engine ? "hud.frontier.engine.on" : "hud.frontier.engine.off"),
                x + 30, y + 44, engine ? 0xFF9FD89F : 0xFFC25454, false);
        RenderSystem.disableBlend();
    }
}
