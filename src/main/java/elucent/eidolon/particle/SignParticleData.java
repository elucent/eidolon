package elucent.eidolon.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import elucent.eidolon.Registry;
import elucent.eidolon.spell.Sign;
import elucent.eidolon.spell.Signs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;

import net.minecraft.core.particles.ParticleOptions.Deserializer;

public class SignParticleData implements ParticleOptions {
    Sign sign;

    public static Codec<SignParticleData> codecFor(ParticleType<?> type) {
        return RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("sign").forGetter((d) -> d.sign.getRegistryName().toString())
        ).apply(instance, (sign) -> {
            SignParticleData data = new SignParticleData(Signs.find(new ResourceLocation(sign)));
            return data;
        }));
    }

    public SignParticleData(Sign sign) {
        this.sign = sign;
    }

    @Override
    public ParticleType<?> getType() {
        return Registry.SIGN_PARTICLE.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        buffer.writeUtf(sign.toString());
    }

    @Override
    public String writeToString() {
        return getClass().getSimpleName() + ":internal";
    }

    public static final Deserializer<SignParticleData> DESERIALIZER = new Deserializer<SignParticleData>() {
        @Override
        public SignParticleData fromCommand(ParticleType<SignParticleData> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            String loc = reader.readString();
            SignParticleData data = new SignParticleData(Signs.find(new ResourceLocation(loc)));
            return data;
        }

        @Override
        public SignParticleData fromNetwork(ParticleType<SignParticleData> type, FriendlyByteBuf buf) {
            String loc = buf.readUtf();
            SignParticleData data = new SignParticleData(Signs.find(new ResourceLocation(loc)));
            return data;
        }
    };
}
