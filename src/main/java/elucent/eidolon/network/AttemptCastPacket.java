package elucent.eidolon.network;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import elucent.eidolon.capability.IKnowledge;
import elucent.eidolon.entity.ChantCasterEntity;
import elucent.eidolon.spell.Sign;
import elucent.eidolon.spell.Signs;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraft.world.level.Level;



public class AttemptCastPacket {
    List<Sign> signs = new ArrayList<>();
    UUID uuid;

    public AttemptCastPacket(Player player, List<Sign> signs) {
        this.signs.addAll(signs);
        this.uuid = player.getUUID();
    }

    public AttemptCastPacket(UUID uuid, List<Sign> signs) {
        this.signs.addAll(signs);
        this.uuid = uuid;
    }

    public static void encode(AttemptCastPacket object, FriendlyByteBuf buffer) {
        buffer.writeInt(object.signs.size());
        for (int i = 0; i < object.signs.size(); i ++) buffer.writeUtf(object.signs.get(i).getRegistryName().toString(), 255);
        buffer.writeUUID(object.uuid);
    }

    public static AttemptCastPacket decode(FriendlyByteBuf buffer) {
        int n = buffer.readInt();
        List<Sign> signs = new ArrayList<>();
        for (int i = 0; i < n; i ++) signs.add(Signs.find(new ResourceLocation(buffer.readUtf(255))));
        return new AttemptCastPacket(buffer.readUUID(), signs);
    }

    public static void consume(AttemptCastPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            assert ctx.get().getDirection() == NetworkDirection.PLAY_TO_SERVER;

            Level world = ctx.get().getSender().level;
            if (world != null) {
                Player player = world.getPlayerByUUID(packet.uuid);
                if (player != null) {
                    List<Sign> signs = packet.signs;
                    IKnowledge knowledge = player.getCapability(IKnowledge.INSTANCE, null).resolve().get();
                    for (Sign sign : signs) if (!knowledge.knowsSign(sign)) return;
                    Vec3 placement = player.position().add(0, player.getBbHeight() * 2 / 3, 0).add(player.getLookAngle());
                    ChantCasterEntity entity = new ChantCasterEntity(world, player, signs);
                    entity.setPos(placement.x, placement.y, placement.z);
                    world.addFreshEntity(entity);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
