package elucent.eidolon.network;

import elucent.eidolon.Eidolon;
import elucent.eidolon.Registry;
import elucent.eidolon.particle.Particles;
import elucent.eidolon.util.ColorUtil;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MagicBurstEffectPacket {
    float x, y, z;
    int c1, c2;

    public MagicBurstEffectPacket(BlockPos pos, int color1, int color2) {
        this(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, color1, color2);
    }

    public MagicBurstEffectPacket(double x, double y, double z, int color1, int color2) {
        this.x = (float)x;
        this.y = (float)y;
        this.z = (float)z;
        this.c1 = color1;
        this.c2 = color2;
    }

    public static void encode(MagicBurstEffectPacket object, PacketBuffer buffer) {
        buffer.writeFloat(object.x).writeFloat(object.y).writeFloat(object.z);
        buffer.writeInt(object.c1).writeInt(object.c2);
    }

    public static MagicBurstEffectPacket decode(PacketBuffer buffer) {
        return new MagicBurstEffectPacket(buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readInt(), buffer.readInt());
    }

    public static void consume(MagicBurstEffectPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            assert ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT;

            World world = Eidolon.proxy.getWorld();
            if (world != null) {
                double x = packet.x, y = packet.y, z = packet.z;

                float r1 = ColorUtil.getRed(packet.c1) / 255.0f, g1 = ColorUtil.getGreen(packet.c1) / 255.0f, b1 = ColorUtil.getBlue(packet.c1) / 255.0f;
                float r2 = ColorUtil.getRed(packet.c2) / 255.0f, g2 = ColorUtil.getGreen(packet.c2) / 255.0f, b2 = ColorUtil.getBlue(packet.c2) / 255.0f;
                Particles.create(Registry.WISP_PARTICLE)
                    .setAlpha(0.5f, 0).setScale(0.25f, 0).setLifetime(20)
                    .randomOffset(0.125, 0.125).randomVelocity(0.0625f, 0.0625f)
                    .setColor(r1, g1, b1, r2, g2, b2)
                    .repeat(world, x, y, z, 12);
                Particles.create(Registry.SPARKLE_PARTICLE)
                    .setAlpha(1, 0).setScale(0.0625f, 0).setLifetime(80)
                    .randomOffset(0.0625, 0).randomVelocity(0.125f, 0.125f)
                    .addVelocity(0, 0.25f, 0)
                    .setColor(r1, g1, b1, r2, g2, b2)
                    .enableGravity().setSpin(0.4f)
                    .repeat(world, x, y, z, world.rand.nextInt(4) + 3);
                Particles.create(Registry.SMOKE_PARTICLE)
                    .setAlpha(0.25f, 0).setScale(0.375f, 0).setLifetime(20)
                    .randomOffset(0.25, 0.25).randomVelocity(0.015625f, 0.015625f)
                    .setColor(r2, g2, b2)
                    .repeat(world, x, y, z, 6);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
