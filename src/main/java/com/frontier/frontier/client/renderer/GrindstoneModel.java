package com.frontier.frontier.client.renderer;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

/**
 * Master Grindstone geometry (model space: y_model = -world_y).
 * Stone plinth, dark metal frame, segmented wheel on an x-axis axle,
 * anvil cap where the blade meets the stone.
 */
public final class GrindstoneModel {
    private GrindstoneModel() {
    }

    public static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // Stone plinth (world y 0..6).
        root.addOrReplaceChild("base", CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-8.0F, -4.0F, -8.0F, 16.0F, 4.0F, 16.0F)
                        .texOffs(0, 20).addBox(-7.0F, -6.0F, -7.0F, 14.0F, 2.0F, 14.0F),
                PartPose.ZERO);

        // Dark metal side frames (world y 6..15).
        root.addOrReplaceChild("frame_left", CubeListBuilder.create()
                        .texOffs(0, 36).addBox(-7.0F, -15.0F, -2.0F, 3.0F, 9.0F, 4.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("frame_right", CubeListBuilder.create()
                        .texOffs(14, 36).addBox(4.0F, -15.0F, -2.0F, 3.0F, 9.0F, 4.0F),
                PartPose.ZERO);

        // Axle at world y ~11.
        root.addOrReplaceChild("axle", CubeListBuilder.create()
                        .texOffs(28, 36).addBox(-4.5F, -0.5F, -0.5F, 9.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, -11.0F, 0.0F));

        // Grinding wheel: hub + eight rim segments, rotating about the x axis.
        PartDefinition wheel = root.addOrReplaceChild("wheel", CubeListBuilder.create()
                        .texOffs(36, 20).addBox(-2.0F, -2.0F, -1.5F, 4.0F, 4.0F, 3.0F),
                PartPose.offset(0.0F, -11.0F, 0.0F));
        for (int i = 0; i < 8; i++) {
            wheel.addOrReplaceChild("segment_" + i, CubeListBuilder.create()
                            .texOffs(44, 28).addBox(-1.5F, -6.5F, -1.5F, 3.0F, 4.5F, 3.0F),
                    PartPose.rotation(i * (float) Math.PI / 4.0F, 0.0F, 0.0F));
        }

        // Anvil cap (world y 15..16).
        root.addOrReplaceChild("cap", CubeListBuilder.create()
                        .texOffs(0, 50).addBox(-3.0F, -16.0F, -2.0F, 6.0F, 1.0F, 4.0F),
                PartPose.ZERO);

        return LayerDefinition.create(mesh, 64, 64);
    }
}
