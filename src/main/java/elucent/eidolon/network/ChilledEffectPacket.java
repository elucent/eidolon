package elucent.eidolon.network;

import java.util.function.Supplier;

import elucent.eidolon.Eidolon;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;



public class ChilledEffectPacket {
    double x, y, z;

    public ChilledEffectPacket(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static void encode(ChilledEffectPacket object, FriendlyByteBuf buffer) {
        buffer.writeDouble(object.x);
        buffer.writeDouble(object.y);
        buffer.writeDouble(object.z);
    }

    public static ChilledEffectPacket decode(FriendlyByteBuf buffer) {
        return new ChilledEffectPacket(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
    }

    public static void consume(ChilledEffectPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            assert ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT;

            Player player = Eidolon.proxy.getPlayer();
            if (player != null) {
                Level world = player.level;
                world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 1.0f, 1.0f);
                for (int i = 0; i < 5; i ++) {
                    world.addParticle(
                        new BlockParticleOption(ParticleTypes.BLOCK, Blocks.ICE.defaultBlockState()),
                        packet.x, packet.y, packet.z,
                        0.05f * world.random.nextGaussian(), 0.05f * world.random.nextGaussian(), 0.05f * world.random.nextGaussian()
                    );
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
