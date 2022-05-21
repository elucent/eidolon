package elucent.eidolon.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import elucent.eidolon.Registry;
import elucent.eidolon.spell.Rune;
import elucent.eidolon.spell.Runes;
import elucent.eidolon.spell.Sign;
import elucent.eidolon.spell.Signs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;

public class RuneParticleData implements ParticleOptions {
    Rune rune;
    float r1, g1, b1, r2, g2, b2;

    public static Codec<RuneParticleData> codecFor(ParticleType<?> type) {
        return RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("rune").forGetter((d) -> d.rune.getRegistryName().toString()),
            Codec.FLOAT.fieldOf("r1").forGetter((d) -> d.r1),
            Codec.FLOAT.fieldOf("g1").forGetter((d) -> d.g1),
            Codec.FLOAT.fieldOf("b1").forGetter((d) -> d.b1),
            Codec.FLOAT.fieldOf("r2").forGetter((d) -> d.r2),
            Codec.FLOAT.fieldOf("g2").forGetter((d) -> d.g2),
            Codec.FLOAT.fieldOf("b2").forGetter((d) -> d.b2)
        ).apply(instance, (rune, r1, g1, b1, r2, g2, b2) -> {
            RuneParticleData data = new RuneParticleData(Runes.find(new ResourceLocation(rune)), r1, g1, b1, r2, g2, b2);
            return data;
        }));
    }

    public RuneParticleData(Rune rune, float r1, float g1, float b1, float r2, float g2, float b2) {
        this.rune = rune;
        this.r1 = r1;
        this.g1 = g1;
        this.b1 = b1;
        this.r2 = r2;
        this.g2 = g2;
        this.b2 = b2;
    }

    @Override
    public ParticleType<?> getType() {
        return Registry.RUNE_PARTICLE.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        buffer.writeUtf(rune.toString());
        buffer.writeFloat(r1);
        buffer.writeFloat(g1);
        buffer.writeFloat(b1);
        buffer.writeFloat(r2);
        buffer.writeFloat(g2);
        buffer.writeFloat(b2);
    }

    @Override
    public String writeToString() {
        return getClass().getSimpleName() + ":internal";
    }

    public static final Deserializer<RuneParticleData> DESERIALIZER = new Deserializer<RuneParticleData>() {
        @Override
        public RuneParticleData fromCommand(ParticleType<RuneParticleData> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            String loc = reader.readString();
            reader.expect(' ');
            float r1 = reader.readFloat();
            reader.expect(' ');
            float g1 = reader.readFloat();
            reader.expect(' ');
            float b1 = reader.readFloat();
            reader.expect(' ');
            float r2 = reader.readFloat();
            reader.expect(' ');
            float g2 = reader.readFloat();
            reader.expect(' ');
            float b2 = reader.readFloat();
            RuneParticleData data = new RuneParticleData(Runes.find(new ResourceLocation(loc)), r1, g1, b1, r2, g2, b2);
            return data;
        }

        @Override
        public RuneParticleData fromNetwork(ParticleType<RuneParticleData> type, FriendlyByteBuf buf) {
            String loc = buf.readUtf();
            float r1 = buf.readFloat();
            float g1 = buf.readFloat();
            float b1 = buf.readFloat();
            float r2 = buf.readFloat();
            float g2 = buf.readFloat();
            float b2 = buf.readFloat();
            RuneParticleData data = new RuneParticleData(Runes.find(new ResourceLocation(loc)), r1, g1, b1, r2, g2, b2);
            return data;
        }
    };
}
