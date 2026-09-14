package com.frontier.frontier.entity.renderer;

import com.frontier.frontier.FrontierMod;
import com.frontier.frontier.client.FrontierModelLayers;
import com.frontier.frontier.entity.AirshipEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

/** Pocket airship: spinning propeller, breathing balloon, vibrating engine. */
public class AirshipRenderer extends EntityRenderer<AirshipEntity> {
    private static final ResourceLocation TEXTURE = FrontierMod.id("textures/entity/pocket_airship.png");

    private final ModelPart root;
    private final ModelPart propeller;
    private final ModelPart balloon;
    private final ModelPart engine;

    public AirshipRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 1.4F;
        this.root = context.bakeLayer(FrontierModelLayers.AIRSHIP);
        this.propeller = this.root.getChild("propeller");
        this.balloon = this.root.getChild("balloon");
        this.engine = this.root.getChild("engine");
    }

    @Override
    public ResourceLocation getTextureLocation(AirshipEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(AirshipEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        float yaw = Mth.lerp(partialTick, entity.yRotO, entity.getYRot());
        float hover = Mth.sin(entity.balloonPhase + partialTick * 0.03F) * 0.08F;

        poseStack.translate(0.0F, hover, 0.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - yaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));

        // Propeller spin (fast with engine, drifting without).
        this.propeller.zRot = entity.propellerPhase % (2.0F * (float) Math.PI);

        // Balloon breathing.
        float breath = 1.0F + Mth.sin(entity.balloonPhase) * 0.006F;
        this.balloon.xScale = breath;
        this.balloon.yScale = breath;

        // Engine vibration under power.
        if (entity.isEngineOn()) {
            float jitter = (entity.tickCount + partialTick) * 1.7F;
            this.engine.x = Mth.sin(jitter) * 0.15F;
            this.engine.y = Mth.cos(jitter * 1.3F) * 0.1F;
        } else {
            this.engine.x = 0;
            this.engine.y = 0;
        }

        this.root.render(poseStack, bufferSource.getBuffer(RenderType.entityCutoutNoCull(TEXTURE)),
                packedLight, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
