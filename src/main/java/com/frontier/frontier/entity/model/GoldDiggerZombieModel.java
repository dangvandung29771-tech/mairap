package com.frontier.frontier.entity.model;

import com.frontier.frontier.entity.GoldDiggerZombie;
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
 * Undead prospector: hard hat, worn overalls, pack, and a pickaxe it still
 * knows how to swing. Keyframe-animated dig and attack cycles.
 */
public class GoldDiggerZombieModel extends HierarchicalModel<GoldDiggerZombie> {
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart armLeft;
    private final ModelPart armRight;
    private final ModelPart legLeft;
    private final ModelPart legRight;
    private final ModelPart pickaxe;

    public GoldDiggerZombieModel(ModelPart root) {
        this.root = root;
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.armLeft = root.getChild("arm_left");
        this.armRight = root.getChild("arm_right");
        this.legLeft = root.getChild("leg_left");
        this.legRight = root.getChild("leg_right");
        this.pickaxe = this.armRight.getChild("pickaxe");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("head", CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F)
                        .texOffs(32, 0).addBox(-4.5F, -9.0F, -4.5F, 9.0F, 3.0F, 9.0F), // hard hat
                PartPose.offset(0.0F, 0.0F, 0.0F));

        root.addOrReplaceChild("body", CubeListBuilder.create()
                        .texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F)
                        .texOffs(16, 32).addBox(-4.5F, 0.0F, -2.5F, 9.0F, 12.0F, 5.0F, new CubeDeformation(0.15F)), // overalls
                PartPose.offset(0.0F, 0.0F, 0.0F));

        // Prospector pack with a rolled bedroll.
        root.addOrReplaceChild("pack", CubeListBuilder.create()
                        .texOffs(44, 32).addBox(-3.5F, 1.0F, 2.5F, 7.0F, 8.0F, 3.0F)
                        .texOffs(44, 44).addBox(-3.5F, -1.0F, 2.5F, 7.0F, 2.0F, 3.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        // Zombie-style arms reaching forward.
        root.addOrReplaceChild("arm_left", CubeListBuilder.create()
                        .texOffs(40, 16).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F),
                PartPose.offsetAndRotation(5.0F, 2.0F, 0.0F, -(float) Math.PI / 2.0F, 0.0F, 0.0F));

        root.addOrReplaceChild("arm_right", CubeListBuilder.create()
                        .texOffs(40, 16).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, true),
                PartPose.offsetAndRotation(-5.0F, 2.0F, 0.0F, -(float) Math.PI / 2.0F, 0.0F, 0.0F));

        root.addOrReplaceChild("leg_left", CubeListBuilder.create()
                        .texOffs(0, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F),
                PartPose.offset(2.0F, 12.0F, 0.0F));

        root.addOrReplaceChild("leg_right", CubeListBuilder.create()
                        .texOffs(0, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F, true),
                PartPose.offset(-2.0F, 12.0F, 0.0F));

        // Pickaxe gripped in the right hand.
        root.getChild("arm_right").addOrReplaceChild("pickaxe", CubeListBuilder.create()
                        .texOffs(0, 32).addBox(-0.5F, 6.0F, -12.0F, 1.0F, 1.0F, 16.0F)   // handle
                        .texOffs(0, 50).addBox(-0.5F, 4.5F, -14.0F, 1.0F, 4.0F, 2.0F)     // head hub
                        .texOffs(8, 50).addBox(-5.0F, 5.0F, -13.5F, 4.5F, 2.0F, 1.0F)     // left blade
                        .texOffs(8, 54).addBox(0.5F, 5.0F, -13.5F, 4.5F, 2.0F, 1.0F),      // right blade
                PartPose.offsetAndRotation(0.0F, 10.0F, 0.0F, 0.0F, (float) Math.PI / 2.0F, 0.0F));

        return LayerDefinition.create(mesh, 80, 80);
    }

    public static final AnimationDefinition IDLE = AnimationDefinition.Builder.withLength(2.4F).looping()
            .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[]{
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0, 0, 0), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.2F, KeyframeAnimations.degreeVec(3.0F, 2.0F, 0), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.4F, KeyframeAnimations.degreeVec(0, 0, 0), AnimationChannel.Interpolations.LINEAR)
            }))
            .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[]{
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0, 0, 0), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.2F, KeyframeAnimations.degreeVec(-6.0F, -4.0F, 0), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.4F, KeyframeAnimations.degreeVec(0, 0, 0), AnimationChannel.Interpolations.LINEAR)
            }))
            .build();

    /** Two-handed overhead chop used for attacks and digging. */
    public static final AnimationDefinition SWING = AnimationDefinition.Builder.withLength(0.5F)
            .addAnimation("arm_right", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[]{
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0, 0, 0), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.14F, KeyframeAnimations.degreeVec(35.0F, 0, 0), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.24F, KeyframeAnimations.degreeVec(-55.0F, 0, 0), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.34F, KeyframeAnimations.degreeVec(-62.0F, 0, 0), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(0, 0, 0), AnimationChannel.Interpolations.LINEAR)
            }))
            .addAnimation("arm_left", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[]{
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0, 0, 0), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.14F, KeyframeAnimations.degreeVec(30.0F, 0, 6.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.24F, KeyframeAnimations.degreeVec(-45.0F, 0, -4.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(0, 0, 0), AnimationChannel.Interpolations.LINEAR)
            }))
            .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[]{
                    new Keyframe(0.10F, KeyframeAnimations.degreeVec(-8.0F, 0, 0), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.26F, KeyframeAnimations.degreeVec(10.0F, 0, 0), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(0, 0, 0), AnimationChannel.Interpolations.LINEAR)
            }))
            .build();

    /** Relentless repeated mining strikes. */
    public static final AnimationDefinition DIG = AnimationDefinition.Builder.withLength(0.45F).looping()
            .addAnimation("arm_right", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[]{
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(20.0F, 0, 0), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.12F, KeyframeAnimations.degreeVec(-60.0F, 0, 0), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.22F, KeyframeAnimations.degreeVec(-70.0F, 0, 0), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.45F, KeyframeAnimations.degreeVec(20.0F, 0, 0), AnimationChannel.Interpolations.LINEAR)
            }))
            .addAnimation("arm_left", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[]{
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(15.0F, 0, 0), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.12F, KeyframeAnimations.degreeVec(-50.0F, 0, 0), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.45F, KeyframeAnimations.degreeVec(15.0F, 0, 0), AnimationChannel.Interpolations.LINEAR)
            }))
            .build();

    @Override
    public void setupAnim(GoldDiggerZombie entity, float limbSwing, float limbSwingAmount, float ageInTicks,
                          float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);

        // Restore zombie arm stance after resetPose.
        this.armLeft.xRot = -(float) Math.PI / 2.0F;
        this.armRight.xRot = -(float) Math.PI / 2.0F;

        this.head.yRot = netHeadYaw * Mth.DEG_TO_RAD;
        this.head.xRot = headPitch * Mth.DEG_TO_RAD;

        this.legRight.xRot = Mth.cos(limbSwing * 0.6662F) * 1.2F * limbSwingAmount;
        this.legLeft.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.2F * limbSwingAmount;

        this.animate(entity.idleState, IDLE, ageInTicks, 1.0F);
        if (entity.isDigging()) {
            this.animate(entity.digState, DIG, ageInTicks, 1.0F);
        } else {
            this.animate(entity.swingState, SWING, ageInTicks, 1.0F);
        }
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}
