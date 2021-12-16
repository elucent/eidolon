package elucent.eidolon.network;

import java.util.UUID;
import java.util.function.Supplier;

import elucent.eidolon.Eidolon;
import elucent.eidolon.capability.IKnowledge;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;



public class KnowledgeUpdatePacket {
    UUID uuid;
    CompoundTag tag;
    boolean playSound;

    public KnowledgeUpdatePacket(UUID uuid, CompoundTag tag, boolean playSound) {
        this.uuid = uuid;
        this.tag = tag;
        this.playSound = playSound;
    }

    public KnowledgeUpdatePacket(Player entity, boolean playSound) {
        this.uuid = entity.getUUID();
        entity.getCapability(IKnowledge.INSTANCE, null).ifPresent((k) -> {
            this.tag = ((INBTSerializable<CompoundTag>)k).serializeNBT();
        });
        this.playSound = playSound;
    }

    public static void encode(KnowledgeUpdatePacket object, FriendlyByteBuf buffer) {
        buffer.writeUUID(object.uuid);
        buffer.writeNbt(object.tag);
        buffer.writeBoolean(object.playSound);
    }

    public static KnowledgeUpdatePacket decode(FriendlyByteBuf buffer) {
        return new KnowledgeUpdatePacket(buffer.readUUID(), buffer.readNbt(), buffer.readBoolean());
    }

    public static void consume(KnowledgeUpdatePacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            assert ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT;

            Level world = Eidolon.proxy.getWorld();
            Player player = world.getPlayerByUUID(packet.uuid);
            if (player != null) {
                player.getCapability(IKnowledge.INSTANCE, null).ifPresent((k) -> {
                	((INBTSerializable<CompoundTag>)k).deserializeNBT(packet.tag);
                    if (packet.playSound) player.playSound(SoundEvents.PLAYER_LEVELUP, 1.0f, 0.5f);
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
