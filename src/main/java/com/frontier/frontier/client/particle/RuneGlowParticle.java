package com.frontier.frontier.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;

/** Soft magical dust drifting off an awoken rune. */
public class RuneGlowParticle extends TextureSheetParticle {
    private final SpriteSet sprites;

    public RuneGlowParticle(ClientLevel level, double x, double y, double z, SpriteSet sprites) {
        super(level, x, y, z, 0, 0, 0);
        this.sprites = sprites;
        this.setSpriteFromAge(sprites);
        this.lifetime = 30 + level.random.nextInt(20);
        this.quadSize = 0.08F + level.random.nextFloat() * 0.06F;
        this.yd = 0.012 + level.random.nextDouble() * 0.01;
        this.xd = (level.random.nextDouble() - 0.5) * 0.01;
        this.zd = (level.random.nextDouble() - 0.5) * 0.01;
        this.rCol = 0.45F + level.random.nextFloat() * 0.1F;
        this.gCol = 0.85F;
        this.bCol = 0.9F;
        this.hasPhysics = false;
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteFromAge(sprites);
    }

    @Override
    protected int getLightColor(float partialTicks) {
        return 0xF000F0;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z,
                                       double dx, double dy, double dz) {
            return new RuneGlowParticle(level, x, y, z, sprites);
        }
    }
}
