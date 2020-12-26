package elucent.eidolon.network;

import java.util.function.Supplier;

import elucent.eidolon.Eidolon;
import elucent.eidolon.Registry;
import elucent.eidolon.particle.Particles;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

public class IgniteEffectPacket {
    BlockPos pos;
    float r, g, b;

    public IgniteEffectPacket(BlockPos pos, float r, float g, float b) {
        this.pos = pos;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public static void encode(IgniteEffectPacket object, PacketBuffer buffer) {
        buffer.writeBlockPos(object.pos);
        buffer.writeFloat(object.r).writeFloat(object.g).writeFloat(object.b);
    }

    public static IgniteEffectPacket decode(PacketBuffer buffer) {
        return new IgniteEffectPacket(buffer.readBlockPos(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat());
    }

    public static void consume(IgniteEffectPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            assert ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT;

            World world = Eidolon.proxy.getWorld();
            if (world != null) {
                BlockPos pos = packet.pos;
                double x = pos.getX() + 0.5, y = pos.getY() + 1, z = pos.getZ() + 0.5;
                world.playSound(Eidolon.proxy.getPlayer(), x, y, z, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0f, 1.0f);

                Particles.create(Registry.FLAME_PARTICLE)
                    .setAlpha(0.75f, 0).setScale(0.5f, 0.25f).setLifetime(20)
                    .randomOffset(0.5, 0.125).randomVelocity(0.00625f, 0.01875f)
                    .addVelocity(0, 0.00625f, 0)
                    .setColor(packet.r, packet.g, packet.b, packet.r, packet.g * 0.5f, packet.b * 1.5f)
                    .repeat(world, x, y, z, 10);
                Particles.create(Registry.SPARKLE_PARTICLE)
                    .setAlpha(1, 0).setScale(0.0625f, 0).setLifetime(40)
                    .randomOffset(0.0625, 0).randomVelocity(0.125f, 0)
                    .addVelocity(0, 0.125f, 0)
                    .setColor(packet.r, packet.g * 1.5f, packet.b * 2.0f, packet.r, packet.g, packet.b)
                    .enableGravity().setSpin(0.4f)
                    .repeat(world, x, y, z, world.rand.nextInt(2) + 2);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
