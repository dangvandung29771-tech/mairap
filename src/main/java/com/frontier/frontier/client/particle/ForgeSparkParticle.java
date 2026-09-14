package com.frontier.frontier.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;

/** A short-lived hot metal spark from the Master Grindstone. */
public class ForgeSparkParticle extends TextureSheetParticle {
    private final SpriteSet sprites;

    public ForgeSparkParticle(ClientLevel level, double x, double y, double z,
                              double dx, double dy, double dz, SpriteSet sprites) {
        super(level, x, y, z, dx, dy, dz);
        this.sprites = sprites;
        this.gravity = 0.6F;
        this.lifetime = 12 + level.random.nextInt(10);
        this.xd = dx * 0.5 + (level.random.nextDouble() - 0.5) * 0.06;
        this.yd = dy * 0.5 + level.random.nextDouble() * 0.05;
        this.zd = dz * 0.5 + (level.random.nextDouble() - 0.5) * 0.06;
        this.quadSize = 0.06F + level.random.nextFloat() * 0.04F;
        this.setSpriteFromAge(sprites);
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
            return new ForgeSparkParticle(level, x, y, z, dx, dy, dz, sprites);
        }
    }
}
