package elucent.eidolon.particle;

import com.mojang.serialization.Codec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.ParticleType;

import java.util.Random;

public class SignParticleType extends ParticleType<SignParticleData> {
    public SignParticleType() {
        super(false, SignParticleData.DESERIALIZER);
    }

    @Override
    public Codec<SignParticleData> codec() {
        return SignParticleData.codecFor(this);
    }

    public static class Factory implements ParticleProvider<SignParticleData> {
        public Factory() {
            //
        }

        @Override
        public Particle createParticle(SignParticleData data, ClientLevel world, double x, double y, double z, double mx, double my, double mz) {
            SignParticle ret = new SignParticle(world, data.sign, x, y, z, mx, my, mz);
            ret.pickSprite(new SpriteSet() {
                @Override
                public TextureAtlasSprite get(int particleAge, int particleMaxAge) {
                    return Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(ret.sign.getSprite());
                }

                @Override
                public TextureAtlasSprite get(Random rand) {
                    return Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(ret.sign.getSprite());
                }
            });
            return ret;
        }
    }
}
