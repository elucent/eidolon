package elucent.eidolon.network;

import elucent.eidolon.Eidolon;
import elucent.eidolon.Registry;
import elucent.eidolon.capability.IKnowledge;
import elucent.eidolon.capability.KnowledgeProvider;
import elucent.eidolon.entity.ChantCasterEntity;
import elucent.eidolon.particle.Particles;
import elucent.eidolon.spell.Sign;
import elucent.eidolon.spell.Signs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class AttemptCastPacket {
    List<Sign> signs = new ArrayList<>();
    UUID uuid;

    public AttemptCastPacket(PlayerEntity player, List<Sign> signs) {
        this.signs.addAll(signs);
        this.uuid = player.getUniqueID();
    }

    public AttemptCastPacket(UUID uuid, List<Sign> signs) {
        this.signs.addAll(signs);
        this.uuid = uuid;
    }

    public static void encode(AttemptCastPacket object, PacketBuffer buffer) {
        buffer.writeInt(object.signs.size());
        for (int i = 0; i < object.signs.size(); i ++) buffer.writeString(object.signs.get(i).getRegistryName().toString());
        buffer.writeUniqueId(object.uuid);
    }

    public static AttemptCastPacket decode(PacketBuffer buffer) {
        int n = buffer.readInt();
        List<Sign> signs = new ArrayList<>();
        for (int i = 0; i < n; i ++) signs.add(Signs.find(new ResourceLocation(buffer.readString())));
        return new AttemptCastPacket(buffer.readUniqueId(), signs);
    }

    public static void consume(AttemptCastPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            assert ctx.get().getDirection() == NetworkDirection.PLAY_TO_SERVER;

            World world = ctx.get().getSender().world;
            if (world != null) {
                PlayerEntity player = world.getPlayerByUuid(packet.uuid);
                if (player != null) {
                    List<Sign> signs = packet.signs;
                    IKnowledge knowledge = player.getCapability(KnowledgeProvider.CAPABILITY, null).resolve().get();
                    for (Sign sign : signs) if (!knowledge.knowsSign(sign)) return;
                    Vector3d placement = player.getPositionVec().add(0, player.getHeight() * 2 / 3, 0).add(player.getLookVec());
                    ChantCasterEntity entity = new ChantCasterEntity(world, player, signs);
                    entity.setPosition(placement.x, placement.y, placement.z);
                    world.addEntity(entity);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
