package elucent.eidolon.network;

import elucent.eidolon.Eidolon;
import elucent.eidolon.capability.IKnowledge;
import elucent.eidolon.capability.KnowledgeProvider;
import elucent.eidolon.spell.Sign;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

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

            World world = Minecraft.getInstance().world;
            PlayerEntity player = world.getPlayerByUuid(packet.uuid);
            if (player != null) {
                player.getCapability(KnowledgeProvider.CAPABILITY, null).ifPresent((k) -> {
                    Set<Sign> before = new HashSet<>(), after = new HashSet<>();
                    for (Sign s : k.getKnownSigns()) before.add(s);
                    KnowledgeProvider.CAPABILITY.getStorage().readNBT(KnowledgeProvider.CAPABILITY, k, null, packet.tag);
                    for (Sign s : k.getKnownSigns()) after.add(s);
                    for (Sign s : before) after.remove(s);
                    if (packet.playSound) player.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, 1.0f, 0.5f);
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
