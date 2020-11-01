package elucent.eidolon.network;

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

import java.util.function.Supplier;

public class CrucibleSuccessPacket {
    BlockPos pos;
    float r, g, b;

    public CrucibleSuccessPacket(BlockPos pos, float r, float g, float b) {
        this.pos = pos;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public static void encode(CrucibleSuccessPacket object, PacketBuffer buffer) {
        buffer.writeBlockPos(object.pos);
        buffer.writeFloat(object.r).writeFloat(object.g).writeFloat(object.b);
    }

    public static CrucibleSuccessPacket decode(PacketBuffer buffer) {
        return new CrucibleSuccessPacket(buffer.readBlockPos(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat());
    }

    public static void consume(CrucibleSuccessPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            assert ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT;

            World world = Eidolon.proxy.getWorld();
            if (world != null) {
                BlockPos pos = packet.pos;
                double x = pos.getX() + 0.5, y = pos.getY() + 1, z = pos.getZ() + 0.5;
                world.playSound(Eidolon.proxy.getPlayer(), x, y, z, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1.0f, 0.75f);
                world.playSound(Eidolon.proxy.getPlayer(), x, y, z, SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.BLOCKS, 1.0f, 0.75f);

                Particles.create(Registry.STEAM_PARTICLE)
                    .setAlpha(0.0625f, 0).setScale(0.375f, 0.125f).setLifetime(40)
                    .randomOffset(0.375, 0.125).randomVelocity(0.025f, 0.0125f)
                    .addVelocity(0, 0.0125f, 0)
                    .setColor(packet.r, packet.g, packet.b)
                    .repeat(world, pos.getX() + 0.5, pos.getY() + 0.875, pos.getZ() + 0.5, 20);
                Particles.create(Registry.SPARKLE_PARTICLE)
                    .setAlpha(0.0625f, 0).setScale(0.125f, 0.0f).setLifetime(80)
                    .randomOffset(0.375, 0.375).randomVelocity(0.00625f, 0.00625f)
                    .addVelocity(0, 0, 0)
                    .setColor(packet.r, packet.g, packet.b)
                    .setSpin(0.1f)
                    .repeat(world, pos.getX() + 0.5, pos.getY() + 1.25, pos.getZ() + 0.5, 8);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
