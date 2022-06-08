package elucent.eidolon.network;

import elucent.eidolon.Eidolon;
import elucent.eidolon.Registry;
import elucent.eidolon.particle.Particles;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class LifestealEffectPacket {
    BlockPos src, dst;
    float r, g, b;

    public LifestealEffectPacket(BlockPos src, BlockPos dst, float r, float g, float b) {
        this.src = src;
        this.dst = dst;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public static void encode(LifestealEffectPacket object, FriendlyByteBuf buffer) {
        buffer.writeBlockPos(object.src).writeBlockPos(object.dst);
        buffer.writeFloat(object.r).writeFloat(object.g).writeFloat(object.b);
    }

    public static LifestealEffectPacket decode(FriendlyByteBuf buffer) {
        return new LifestealEffectPacket(buffer.readBlockPos(), buffer.readBlockPos(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat());
    }

    public static void consume(LifestealEffectPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            assert ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT;

            Level world = Eidolon.proxy.getWorld();
            if (world != null) {
                BlockPos src = packet.src, dst = packet.dst;
                for (int i = 0; i < 10; i ++) {
                    Particles.create(Registry.LINE_WISP_PARTICLE)
                        .setAlpha(0.75f, 0).setScale(0.25f + 0.125f * world.random.nextFloat(), 0).setLifetime(16 + world.random.nextInt(4))
                        .randomOffset(0.375, 0.375).randomVelocity(0.125, 0.125)
                        .addVelocity(dst.getX() + 0.5, dst.getY() + 0.5, dst.getZ() + 0.5)
                        .setColor(packet.r, packet.g, packet.b, packet.r, packet.g * 0.5f, packet.b * 1.5f)
                        .spawn(world, src.getX() + 0.5, src.getY() + 0.5, src.getZ() + 0.5);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
