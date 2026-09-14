package com.frontier.frontier.client.renderer;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

/** Brass-and-wood sprinkler with a rotating spray head (y_model = -world_y). */
public final class SprinklerModel {
    private SprinklerModel() {
    }

    public static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("column", CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-3.0F, -8.0F, -3.0F, 6.0F, 8.0F, 6.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("collar", CubeListBuilder.create()
                        .texOffs(0, 14).addBox(-4.0F, -9.0F, -4.0F, 8.0F, 1.0F, 8.0F),
                PartPose.ZERO);

        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create()
                        .texOffs(0, 24).addBox(-2.5F, -12.0F, -2.5F, 5.0F, 3.0F, 5.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        // Spray nozzles.
        head.addOrReplaceChild("nozzle_1", CubeListBuilder.create()
                        .texOffs(20, 24).addBox(2.5F, -11.5F, -1.0F, 2.0F, 2.0F, 2.0F),
                PartPose.ZERO);
        head.addOrReplaceChild("nozzle_2", CubeListBuilder.create()
                        .texOffs(20, 24).addBox(-4.5F, -11.5F, -1.0F, 2.0F, 2.0F, 2.0F),
                PartPose.ZERO);
        head.addOrReplaceChild("nozzle_3", CubeListBuilder.create()
                        .texOffs(20, 24).addBox(-1.0F, -11.5F, 2.5F, 2.0F, 2.0F, 2.0F),
                PartPose.ZERO);
        head.addOrReplaceChild("nozzle_4", CubeListBuilder.create()
                        .texOffs(20, 24).addBox(-1.0F, -11.5F, -4.5F, 2.0F, 2.0F, 2.0F),
                PartPose.ZERO);

        return LayerDefinition.create(mesh, 32, 32);
    }
}
