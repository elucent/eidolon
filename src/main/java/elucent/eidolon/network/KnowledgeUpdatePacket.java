package elucent.eidolon.network;

import java.util.UUID;
import java.util.function.Supplier;

import elucent.eidolon.Eidolon;
import elucent.eidolon.capability.KnowledgeProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

public class KnowledgeUpdatePacket {
    UUID uuid;
    CompoundNBT tag;
    boolean playSound;

    public KnowledgeUpdatePacket(UUID uuid, CompoundNBT tag, boolean playSound) {
        this.uuid = uuid;
        this.tag = tag;
        this.playSound = playSound;
    }

    public KnowledgeUpdatePacket(PlayerEntity entity, boolean playSound) {
        this.uuid = entity.getUniqueID();
        entity.getCapability(KnowledgeProvider.CAPABILITY, null).ifPresent((k) -> {
            this.tag = (CompoundNBT)KnowledgeProvider.CAPABILITY.getStorage().writeNBT(KnowledgeProvider.CAPABILITY, k, null);
        });
        this.playSound = playSound;
    }

    public static void encode(KnowledgeUpdatePacket object, PacketBuffer buffer) {
        buffer.writeUniqueId(object.uuid);
        buffer.writeCompoundTag(object.tag);
        buffer.writeBoolean(object.playSound);
    }

    public static KnowledgeUpdatePacket decode(PacketBuffer buffer) {
        return new KnowledgeUpdatePacket(buffer.readUniqueId(), buffer.readCompoundTag(), buffer.readBoolean());
    }

    public static void consume(KnowledgeUpdatePacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            assert ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT;

            World world = Eidolon.proxy.getWorld();
            PlayerEntity player = world.getPlayerByUuid(packet.uuid);
            if (player != null) {
                player.getCapability(KnowledgeProvider.CAPABILITY, null).ifPresent((k) -> {
                    KnowledgeProvider.CAPABILITY.getStorage().readNBT(KnowledgeProvider.CAPABILITY, k, null, packet.tag);
                    if (packet.playSound) player.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, 1.0f, 0.5f);
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
