package elucent.eidolon.network;

import elucent.eidolon.Eidolon;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Dimension;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class Networking {
    public static SimpleChannel INSTANCE;

    static int id = 0;

    public static void init() {
        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(Eidolon.MODID, "network"), () -> "1.0", (s) -> true, (s) -> true);

        INSTANCE.registerMessage(
            ++ id,
            ChilledEffectPacket.class,
            ChilledEffectPacket::encode,
            ChilledEffectPacket::decode,
            ChilledEffectPacket::consume
        );

        INSTANCE.registerMessage(
            ++ id,
            TESyncPacket.class,
            TESyncPacket::encode,
            TESyncPacket::decode,
            TESyncPacket::consume
        );

        INSTANCE.registerMessage(
            ++ id,
            ExtinguishEffectPacket.class,
            ExtinguishEffectPacket::encode,
            ExtinguishEffectPacket::decode,
            ExtinguishEffectPacket::consume
        );

        INSTANCE.registerMessage(
            ++ id,
            IgniteEffectPacket.class,
            IgniteEffectPacket::encode,
            IgniteEffectPacket::decode,
            IgniteEffectPacket::consume
        );

        INSTANCE.registerMessage(
            ++ id,
            FlameEffectPacket.class,
            FlameEffectPacket::encode,
            FlameEffectPacket::decode,
            FlameEffectPacket::consume
        );

        INSTANCE.registerMessage(
            ++ id,
            RitualCompletePacket.class,
            RitualCompletePacket::encode,
            RitualCompletePacket::decode,
            RitualCompletePacket::consume
        );

        INSTANCE.registerMessage(
            ++ id,
            RitualConsumePacket.class,
            RitualConsumePacket::encode,
            RitualConsumePacket::decode,
            RitualConsumePacket::consume
        );

        INSTANCE.registerMessage(
            ++ id,
            CrystallizeEffectPacket.class,
            CrystallizeEffectPacket::encode,
            CrystallizeEffectPacket::decode,
            CrystallizeEffectPacket::consume
        );

        INSTANCE.registerMessage(
            ++ id,
            CrucibleFailPacket.class,
            CrucibleFailPacket::encode,
            CrucibleFailPacket::decode,
            CrucibleFailPacket::consume
        );

        INSTANCE.registerMessage(
            ++ id,
            CrucibleSuccessPacket.class,
            CrucibleSuccessPacket::encode,
            CrucibleSuccessPacket::decode,
            CrucibleSuccessPacket::consume
        );

        INSTANCE.registerMessage(
            ++ id,
            LifestealEffectPacket.class,
            LifestealEffectPacket::encode,
            LifestealEffectPacket::decode,
            LifestealEffectPacket::consume
        );
    }

    public static <MSG> void sendToDimension(World world, MSG msg, RegistryKey<World> dimension) {
        Networking.INSTANCE.send(PacketDistributor.DIMENSION.with(() -> dimension), msg);
    }

    public static <MSG> void sendToTracking(World world, BlockPos pos, MSG msg) {
        Networking.INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> world.getChunkAt(pos)), msg);
    }
}
