package elucent.eidolon.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

public class GenericParticleData implements ParticleOptions {
    float r1 = 1, g1 = 1, b1 = 1, a1 = 1, r2 = 1, g2 = 1, b2 = 1, a2 = 0;
    float scale1 = 1, scale2 = 0;
    int lifetime = 20;
    float spin = 0;
    boolean gravity = false;

    public static Codec<GenericParticleData> codecFor(ParticleType<?> type) {
        return RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.fieldOf("r1").forGetter(d -> d.r1),
            Codec.FLOAT.fieldOf("g1").forGetter(d -> d.g1),
            Codec.FLOAT.fieldOf("b1").forGetter(d -> d.b1),
            Codec.FLOAT.fieldOf("a1").forGetter(d -> d.a1),
            Codec.FLOAT.fieldOf("r2").forGetter(d -> d.r2),
            Codec.FLOAT.fieldOf("g2").forGetter(d -> d.g2),
            Codec.FLOAT.fieldOf("b2").forGetter(d -> d.b2),
            Codec.FLOAT.fieldOf("a2").forGetter(d -> d.a2),
            Codec.FLOAT.fieldOf("scale1").forGetter(d -> d.scale1),
            Codec.FLOAT.fieldOf("scale2").forGetter(d -> d.scale2),
            Codec.INT.fieldOf("lifetime").forGetter(d -> d.lifetime),
            Codec.FLOAT.fieldOf("spin").forGetter(d -> d.spin),
            Codec.BOOL.fieldOf("gravity").forGetter(d -> d.gravity)
        ).apply(instance, (r1, g1, b1, a1, r2, g2, b2, a2, scale1, scale2,
                           lifetime, spin, gravity) -> {
            GenericParticleData data = new GenericParticleData(type);
            data.r1 = r1; data.g1 = g1; data.b1 = b1; data.a1 = a1;
            data.r2 = r2; data.g2 = g2; data.b2 = b2; data.a2 = a2;
            data.scale1 = scale1;
            data.scale2 = scale2;
            data.lifetime = lifetime;
            data.spin = spin;
            data.gravity = gravity;
            return data;
        }));
    }

    ParticleType<?> type;

    public GenericParticleData(ParticleType<?> type) {
        this.type = type;
    }

    @Override
    public ParticleType<?> getType() {
        return type;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        buffer.writeFloat(r1).writeFloat(g1).writeFloat(b1).writeFloat(a1);
        buffer.writeFloat(r2).writeFloat(g2).writeFloat(b2).writeFloat(a2);
        buffer.writeFloat(scale1).writeFloat(scale2);
        buffer.writeInt(lifetime);
        buffer.writeFloat(spin);
        buffer.writeBoolean(gravity);
    }

    @Override
    public String writeToString() {
        return getClass().getSimpleName() + ":internal";
    }

    public static final Deserializer<GenericParticleData> DESERIALIZER = new Deserializer<GenericParticleData>() {
        @Override
        public GenericParticleData fromCommand(ParticleType<GenericParticleData> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            float r1 = reader.readFloat();
            reader.expect(' ');
            float g1 = reader.readFloat();
            reader.expect(' ');
            float b1 = reader.readFloat();
            reader.expect(' ');
            float a1 = reader.readFloat();
            reader.expect(' ');
            float r2 = reader.readFloat();
            reader.expect(' ');
            float g2 = reader.readFloat();
            reader.expect(' ');
            float b2 = reader.readFloat();
            reader.expect(' ');
            float a2 = reader.readFloat();
            reader.expect(' ');
            float scale1 = reader.readFloat();
            reader.expect(' ');
            float scale2 = reader.readFloat();
            reader.expect(' ');
            int lifetime = reader.readInt();
            reader.expect(' ');
            float spin = reader.readFloat();
            reader.expect(' ');
            boolean gravity = reader.readBoolean();
            GenericParticleData data = new GenericParticleData(type);
            data.r1 = r1;
            data.g1 = g1;
            data.b1 = b1;
            data.a1 = a1;
            data.r2 = r2;
            data.g2 = g2;
            data.b2 = b2;
            data.a2 = a2;
            data.scale1 = scale1;
            data.scale2 = scale2;
            data.lifetime = lifetime;
            data.spin = spin;
            data.gravity = gravity;
            return data;
        }

        @Override
        public GenericParticleData fromNetwork(ParticleType<GenericParticleData> type, FriendlyByteBuf buf) {
            float r1 = buf.readFloat();
            float g1 = buf.readFloat();
            float b1 = buf.readFloat();
            float a1 = buf.readFloat();
            float r2 = buf.readFloat();
            float g2 = buf.readFloat();
            float b2 = buf.readFloat();
            float a2 = buf.readFloat();
            float scale1 = buf.readFloat();
            float scale2 = buf.readFloat();
            int lifetime = buf.readInt();
            float spin = buf.readFloat();
            boolean gravity = buf.readBoolean();
            GenericParticleData data = new GenericParticleData(type);
            data.r1 = r1;
            data.g1 = g1;
            data.b1 = b1;
            data.a1 = a1;
            data.r2 = r2;
            data.g2 = g2;
            data.b2 = b2;
            data.a2 = a2;
            data.scale1 = scale1;
            data.scale2 = scale2;
            data.lifetime = lifetime;
            data.spin = spin;
            data.gravity = gravity;
            return data;
        }
    };
}
