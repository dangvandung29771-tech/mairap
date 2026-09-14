package com.frontier.frontier.client.renderer;

import com.frontier.frontier.FrontierMod;
import com.frontier.frontier.block.SprinklerBlock;
import com.frontier.frontier.block.entity.SprinklerBlockEntity;
import com.frontier.frontier.client.FrontierModelLayers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;

/** Rotating sprinkler head that sprays gentle water arcs while active. */
public class SprinklerRenderer implements BlockEntityRenderer<SprinklerBlockEntity> {
    private static final ResourceLocation TEXTURE = FrontierMod.id("textures/entity/sprinkler.png");

    private final ModelPart root;
    private final ModelPart head;

    public SprinklerRenderer(BlockEntityRendererProvider.Context context) {
        this.root = context.bakeLayer(FrontierModelLayers.SPRINKLER);
        this.head = this.root.getChild("head");
    }

    @Override
    public void render(SprinklerBlockEntity blockEntity, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        boolean active = blockEntity.isActive();

        poseStack.pushPose();
        poseStack.translate(0.5F, 0.0F, 0.5F);
        poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));

        long time = blockEntity.getLevel() == null ? 0 : blockEntity.getLevel().getGameTime();
        this.head.yRot = active ? ((time + partialTick) * 24.0F) % 360.0F * ((float) Math.PI / 180.0F) : 0.0F;

        this.root.render(poseStack, bufferSource.getBuffer(RenderType.entityCutoutNoCull(TEXTURE)),
                packedLight, packedOverlay);
        poseStack.popPose();

        // Subtle water arcs — only while active, throttled per frame.
        if (active && blockEntity.getLevel() instanceof ClientLevel clientLevel) {
            double angle = (this.head.yRot / (float) Math.PI * 180.0F) * (float) Math.PI / 180.0F;
            for (int i = 0; i < 2; i++) {
                if (clientLevel.random.nextInt(3) != 0) {
                    continue;
                }
                double a = angle + i * Math.PI + clientLevel.random.nextFloat() * 0.4;
                double x = blockEntity.getBlockPos().getX() + 0.5 + Math.cos(a) * 0.35;
                double z = blockEntity.getBlockPos().getZ() + 0.5 + Math.sin(a) * 0.35;
                clientLevel.addParticle(ParticleTypes.SPLASH,
                        x, blockEntity.getBlockPos().getY() + 0.78, z,
                        Math.cos(a) * 0.12, 0.09, Math.sin(a) * 0.12);
            }
        }
    }
}
