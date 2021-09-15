package elucent.eidolon.network;

import elucent.eidolon.Eidolon;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ChilledEffectPacket {
    double x, y, z;

    public ChilledEffectPacket(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static void encode(ChilledEffectPacket object, PacketBuffer buffer) {
        buffer.writeDouble(object.x);
        buffer.writeDouble(object.y);
        buffer.writeDouble(object.z);
    }

    public static ChilledEffectPacket decode(PacketBuffer buffer) {
        return new ChilledEffectPacket(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
    }

    public static void consume(ChilledEffectPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            assert ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT;

            PlayerEntity player = Eidolon.proxy.getPlayer();
            if (player != null) {
                World world = player.world;
                world.playSound(player, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 1.0f, 1.0f);
                for (int i = 0; i < 5; i ++) {
                    world.addParticle(
                        new BlockParticleData(ParticleTypes.BLOCK, Blocks.ICE.getDefaultState()),
                        packet.x, packet.y, packet.z,
                        0.05f * world.rand.nextGaussian(), 0.05f * world.rand.nextGaussian(), 0.05f * world.rand.nextGaussian()
                    );
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
