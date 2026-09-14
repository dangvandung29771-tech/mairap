package com.frontier.frontier.client.renderer;

import com.frontier.frontier.FrontierMod;
import com.frontier.frontier.block.MasterGrindstoneBlock;
import com.frontier.frontier.block.entity.MasterGrindstoneBlockEntity;
import com.frontier.frontier.client.FrontierModelLayers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

/** Renders the grindstone with a wheel that idles slowly and spins up during forging. */
public class MasterGrindstoneRenderer implements BlockEntityRenderer<MasterGrindstoneBlockEntity> {
    private static final ResourceLocation TEXTURE = FrontierMod.id("textures/entity/master_grindstone.png");

    private final ModelPart root;
    private final ModelPart wheel;

    public MasterGrindstoneRenderer(BlockEntityRendererProvider.Context context) {
        this.root = context.bakeLayer(FrontierModelLayers.GRINDSTONE);
        this.wheel = this.root.getChild("wheel");
    }

    @Override
    public void render(MasterGrindstoneBlockEntity blockEntity, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        poseStack.translate(0.5F, 0.0F, 0.5F);

        BlockState state = blockEntity.getBlockState();
        if (state.hasProperty(MasterGrindstoneBlock.FACING)) {
            Direction facing = state.getValue(MasterGrindstoneBlock.FACING);
            poseStack.mulPose(Axis.YP.rotationDegrees(facing.toYRot()));
        }

        poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));

        long time = blockEntity.getLevel() == null ? 0 : blockEntity.getLevel().getGameTime();
        float speed = blockEntity.isActive() ? 28.0F : 2.5F;
        this.wheel.xRot = ((time + partialTick) * speed) % 360.0F * ((float) Math.PI / 180.0F);

        this.root.render(poseStack, bufferSource.getBuffer(RenderType.entityCutoutNoCull(TEXTURE)),
                packedLight, packedOverlay);
        poseStack.popPose();
    }
}
