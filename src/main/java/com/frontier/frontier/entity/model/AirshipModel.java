package com.frontier.frontier.entity.model;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

/**
 * Pocket Airship: wooden gondola, fabric balloon, rigging ropes, a small
 * steam engine with a propeller, and copper fittings. Model space:
 * y = -world_y, z = -world_z (nose is -z). Texture 128x128.
 */
public final class AirshipModel {
    private AirshipModel() {
    }

    public static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // Gondola hull with rim and keel.
        root.addOrReplaceChild("gondola", CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-7.0F, -5.0F, -10.0F, 14.0F, 5.0F, 20.0F)
                        .texOffs(0, 25).addBox(-8.0F, -6.5F, -11.0F, 16.0F, 1.5F, 22.0F)
                        .texOffs(0, 48).addBox(-2.0F, -3.0F, -13.0F, 4.0F, 2.0F, 4.0F),
                PartPose.ZERO);

        // Copper trim.
        root.addOrReplaceChild("trim", CubeListBuilder.create()
                        .texOffs(52, 25).addBox(-8.0F, -7.0F, -4.0F, 16.0F, 0.5F, 2.0F)
                        .texOffs(52, 25).addBox(-8.0F, -7.0F, 3.0F, 16.0F, 0.5F, 2.0F),
                PartPose.ZERO);

        // Rigging ropes.
        root.addOrReplaceChild("ropes", CubeListBuilder.create()
                        .texOffs(0, 56).addBox(-6.0F, -12.0F, -8.0F, 1.0F, 6.0F, 1.0F)
                        .texOffs(0, 56).addBox(5.0F, -12.0F, -8.0F, 1.0F, 6.0F, 1.0F)
                        .texOffs(0, 56).addBox(-6.0F, -12.0F, 7.0F, 1.0F, 6.0F, 1.0F)
                        .texOffs(0, 56).addBox(5.0F, -12.0F, 7.0F, 1.0F, 6.0F, 1.0F),
                PartPose.ZERO);

        // Fabric balloon (blocky, low-poly).
        PartDefinition balloon = root.addOrReplaceChild("balloon", CubeListBuilder.create()
                        .texOffs(0, 64).addBox(-6.0F, -17.0F, -12.0F, 12.0F, 7.0F, 24.0F)
                        .texOffs(48, 64).addBox(-4.5F, -16.0F, -16.0F, 9.0F, 5.0F, 4.0F)
                        .texOffs(48, 74).addBox(-4.5F, -16.0F, 12.0F, 9.0F, 5.0F, 4.0F)
                        .texOffs(72, 64).addBox(-3.0F, -18.5F, -10.0F, 6.0F, 2.0F, 20.0F)
                        .texOffs(72, 86).addBox(-3.0F, -11.0F, -10.0F, 6.0F, 1.5F, 20.0F),
                PartPose.ZERO);

        // Steam engine at the stern.
        root.addOrReplaceChild("engine", CubeListBuilder.create()
                        .texOffs(16, 48).addBox(-3.0F, -9.5F, 6.0F, 6.0F, 4.0F, 5.0F)
                        .texOffs(38, 48).addBox(-1.0F, -11.5F, 8.0F, 2.0F, 2.0F, 2.0F),
                PartPose.ZERO);

        // Propeller: hub + four blades, spinning about the z axis.
        PartDefinition propeller = root.addOrReplaceChild("propeller", CubeListBuilder.create()
                        .texOffs(46, 48).addBox(-1.0F, -1.0F, -0.5F, 2.0F, 2.0F, 1.0F),
                PartPose.offset(0.0F, -7.5F, 11.5F));
        for (int i = 0; i < 4; i++) {
            propeller.addOrReplaceChild("blade_" + i, CubeListBuilder.create()
                            .texOffs(52, 48).addBox(-0.5F, -6.5F, -0.5F, 1.0F, 5.5F, 1.0F),
                    PartPose.rotation(0.0F, 0.0F, i * (float) Math.PI / 2.0F));
        }

        return LayerDefinition.create(mesh, 128, 128);
    }
}
