package com.frontier.frontier.init;

import com.frontier.frontier.FrontierMod;
import com.frontier.frontier.forge.ForgedData;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import java.util.function.UnaryOperator;

public class FrontierComponents {
    public static final DeferredRegister<DataComponentType<?>> COMPONENTS =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, FrontierMod.MODID);

    /** Forged properties applied at the Master Grindstone. */
    public static final Supplier<DataComponentType<ForgedData>> FORGED = COMPONENTS.register("forged",
            () -> build(builder -> builder.persistent(ForgedData.CODEC).networkSynchronized(ForgedData.STREAM_CODEC)));

    /** Attuned crypt position for the Explorer Compass. */
    public static final Supplier<DataComponentType<BlockPos>> CRYPT_POS = COMPONENTS.register("crypt_pos",
            () -> build(builder -> builder.persistent(BlockPos.CODEC).networkSynchronized(ByteBufCodecs.BLOCK_POS)));

    private static <T> DataComponentType<T> build(UnaryOperator<DataComponentType.Builder<T>> op) {
        return op.apply(DataComponentType.builder()).cacheEncoding().build();
    }

    public static void touch() {
    }
}
