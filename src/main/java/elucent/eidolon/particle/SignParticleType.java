package elucent.eidolon.particle;

import java.util.Random;

import com.mojang.serialization.Codec;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.ParticleType;

public class SignParticleType extends ParticleType<SignParticleData> {
    public SignParticleType() {
        super(false, SignParticleData.DESERIALIZER);
    }

    @Override
    public Codec<SignParticleData> func_230522_e_() {
        return SignParticleData.codecFor(this);
    }

    public static class Factory implements IParticleFactory<SignParticleData> {
        public Factory() {
            //
        }

        @Override
        public Particle makeParticle(SignParticleData data, ClientWorld world, double x, double y, double z, double mx, double my, double mz) {
            SignParticle ret = new SignParticle(world, data.sign, x, y, z, mx, my, mz);
            ret.selectSpriteRandomly(new IAnimatedSprite() {
                @Override
                public TextureAtlasSprite get(int particleAge, int particleMaxAge) {
                    return Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(ret.sign.getSprite());
                }

                @Override
                public TextureAtlasSprite get(Random rand) {
                    return Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(ret.sign.getSprite());
                }
            });
            return ret;
        }
    }
}
