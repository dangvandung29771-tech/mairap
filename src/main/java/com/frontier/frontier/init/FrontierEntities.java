package com.frontier.frontier.init;

import com.frontier.frontier.FrontierMod;
import com.frontier.frontier.entity.AirshipEntity;
import com.frontier.frontier.entity.CopperShieldSkeleton;
import com.frontier.frontier.entity.GoldDiggerZombie;
import com.frontier.frontier.entity.RaftEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class FrontierEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(Registries.ENTITY_TYPE, FrontierMod.MODID);

    public static final Supplier<EntityType<RaftEntity>> LARGE_RAFT = ENTITY_TYPES.register("large_raft",
            () -> EntityType.Builder.<RaftEntity>of(RaftEntity::new, MobCategory.MISC)
                    .sized(2.5F, 0.9F)
                    .eyeHeight(0.5F)
                    .clientTrackingRange(10)
                    .build("large_raft"));

    public static final Supplier<EntityType<AirshipEntity>> POCKET_AIRSHIP = ENTITY_TYPES.register("pocket_airship",
            () -> EntityType.Builder.<AirshipEntity>of(AirshipEntity::new, MobCategory.MISC)
                    .sized(1.7F, 1.9F)
                    .eyeHeight(1.2F)
                    .clientTrackingRange(10)
                    .build("pocket_airship"));

    public static final Supplier<EntityType<CopperShieldSkeleton>> COPPER_SHIELD_SKELETON =
            ENTITY_TYPES.register("copper_shield_skeleton",
                    () -> EntityType.Builder.<CopperShieldSkeleton>of(CopperShieldSkeleton::new, MobCategory.MONSTER)
                            .sized(0.7F, 2.1F)
                            .eyeHeight(1.74F)
                            .clientTrackingRange(8)
                            .build("copper_shield_skeleton"));

    public static final Supplier<EntityType<GoldDiggerZombie>> GOLD_DIGGER_ZOMBIE =
            ENTITY_TYPES.register("gold_digger_zombie",
                    () -> EntityType.Builder.<GoldDiggerZombie>of(GoldDiggerZombie::new, MobCategory.MONSTER)
                            .sized(0.7F, 2.05F)
                            .eyeHeight(1.74F)
                            .clientTrackingRange(8)
                            .build("gold_digger_zombie"));
}
