package elucent.eidolon.particle;

import com.mojang.serialization.Codec;

import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleType;

public class SlashParticleType extends ParticleType<SlashParticleData> {
    public SlashParticleType() {
        super(false, SlashParticleData.DESERIALIZER);
    }

    @Override
    public Codec<SlashParticleData> codec() {
        return SlashParticleData.codecFor(this);
    }

    public static class Factory implements ParticleProvider<SlashParticleData> {
        private final SpriteSet sprite;

        public Factory(SpriteSet sprite) {
            this.sprite = sprite;
        }

        @Override
        public Particle createParticle(SlashParticleData data, ClientLevel world, double x, double y, double z, double mx, double my, double mz) {
            SlashParticle ret = new SlashParticle(world, data, x, y, z, mx, my, mz);
            ret.pickSprite(sprite);
            return ret;
        }
    }
}
