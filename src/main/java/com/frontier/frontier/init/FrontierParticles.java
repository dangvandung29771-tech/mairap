package com.frontier.frontier.init;

import com.frontier.frontier.FrontierMod;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class FrontierParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(Registries.PARTICLE_TYPE, FrontierMod.MODID);

    public static final Supplier<SimpleParticleType> FORGE_SPARK =
            PARTICLE_TYPES.register("forge_spark", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> RUNE_GLOW =
            PARTICLE_TYPES.register("rune_glow", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> STEAM_PUFF =
            PARTICLE_TYPES.register("steam_puff", () -> new SimpleParticleType(false));
}
