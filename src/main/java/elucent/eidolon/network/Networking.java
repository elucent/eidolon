package elucent.eidolon.network;

import elucent.eidolon.Eidolon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

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

        INSTANCE.registerMessage(
            ++ id,
            MagicBurstEffectPacket.class,
            MagicBurstEffectPacket::encode,
            MagicBurstEffectPacket::decode,
            MagicBurstEffectPacket::consume
        );

        INSTANCE.registerMessage(
            ++ id,
            KnowledgeUpdatePacket.class,
            KnowledgeUpdatePacket::encode,
            KnowledgeUpdatePacket::decode,
            KnowledgeUpdatePacket::consume
        );

        INSTANCE.registerMessage(
            ++ id,
            AttemptCastPacket.class,
            AttemptCastPacket::encode,
            AttemptCastPacket::decode,
            AttemptCastPacket::consume
        );

        INSTANCE.registerMessage(
            ++ id,
            SpellCastPacket.class,
            SpellCastPacket::encode,
            SpellCastPacket::decode,
            SpellCastPacket::consume
        );
    }

    public static <MSG> void sendToDimension(Level world, MSG msg, ResourceKey<Level> dimension) {
        Networking.INSTANCE.send(PacketDistributor.DIMENSION.with(() -> dimension), msg);
    }

    public static <MSG> void sendToTracking(Level world, BlockPos pos, MSG msg) {
        Networking.INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> world.getChunkAt(pos)), msg);
    }

    public static <MSG> void sendTo(Player entity, MSG msg) {
        Networking.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer)entity), msg);
    }

    public static <MSG> void sendToServer(MSG msg) {
        Networking.INSTANCE.sendToServer(msg);
    }
}
