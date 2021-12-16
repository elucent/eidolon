package elucent.eidolon.particle;

import com.mojang.serialization.Codec;

import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleType;

public class BubbleParticleType extends ParticleType<GenericParticleData> {
    public BubbleParticleType() {
        super(false, GenericParticleData.DESERIALIZER);
    }

    @Override
    public Codec<GenericParticleData> codec() {
        return GenericParticleData.codecFor(this);
    }

    public static class Factory implements ParticleProvider<GenericParticleData> {
        private final SpriteSet sprite;

        public Factory(SpriteSet sprite) {
            this.sprite = sprite;
        }

        @Override
        public Particle createParticle(GenericParticleData data, ClientLevel world, double x, double y, double z, double mx, double my, double mz) {
            BubbleParticle ret = new BubbleParticle(world, data, sprite, x, y, z, mx, my, mz);
            ret.setSpriteFromAge(sprite);
            return ret;
        }
    }
}
