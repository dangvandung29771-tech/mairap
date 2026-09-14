package com.frontier.frontier.entity.model;

import com.frontier.frontier.entity.CopperShieldSkeleton;
import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

/**
 * Skeletal guardian with corroded copper shield and worn armor plating.
 * Vanilla-proportioned (32px tall), keyframe-animated.
 */
public class CopperShieldSkeletonModel extends HierarchicalModel<CopperShieldSkeleton> {
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart armor;
    private final ModelPart armLeft;
    private final ModelPart armRight;
    private final ModelPart legLeft;
    private final ModelPart legRight;
    private final ModelPart shield;

    public CopperShieldSkeletonModel(ModelPart root) {
        this.root = root;
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.armor = root.getChild("armor");
        this.armLeft = root.getChild("arm_left");
        this.armRight = root.getChild("arm_right");
        this.legLeft = root.getChild("leg_left");
        this.legRight = root.getChild("leg_right");
        this.shield = this.armLeft.getChild("shield");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("head", CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F)
                        .texOffs(32, 0).addBox(-4.5F, -8.5F, -4.5F, 9.0F, 4.0F, 9.0F), // worn helmet
                PartPose.offset(0.0F, 0.0F, 0.0F));

        root.addOrReplaceChild("body", CubeListBuilder.create()
                        .texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        root.addOrReplaceChild("armor", CubeListBuilder.create()
                        .texOffs(16, 32).addBox(-4.5F, -0.5F, -2.5F, 9.0F, 7.0F, 5.0F, new CubeDeformation(0.1F))
                        .texOffs(40, 16).addBox(-4.5F, 0.0F, -3.0F, 4.0F, 4.0F, 1.0F), // pauldron
                PartPose.offset(0.0F, 0.0F, 0.0F));

        root.addOrReplaceChild("arm_left", CubeListBuilder.create()
                        .texOffs(40, 24).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F),
                PartPose.offset(5.0F, 2.0F, 0.0F));

        root.addOrReplaceChild("arm_right", CubeListBuilder.create()
                        .texOffs(40, 24).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, true),
                PartPose.offset(-5.0F, 2.0F, 0.0F));

        root.addOrReplaceChild("leg_left", CubeListBuilder.create()
                        .texOffs(0, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F),
                PartPose.offset(2.0F, 12.0F, 0.0F));

        root.addOrReplaceChild("leg_right", CubeListBuilder.create()
                        .texOffs(0, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F, true),
                PartPose.offset(-2.0F, 12.0F, 0.0F));

        // Copper shield strapped to the left arm.
        root.getChild("arm_left").addOrReplaceChild("shield", CubeListBuilder.create()
                        .texOffs(48, 32).addBox(-6.0F, -8.0F, -1.0F, 12.0F, 16.0F, 2.0F)
                        .texOffs(52, 50).addBox(-2.0F, -3.0F, -2.5F, 4.0F, 6.0F, 2.0F), // boss
                PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, 0.0F, (float) Math.PI / 2.0F, 0.0F));

        return LayerDefinition.create(mesh, 80, 80);
    }

    public static final AnimationDefinition IDLE = AnimationDefinition.Builder.withLength(2.0F).looping()
            .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[]{
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0, 0, 0), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(2.0F, 0, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.0F, KeyframeAnimations.degreeVec(0, 0, 0), AnimationChannel.Interpolations.CATMULLROM)
            }))
            .addAnimation("arm_left", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[]{
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-4.0F, 0, 0), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(3.0F, 0, 0), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.0F, KeyframeAnimations.degreeVec(-4.0F, 0, 0), AnimationChannel.Interpolations.CATMULLROM)
            }))
            .build();

    public static final AnimationDefinition ATTACK = AnimationDefinition.Builder.withLength(0.55F)
            .addAnimation("arm_right", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[]{
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0, 0, 0), AnimationChannel.Interpolations.CATMULLROM),
                    // anticipation: draw back
                    new Keyframe(0.16F, KeyframeAnimations.degreeVec(-135.0F, 0, -14.0F), AnimationChannel.Interpolations.CATMULLROM),
                    // impact: whip forward
                    new Keyframe(0.28F, KeyframeAnimations.degreeVec(-35.0F, 0, 8.0F), AnimationChannel.Interpolations.CATMULLROM),
                    // follow-through
                    new Keyframe(0.38F, KeyframeAnimations.degreeVec(-20.0F, 0, 4.0F), AnimationChannel.Interpolations.CATMULLROM),
                    // recovery
                    new Keyframe(0.55F, KeyframeAnimations.degreeVec(0, 0, 0), AnimationChannel.Interpolations.CATMULLROM)
            }))
            .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[]{
                    new Keyframe(0.10F, KeyframeAnimations.degreeVec(0, -12.0F, 0), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.30F, KeyframeAnimations.degreeVec(4.0F, 14.0F, 0), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.55F, KeyframeAnimations.degreeVec(0, 0, 0), AnimationChannel.Interpolations.CATMULLROM)
            }))
            .build();

    public static final AnimationDefinition BLOCK = AnimationDefinition.Builder.withLength(0.9F)
            .addAnimation("arm_left", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[]{
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0, 0, 0), AnimationChannel.Interpolations.CATMULLROM),
                    // anticipation dip, then snap up past the guard line
                    new Keyframe(0.10F, KeyframeAnimations.degreeVec(10.0F, 0, 4.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.26F, KeyframeAnimations.degreeVec(-92.0F, 6.0F, -12.0F), AnimationChannel.Interpolations.CATMULLROM),
                    // settle into the held guard pose; final keyframe is held
                    new Keyframe(0.42F, KeyframeAnimations.degreeVec(-82.0F, 2.0F, -8.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.9F, KeyframeAnimations.degreeVec(-80.0F, 0, -8.0F), AnimationChannel.Interpolations.CATMULLROM)
            }))
            .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[]{
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0, 0, 0), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.26F, KeyframeAnimations.degreeVec(6.0F, -14.0F, 0), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.9F, KeyframeAnimations.degreeVec(3.0F, -8.0F, 0), AnimationChannel.Interpolations.CATMULLROM)
            }))
            .build();

    public static final AnimationDefinition HIT = AnimationDefinition.Builder.withLength(0.4F)
            .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[]{
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0, 0, 0), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.10F, KeyframeAnimations.degreeVec(-10.0F, 0, -3.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4F, KeyframeAnimations.degreeVec(0, 0, 0), AnimationChannel.Interpolations.CATMULLROM)
            }))
            .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[]{
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0, 0, 0), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.08F, KeyframeAnimations.degreeVec(-14.0F, 6.0F, 0), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4F, KeyframeAnimations.degreeVec(0, 0, 0), AnimationChannel.Interpolations.CATMULLROM)
            }))
            .build();

    @Override
    public void setupAnim(CopperShieldSkeleton entity, float limbSwing, float limbSwingAmount, float ageInTicks,
                          float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);

        this.head.yRot = netHeadYaw * Mth.DEG_TO_RAD;
        this.head.xRot = headPitch * Mth.DEG_TO_RAD;

        // Walk cycle.
        this.legRight.xRot = Mth.cos(limbSwing * 0.6662F) * 1.2F * limbSwingAmount;
        this.legLeft.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.2F * limbSwingAmount;

        this.animate(entity.idleState, IDLE, ageInTicks, 1.0F);
        this.animate(entity.attackState, ATTACK, ageInTicks, 1.0F);
        this.animate(entity.blockState, BLOCK, ageInTicks, 1.0F);
        this.animate(entity.hitState, HIT, ageInTicks, 1.0F);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}
