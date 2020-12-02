package elucent.eidolon.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import elucent.eidolon.Registry;
import elucent.eidolon.spell.Sign;
import elucent.eidolon.spell.Signs;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.ResourceLocation;

public class SignParticleData implements IParticleData {
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
    public void write(PacketBuffer buffer) {
        buffer.writeString(sign.toString());
    }

    @Override
    public String getParameters() {
        return getClass().getSimpleName() + ":internal";
    }

    public static final IDeserializer<SignParticleData> DESERIALIZER = new IDeserializer<SignParticleData>() {
        @Override
        public SignParticleData deserialize(ParticleType<SignParticleData> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            String loc = reader.readString();
            SignParticleData data = new SignParticleData(Signs.find(new ResourceLocation(loc)));
            return data;
        }

        @Override
        public SignParticleData read(ParticleType<SignParticleData> type, PacketBuffer buf) {
            String loc = buf.readString();
            SignParticleData data = new SignParticleData(Signs.find(new ResourceLocation(loc)));
            return data;
        }
    };
}
