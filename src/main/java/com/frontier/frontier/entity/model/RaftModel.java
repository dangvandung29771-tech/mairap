package com.frontier.frontier.entity.model;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

/**
 * Large hand-built raft: plank deck, rope-lashed rails, mast with a hanging
 * lantern, storage chest, furnace and two oars. Model space: y = -world_y,
 * z = -world_z (front of the raft is -z). Texture 128x128.
 */
public final class RaftModel {
    private RaftModel() {
    }

    public static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // Deck: three plank bands with visible seams.
        root.addOrReplaceChild("deck", CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-16.0F, -2.0F, -24.0F, 32.0F, 2.0F, 16.0F)
                        .texOffs(0, 18).addBox(-16.0F, -2.0F, -8.0F, 32.0F, 2.0F, 16.0F)
                        .texOffs(0, 36).addBox(-16.0F, -2.0F, 8.0F, 32.0F, 2.0F, 16.0F),
                PartPose.ZERO);

        // Cross-beams underneath.
        root.addOrReplaceChild("beams", CubeListBuilder.create()
                        .texOffs(0, 58).addBox(-17.0F, -3.5F, -20.0F, 34.0F, 1.5F, 3.0F)
                        .texOffs(0, 58).addBox(-17.0F, -3.5F, -1.5F, 34.0F, 1.5F, 3.0F)
                        .texOffs(0, 58).addBox(-17.0F, -3.5F, 17.0F, 34.0F, 1.5F, 3.0F),
                PartPose.ZERO);

        // Corner posts + side rails.
        root.addOrReplaceChild("posts", CubeListBuilder.create()
                        .texOffs(0, 63).addBox(-16.0F, -9.0F, -24.0F, 2.0F, 7.0F, 2.0F)
                        .texOffs(0, 63).addBox(14.0F, -9.0F, -24.0F, 2.0F, 7.0F, 2.0F)
                        .texOffs(0, 63).addBox(-16.0F, -9.0F, 22.0F, 2.0F, 7.0F, 2.0F)
                        .texOffs(0, 63).addBox(14.0F, -9.0F, 22.0F, 2.0F, 7.0F, 2.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("rails", CubeListBuilder.create()
                        .texOffs(8, 63).addBox(-16.5F, -9.0F, -22.0F, 1.5F, 1.5F, 44.0F)
                        .texOffs(8, 63).addBox(15.0F, -9.0F, -22.0F, 1.5F, 1.5F, 44.0F)
                        .texOffs(8, 63).addBox(-14.0F, -9.0F, 22.5F, 28.0F, 1.5F, 1.5F),
                PartPose.ZERO);

        // Rope coils on deck.
        root.addOrReplaceChild("rope", CubeListBuilder.create()
                        .texOffs(0, 74).addBox(-13.0F, -3.5F, -21.0F, 4.0F, 1.5F, 4.0F)
                        .texOffs(0, 80).addBox(9.0F, -3.5F, 18.0F, 4.0F, 1.5F, 4.0F),
                PartPose.ZERO);

        // Mast + crossbar + hanging lantern.
        root.addOrReplaceChild("mast", CubeListBuilder.create()
                        .texOffs(64, 58).addBox(-1.0F, -26.0F, -19.0F, 2.0F, 24.0F, 2.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("crossbar", CubeListBuilder.create()
                        .texOffs(72, 58).addBox(-6.0F, -25.0F, -19.5F, 12.0F, 1.5F, 1.5F),
                PartPose.ZERO);
        root.addOrReplaceChild("lantern", CubeListBuilder.create()
                        .texOffs(64, 84).addBox(4.0F, -22.5F, -20.0F, 3.0F, 4.0F, 3.0F)
                        .texOffs(64, 92).addBox(4.5F, -23.5F, -19.5F, 2.0F, 1.0F, 2.0F),
                PartPose.ZERO);

        // Storage chest (starboard stern).
        root.addOrReplaceChild("chest", CubeListBuilder.create()
                        .texOffs(96, 0).addBox(6.0F, -8.0F, 14.0F, 8.0F, 6.0F, 6.0F)
                        .texOffs(96, 12).addBox(6.0F, -8.5F, 14.0F, 8.0F, 1.0F, 6.0F),
                PartPose.ZERO);

        // Furnace (port stern) with a glowing mouth.
        root.addOrReplaceChild("furnace", CubeListBuilder.create()
                        .texOffs(96, 20).addBox(-14.0F, -9.0F, 14.0F, 7.0F, 7.0F, 6.0F)
                        .texOffs(96, 34).addBox(-12.5F, -6.5F, 13.6F, 4.0F, 3.0F, 0.4F),
                PartPose.ZERO);

        // Oars pivot on the rails; the renderer swings them while rowing.
        PartDefinition oarLeft = root.addOrReplaceChild("oar_left", CubeListBuilder.create()
                        .texOffs(0, 86).addBox(-1.0F, -1.0F, -1.0F, 18.0F, 2.0F, 2.0F)
                        .texOffs(0, 92).addBox(16.0F, -1.5F, -1.5F, 6.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(-15.0F, -8.0F, -4.0F, 0.0F, 0.35F, 0.0F));
        PartDefinition oarRight = root.addOrReplaceChild("oar_right", CubeListBuilder.create()
                        .texOffs(0, 86).addBox(-17.0F, -1.0F, -1.0F, 18.0F, 2.0F, 2.0F)
                        .texOffs(0, 92).addBox(-22.0F, -1.5F, -1.5F, 6.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(15.0F, -8.0F, -4.0F, 0.0F, -0.35F, 0.0F));

        return LayerDefinition.create(mesh, 128, 128);
    }
}
