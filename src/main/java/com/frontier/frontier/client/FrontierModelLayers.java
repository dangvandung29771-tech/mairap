package com.frontier.frontier.client;

import com.frontier.frontier.FrontierMod;
import net.minecraft.client.model.geom.ModelLayerLocation;

public final class FrontierModelLayers {
    public static final ModelLayerLocation GRINDSTONE = layer("master_grindstone");
    public static final ModelLayerLocation SPRINKLER = layer("sprinkler");
    public static final ModelLayerLocation RAFT = layer("large_raft");
    public static final ModelLayerLocation AIRSHIP = layer("pocket_airship");
    public static final ModelLayerLocation COPPER_SHIELD_SKELETON = layer("copper_shield_skeleton");
    public static final ModelLayerLocation GOLD_DIGGER_ZOMBIE = layer("gold_digger_zombie");

    private FrontierModelLayers() {
    }

    private static ModelLayerLocation layer(String name) {
        return new ModelLayerLocation(FrontierMod.id(name), "main");
    }
}
