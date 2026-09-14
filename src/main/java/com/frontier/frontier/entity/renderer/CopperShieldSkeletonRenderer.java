package com.frontier.frontier.entity.renderer;

import com.frontier.frontier.FrontierMod;
import com.frontier.frontier.client.FrontierModelLayers;
import com.frontier.frontier.entity.CopperShieldSkeleton;
import com.frontier.frontier.entity.model.CopperShieldSkeletonModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

/** Renders the shield skeleton, including its death fall. */
public class CopperShieldSkeletonRenderer extends EntityRenderer<CopperShieldSkeleton> {
    private static final ResourceLocation TEXTURE =
            FrontierMod.id("textures/entity/copper_shield_skeleton.png");

    private final CopperShieldSkeletonModel model;

    public CopperShieldSkeletonRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.5F;
        this.model = new CopperShieldSkeletonModel(context.bakeLayer(FrontierModelLayers.COPPER_SHIELD_SKELETON));
    }

    @Override
    public ResourceLocation getTextureLocation(CopperShieldSkeleton entity) {
        return TEXTURE;
    }

    @Override
    public void render(CopperShieldSkeleton entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        float yaw = Mth.rotLerp(partialTick, entity.yBodyRotO, entity.yBodyRot);
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - yaw));

        // Death fall.
        float deathTime = (float) entity.deathTime + partialTick;
        if (deathTime > 0.0F) {
            float fall = Math.min(deathTime / 20.0F, 1.0F);
            poseStack.mulPose(Axis.ZP.rotationDegrees(fall * 90.0F));
        }

        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.translate(0.0F, -1.5F, 0.0F);

        float headYaw = Mth.rotLerp(partialTick, entity.yHeadRotO, entity.yHeadRot) - yaw;
        float headPitch = Mth.lerp(partialTick, entity.xRotO, entity.getXRot());
        this.model.setupAnim(entity, entity.walkAnimation.position(partialTick),
                entity.walkAnimation.speed(partialTick), entity.tickCount + partialTick, headYaw, headPitch);

        this.model.renderToBuffer(poseStack, bufferSource.getBuffer(RenderType.entityCutoutNoCull(TEXTURE)),
                packedLight, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
