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

public class CrystallizeEffectPacket {
    BlockPos pos;

    public CrystallizeEffectPacket(BlockPos pos) {
        this.pos = pos;
    }

    public static void encode(CrystallizeEffectPacket object, FriendlyByteBuf buffer) {
        buffer.writeBlockPos(object.pos);
    }

    public static CrystallizeEffectPacket decode(FriendlyByteBuf buffer) {
        return new CrystallizeEffectPacket(buffer.readBlockPos());
    }

    public static void consume(CrystallizeEffectPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            assert ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT;

            Level world = Eidolon.proxy.getWorld();
            if (world != null) {
                BlockPos pos = packet.pos;
                double x = pos.getX() + 0.5, y = pos.getY() + 0.1, z = pos.getZ() + 0.5;

                float r = 247 / 255.0f, g = 156 / 255.0f, b = 220 / 255.0f;

                Particles.create(Registry.SPARKLE_PARTICLE)
                    .setAlpha(1.0f, 0).setScale(0.25f, 0).setLifetime(20)
                    .randomOffset(0.5, 0).randomVelocity(0, 0.375f)
                    .addVelocity(0, 0.125f, 0)
                    .setColor(r, g, b, r, g * 0.5f, b * 1.5f)
                    .setSpin(0.4f)
                    .repeat(world, x, y, z, 20);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
