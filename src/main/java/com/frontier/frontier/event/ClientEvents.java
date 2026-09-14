package com.frontier.frontier.event;

import com.frontier.frontier.FrontierMod;
import com.frontier.frontier.entity.AirshipEntity;
import com.frontier.frontier.forge.ForgedData;
import com.frontier.frontier.forge.ForgedProperty;
import com.frontier.frontier.forge.ForgingLogic;
import com.frontier.frontier.init.FrontierComponents;
import com.frontier.frontier.network.FrontierPayloads;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleTypes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

/** Client game-bus events: relic key, airship input, subtle forged-weapon aura. */
@EventBusSubscriber(modid = FrontierMod.MODID, value = Dist.CLIENT)
public final class ClientEvents {
    public static final KeyMapping RELIC_KEY = new KeyMapping(
            "key.frontier.open_relics", InputConstants.KEY_R, "key.categories.frontier");

    private ClientEvents() {
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) {
            return;
        }

        while (RELIC_KEY.consumeClick()) {
            if (mc.screen == null) {
                PacketDistributor.sendToServer(new FrontierPayloads.OpenRelicPayload());
            }
        }

        if (player.getVehicle() instanceof AirshipEntity) {
            sendAirshipInput(mc, player);
        }

        if (player.tickCount % 14 == 0) {
            forgedWeaponAura(player);
        }
    }

    private static void sendAirshipInput(Minecraft mc, LocalPlayer player) {
        float forward = 0;
        if (mc.options.keyUp.isDown()) forward += 1.0F;
        if (mc.options.keyDown.isDown()) forward -= 1.0F;
        float turn = 0;
        if (mc.options.keyLeft.isDown()) turn += 1.0F;
        if (mc.options.keyRight.isDown()) turn -= 1.0F;
        boolean ascend = mc.options.keyJump.isDown();
        boolean descend = mc.options.keyShift.isDown();
        PacketDistributor.sendToServer(new FrontierPayloads.AirshipInputPayload(forward, turn, ascend, descend));
    }

    /** Restrained elemental hints on forged blades — one particle, rarely. */
    private static void forgedWeaponAura(LocalPlayer player) {
        ForgedData data = player.getMainHandItem().get(FrontierComponents.FORGED.get());
        if (data == null || data.isEmpty()) {
            return;
        }
        ForgedProperty dominant = data.dominant();
        if (dominant == null || player.level().random.nextFloat() > 0.4F) {
            return;
        }
        double x = player.getX() + player.getLookAngle().x * 0.8;
        double y = player.getEyeY() - 0.35 + player.getLookAngle().y * 0.8;
        double z = player.getZ() + player.getLookAngle().z * 0.8;
        switch (dominant) {
            case BLOOD -> player.level().addParticle(ParticleTypes.DAMAGE_INDICATOR, x, y, z, 0, 0.01, 0);
            case VOID -> player.level().addParticle(ParticleTypes.PORTAL, x, y, z, 0, 0.01, 0);
            case PRECISION -> player.level().addParticle(ParticleTypes.END_ROD, x, y, z, 0, 0.005, 0);
            case SHARP -> player.level().addParticle(ParticleTypes.CRIT, x, y, z, 0, 0.01, 0);
            default -> {
            }
        }
    }
}
