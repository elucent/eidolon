package elucent.eidolon.network;

import java.util.function.Supplier;

import elucent.eidolon.Eidolon;
import elucent.eidolon.Registry;
import elucent.eidolon.particle.Particles;
import elucent.eidolon.particle.SlashParticleData;
import elucent.eidolon.util.ColorUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

public class DeathbringerSlashEffectPacket {
    float x1, y1, z1, x2, y2, z2;
    int c1, c2, c3, c4;

    public DeathbringerSlashEffectPacket(double x1, double y1, double z1, double x2, double y2, double z2, int color1, int color2, int color3, int color4) {
        this.x1 = (float)x1;
        this.y1 = (float)y1;
        this.z1 = (float)z1;
        this.x2 = (float)x2;
        this.y2 = (float)y2;
        this.z2 = (float)z2;
        this.c1 = color1;
        this.c2 = color2;
        this.c3 = color3;
        this.c4 = color4;
    }

    public static void encode(DeathbringerSlashEffectPacket object, FriendlyByteBuf buffer) {
        buffer.writeFloat(object.x1).writeFloat(object.y1).writeFloat(object.z1);
        buffer.writeFloat(object.x2).writeFloat(object.y2).writeFloat(object.z2);
        buffer.writeInt(object.c1).writeInt(object.c2).writeInt(object.c3).writeInt(object.c4);
    }

    public static DeathbringerSlashEffectPacket decode(FriendlyByteBuf buffer) {
        return new DeathbringerSlashEffectPacket(buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), 
        		buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readInt());
    }

    public static void consume(DeathbringerSlashEffectPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            assert ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT;

            Level world = Eidolon.proxy.getWorld();
            if (world != null) {
                double x = packet.x2, y = packet.y2, z = packet.z2;

                float r1 = ColorUtil.getRed(packet.c1) / 255.0f, g1 = ColorUtil.getGreen(packet.c1) / 255.0f, b1 = ColorUtil.getBlue(packet.c1) / 255.0f;
                float r2 = ColorUtil.getRed(packet.c2) / 255.0f, g2 = ColorUtil.getGreen(packet.c2) / 255.0f, b2 = ColorUtil.getBlue(packet.c2) / 255.0f;
                
                float roll = Mth.PI / 6 - world.random.nextFloat() * (Mth.PI * 7 / 6);
                float scale = 1.0f + world.random.nextFloat() * 0.2f;

            	Vec3 horiz = new Vec3(x - packet.x1, 0, z - packet.z1);
            	float yaw = (float)Mth.atan2(x - packet.x1, z - packet.z1);
            	float pitch = (float)Mth.atan2(y - packet.y1, horiz.length());
                float right = yaw + Mth.PI / 2, up = pitch + Mth.PI / 2;
            	float sy = Mth.sin(yaw), cy = Mth.cos(yaw), sp = Mth.sin(pitch), cp = Mth.cos(pitch);
            	float sr = Mth.sin(right), cr = Mth.cos(right), su = Mth.sin(up), cu = Mth.cos(up);
            	float r = 0.5f;
            	float xax = r * sr * cp, xay = 0, xaz = r * cr * cp;
            	float yax = r * sy * cu, yay = r * su, yaz = r * cy * cu;
            	float zax = r * sy * cp, zay = r * sp, zaz = r * cy * cp;
            	float cro = Mth.cos(roll), sro = Mth.sin(roll);
            	float nxax = xax * cro - yax * sro;
            	float nxay = xay * cro - yay * sro;
            	float nxaz = xaz * cro - yaz * sro;
            	yax = xax * sro + yax * cro;
            	yay = xay * sro + yay * cro;
            	yaz = xaz * sro + yaz * cro;
            	xax = nxax;
            	xay = nxay;
            	xaz = nxaz;
            	for (float i = 0; i < 6; i ++) {
            		float c1 = (i + 0.5f) / 6;
            		float angle = -75 + c1 * 150;
            		float sa = Mth.sin(Mth.DEG_TO_RAD * angle), ca = Mth.cos(Mth.DEG_TO_RAD * angle);
            		float dx = sa * xax + ca * zax, dy = sa * xay + ca * zay, dz = sa * xaz + ca * zaz;
            		Particles.create(Registry.SMOKE_PARTICLE)
            			.randomVelocity(0.025f)
            			.addVelocity(dx * 0.25f, dy * 0.25f, dz * 0.25f)
                		.setColor(33.0f/255, 26.0f/255, 23.0f/255, 0.125f, 10.0f/255, 10.0f/255, 12.0f/255, 0)
                		.randomOffset(0.1f)
                		.setScale(0.375f, 0.125f)
                		.repeat(world, x - sy * cp + dx, y - sp + dy, z - cy * cp + dz, 4);
            	}
                
                SlashParticleData.create(Registry.GLOWING_SLASH_PARTICLE.get())
                	.lookat(packet.x1, packet.y1, packet.z1, x, y, z)
                	.color(r1, g1, b1, r2, g2, b2)
                	.radius(0.9f * scale)
                	.angle(250)
                	.width(1f * scale)
                	.roll(roll)
                	.lifetime(11)
                	.spawn(world, x, y, z, 0, 0, 0);
                
                SlashParticleData.create(Registry.GLOWING_SLASH_PARTICLE.get())
                	.lookat(packet.x1, packet.y1, packet.z1, x, y, z)
                	.color(r1, g1, b1, r2, g2, b2)
                	.radius(0.8f * scale)
                	.angle(250)
                	.width(0.75f * scale)
                	.roll(roll)
                	.lifetime(11)
                	.spawn(world, x, y, z, 0, 0, 0);
                
                SlashParticleData.create(Registry.GLOWING_SLASH_PARTICLE.get())
                	.lookat(packet.x1, packet.y1, packet.z1, x, y, z)
                	.color(r1, g1, b1, r2, g2, b2)
                	.radius(0.7f * scale)
                	.angle(250)
                	.width(0.5f * scale)
                	.roll(roll)
                	.lifetime(13)
                	.spawn(world, x, y, z, 0, 0, 0);

                r1 = ColorUtil.getRed(packet.c3) / 255.0f; g1 = ColorUtil.getGreen(packet.c3) / 255.0f; b1 = ColorUtil.getBlue(packet.c3) / 255.0f;
                r2 = ColorUtil.getRed(packet.c4) / 255.0f; g2 = ColorUtil.getGreen(packet.c4) / 255.0f; b2 = ColorUtil.getBlue(packet.c4) / 255.0f;
                
                SlashParticleData.create(Registry.GLOWING_SLASH_PARTICLE.get())
                	.lookat(packet.x1, packet.y1, packet.z1, x, y, z)
                	.color(r1, g1, b1, r2, g2, b2)
                	.radius(0.8f * scale)
                	.angle(210)
                	.width(0.625f * scale)
                	.highlight(0.75f)
                	.alpha(0.75f, 0)
                	.roll(roll)
                	.lifetime(8)
                	.spawn(world, x, y, z, 0, 0, 0);
                
                SlashParticleData.create(Registry.GLOWING_SLASH_PARTICLE.get())
                	.lookat(packet.x1, packet.y1, packet.z1, x, y, z)
                	.color(r1, g1, b1, r2, g2, b2)
                	.radius(0.9f * scale)
                	.angle(210)
                	.width(0.25f * scale)
                	.highlight(0.625f)
                	.roll(roll)
                	.lifetime(10)
                	.spawn(world, x, y, z, 0, 0, 0);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
