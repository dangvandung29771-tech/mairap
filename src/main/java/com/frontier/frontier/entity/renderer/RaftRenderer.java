package com.frontier.frontier.entity.renderer;

import com.frontier.frontier.FrontierMod;
import com.frontier.frontier.client.FrontierModelLayers;
import com.frontier.frontier.entity.RaftEntity;
import com.frontier.frontier.entity.model.RaftModel;
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

/** Bobbing raft with swinging oars and a swaying lantern. */
public class RaftRenderer extends EntityRenderer<RaftEntity> {
    private static final ResourceLocation TEXTURE = FrontierMod.id("textures/entity/large_raft.png");

    private final ModelPart root;
    private final ModelPart oarLeft;
    private final ModelPart oarRight;
    private final ModelPart lantern;

    public RaftRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.root = context.bakeLayer(FrontierModelLayers.RAFT);
        this.oarLeft = this.root.getChild("oar_left");
        this.oarRight = this.root.getChild("oar_right");
        this.lantern = this.root.getChild("lantern");
    }

    @Override
    public ResourceLocation getTextureLocation(RaftEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(RaftEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        float yaw = Mth.lerp(partialTick, entity.yRotO, entity.getYRot());
        float bob = Mth.sin(entity.bobPhase + partialTick * 0.05F) * 0.06F;
        float rock = Mth.sin(entity.bobPhase * 0.7F + partialTick * 0.05F) * 1.4F;

        poseStack.translate(0.0F, bob + 0.05F, 0.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - yaw));
        poseStack.mulPose(Axis.ZP.rotationDegrees(rock));
        poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));

        // Rowing animation: oars sweep with anticipation and follow-through.
        float oarSwing = entity.isMoving() ? Mth.sin(entity.oarPhase) * 0.55F : 0.15F;
        this.oarLeft.zRot = oarSwing;
        this.oarRight.zRot = -oarSwing;
        this.lantern.yRot = Mth.sin(entity.bobPhase * 0.9F) * 0.12F;

        this.root.render(poseStack, bufferSource.getBuffer(RenderType.entityCutoutNoCull(TEXTURE)),
                packedLight, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
