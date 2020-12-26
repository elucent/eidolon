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

public class ExtinguishEffectPacket {
    BlockPos pos;

    public ExtinguishEffectPacket(BlockPos pos) {
        this.pos = pos;
    }

    public static void encode(ExtinguishEffectPacket object, PacketBuffer buffer) {
        buffer.writeBlockPos(object.pos);
    }

    public static ExtinguishEffectPacket decode(PacketBuffer buffer) {
        return new ExtinguishEffectPacket(buffer.readBlockPos());
    }

    public static void consume(ExtinguishEffectPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            assert ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT;

            World world = Eidolon.proxy.getWorld();
            if (world != null) {
                BlockPos pos = packet.pos;
                double x = pos.getX() + 0.5, y = pos.getY() + 1, z = pos.getZ() + 0.5;
                world.playSound(Eidolon.proxy.getPlayer(), x, y, z, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1.0f, 1.0f);

                Particles.create(Registry.SMOKE_PARTICLE)
                    .setAlpha(0.125f, 0).setScale(0.3125f, 0.125f).setLifetime(80)
                    .randomOffset(0.375, 0.125).randomVelocity(0.0125f, 0.0125f)
                    .setColor(0.5f, 0.5f, 0.5f, 0.25f, 0.25f, 0.25f)
                    .repeat(world, x, y, z, 10);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
