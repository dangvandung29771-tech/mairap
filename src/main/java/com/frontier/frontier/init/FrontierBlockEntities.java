package com.frontier.frontier.init;

import com.frontier.frontier.FrontierMod;
import com.frontier.frontier.block.entity.MasterGrindstoneBlockEntity;
import com.frontier.frontier.block.entity.SprinklerBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class FrontierBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, FrontierMod.MODID);

    public static final Supplier<BlockEntityType<MasterGrindstoneBlockEntity>> MASTER_GRINDSTONE =
            BLOCK_ENTITY_TYPES.register("master_grindstone",
                    () -> BlockEntityType.Builder.of(MasterGrindstoneBlockEntity::new,
                            FrontierBlocks.MASTER_GRINDSTONE.get()).build(null));

    public static final Supplier<BlockEntityType<SprinklerBlockEntity>> SPRINKLER =
            BLOCK_ENTITY_TYPES.register("sprinkler",
                    () -> BlockEntityType.Builder.of(SprinklerBlockEntity::new,
                            FrontierBlocks.SPRINKLER.get()).build(null));
}
