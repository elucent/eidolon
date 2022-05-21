package elucent.eidolon.particle;

import java.util.Random;

import com.mojang.serialization.Codec;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleType;

public class RuneParticleType extends ParticleType<RuneParticleData> {
    public RuneParticleType() {
        super(false, RuneParticleData.DESERIALIZER);
    }

    @Override
    public Codec<RuneParticleData> codec() {
        return RuneParticleData.codecFor(this);
    }

    public static class Factory implements ParticleProvider<RuneParticleData> {
        public Factory() {
            //
        }

        @Override
        public Particle createParticle(RuneParticleData data, ClientLevel world, double x, double y, double z, double mx, double my, double mz) {
            RuneParticle ret = new RuneParticle(world, data, x, y, z, mx, my, mz);
            ret.pickSprite(new SpriteSet() {
                @Override
                public TextureAtlasSprite get(int particleAge, int particleMaxAge) {
                    return Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(ret.rune.getSprite());
                }

                @Override
                public TextureAtlasSprite get(Random rand) {
                    return Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(ret.rune.getSprite());
                }
            });
            return ret;
        }
    }
}
