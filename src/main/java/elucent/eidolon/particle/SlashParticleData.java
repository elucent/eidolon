package elucent.eidolon.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

public class SlashParticleData implements ParticleOptions {
    float r1 = 1, g1 = 1, b1 = 1, a1 = 1, r2 = 1, g2 = 1, b2 = 1, a2 = 0;
    float width = 0.625f, rad = 1, pitch = 0, yaw = 0, roll = 0, angle = 0;
    int lifetime = 10;
    float highlight = 0;

    public static Codec<SlashParticleData> codecFor(ParticleType<?> type) {
        return RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.fieldOf("r1").forGetter(d -> d.r1),
            Codec.FLOAT.fieldOf("g1").forGetter(d -> d.g1),
            Codec.FLOAT.fieldOf("b1").forGetter(d -> d.b1),
            Codec.FLOAT.fieldOf("a1").forGetter(d -> d.a1),
            Codec.FLOAT.fieldOf("r2").forGetter(d -> d.r2),
            Codec.FLOAT.fieldOf("g2").forGetter(d -> d.g2),
            Codec.FLOAT.fieldOf("b2").forGetter(d -> d.b2),
            Codec.FLOAT.fieldOf("a2").forGetter(d -> d.a2),
            Codec.FLOAT.fieldOf("width").forGetter(d -> d.width),
            Codec.FLOAT.fieldOf("rad").forGetter(d -> d.rad),
            Codec.FLOAT.fieldOf("pitch").forGetter(d -> d.pitch),
            Codec.FLOAT.fieldOf("yaw").forGetter(d -> d.yaw),
            Codec.FLOAT.fieldOf("roll").forGetter(d -> d.roll),
            Codec.FLOAT.fieldOf("angle").forGetter(d -> d.angle),
            Codec.INT.fieldOf("lifetime").forGetter(d -> d.lifetime),
            Codec.FLOAT.fieldOf("highlight").forGetter(d -> d.highlight)
        ).apply(instance, (r1, g1, b1, a1, r2, g2, b2, a2, width, rad,
                           pitch, yaw, roll, angle, lifetime, highlight) -> {
            SlashParticleData data = new SlashParticleData(type);
            data.r1 = r1; data.g1 = g1; data.b1 = b1; data.a1 = a1;
            data.r2 = r2; data.g2 = g2; data.b2 = b2; data.a2 = a2;
            data.width = width;
            data.rad = rad;
            data.pitch = pitch;
            data.yaw = yaw;
            data.roll = roll;
            data.angle = angle;
            data.lifetime = lifetime;
            data.highlight = highlight;
            return data;
        }));
    }

    ParticleType<?> type;

    public SlashParticleData(ParticleType<?> type) {
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
        buffer.writeFloat(width).writeFloat(rad);
        buffer.writeFloat(pitch).writeFloat(yaw).writeFloat(roll).writeFloat(angle);
        buffer.writeInt(lifetime);
    }

    @Override
    public String writeToString() {
        return getClass().getSimpleName() + ":internal";
    }

    public static final Deserializer<SlashParticleData> DESERIALIZER = new Deserializer<SlashParticleData>() {
        @Override
        public SlashParticleData fromCommand(ParticleType<SlashParticleData> type, StringReader reader) throws CommandSyntaxException {
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
            float width = reader.readFloat();
            reader.expect(' ');
            float rad = reader.readFloat();
            reader.expect(' ');
            float pitch = reader.readFloat();
            reader.expect(' ');
            float yaw = reader.readFloat();
            reader.expect(' ');
            float roll = reader.readFloat();
            reader.expect(' ');
            float angle = reader.readFloat();
            reader.expect(' ');
            int lifetime = reader.readInt();
            reader.expect(' ');
            float highlight = reader.readFloat();
            SlashParticleData data = new SlashParticleData(type);
            data.r1 = r1;
            data.g1 = g1;
            data.b1 = b1;
            data.a1 = a1;
            data.r2 = r2;
            data.g2 = g2;
            data.b2 = b2;
            data.a2 = a2;
            data.width = width;
            data.rad = rad;
            data.pitch = pitch;
            data.yaw = yaw;
            data.roll = roll;
            data.angle = angle;
            data.lifetime = lifetime;
            data.highlight = highlight;
            return data;
        }

        @Override
        public SlashParticleData fromNetwork(ParticleType<SlashParticleData> type, FriendlyByteBuf buf) {
            float r1 = buf.readFloat();
            float g1 = buf.readFloat();
            float b1 = buf.readFloat();
            float a1 = buf.readFloat();
            float r2 = buf.readFloat();
            float g2 = buf.readFloat();
            float b2 = buf.readFloat();
            float a2 = buf.readFloat();
            float width = buf.readFloat();
            float rad = buf.readFloat();
            float pitch = buf.readFloat();
            float yaw = buf.readFloat();
            float roll = buf.readFloat();
            float angle = buf.readFloat();
            int lifetime = buf.readInt();
            float highlight = buf.readFloat();
            SlashParticleData data = new SlashParticleData(type);
            data.r1 = r1;
            data.g1 = g1;
            data.b1 = b1;
            data.a1 = a1;
            data.r2 = r2;
            data.g2 = g2;
            data.b2 = b2;
            data.a2 = a2;
            data.width = width;
            data.rad = rad;
            data.pitch = pitch;
            data.roll = roll;
            data.yaw = yaw;
            data.angle = angle;
            data.lifetime = lifetime;
            data.highlight = highlight;
            return data;
        }
    };
    
    public static SlashParticleData create(ParticleType<?> type) {
    	SlashParticleData data = new SlashParticleData(type);
    	return data;
    }
    
    public SlashParticleData color(float r, float g, float b) {
    	return color(r, g, b, a1, r, g, b, a2);
    }
    
    public SlashParticleData color(float r, float g, float b, float a) {
    	return color(r, g, b, a, r, g, b, a);
    }
    
    public SlashParticleData color(float r1, float g1, float b1, float r2, float g2, float b2) {
    	return color(r1, g1, b1, a1, r2, g2, b2, a2);
    }
    
    public SlashParticleData color(float r1, float g1, float b1, float a1, float r2, float g2, float b2, float a2) {
    	this.r1 = r1;
    	this.g1 = g1;
    	this.b1 = b1;
    	this.a1 = a1;
    	this.r2 = r2;
    	this.g2 = g2;
    	this.b2 = b2;
    	this.a2 = a2;
    	return this;
    }
    
    public SlashParticleData alpha(float a) {
    	this.a1 = this.a2 = a;
    	return this;
    }
    
    public SlashParticleData alpha(float a1, float a2) {
    	this.a1 = a1;
    	this.a2 = a2;
    	return this;
    }
    
    public SlashParticleData width(float w) {
    	this.width = w;
    	return this;
    }
    
    public SlashParticleData lookat(double x1, double y1, double z1, double x2, double y2, double z2) {
    	Vec3 horiz = new Vec3(x2 - x1, 0, z2 - z1);
    	this.yaw = (float)Mth.atan2(x2 - x1, z2 - z1);
    	this.pitch = (float)Mth.atan2(y2 - y1, horiz.length());
    	return this;
    }
    
    public SlashParticleData pitch(float pitch) {
    	this.pitch = pitch;
    	return this;
    }
    
    public SlashParticleData yaw(float yaw) {
    	this.yaw = yaw;
    	return this;
    }
    
    public SlashParticleData roll(float roll) {
    	this.roll = roll;
    	return this;
    }
    
    public SlashParticleData highlight(float highlight) {
    	this.highlight = highlight;
    	return this;
    }
    
    public SlashParticleData angle(float angle) {
    	this.angle = Mth.DEG_TO_RAD * angle;
    	return this;
    }
    
    public SlashParticleData radius(float rad) {
    	this.rad = rad;
    	return this;
    }
    
    public SlashParticleData lifetime(int ticks) {
    	this.lifetime = ticks;
    	return this;
    }
    
    public void spawn(Level level, double x, double y, double z, double vx, double vy, double vz) {
    	level.addParticle(this, x, y, z, vx, vy, vz);
    }
}
