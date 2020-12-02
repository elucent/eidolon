package elucent.eidolon.network;

import elucent.eidolon.Eidolon;
import elucent.eidolon.capability.IKnowledge;
import elucent.eidolon.capability.KnowledgeProvider;
import elucent.eidolon.entity.ChantCasterEntity;
import elucent.eidolon.spell.Sign;
import elucent.eidolon.spell.Signs;
import elucent.eidolon.spell.Spell;
import elucent.eidolon.spell.Spells;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class SpellCastPacket {
    List<Sign> signs = new ArrayList<>();
    Spell spell;
    BlockPos pos;
    UUID uuid;

    public SpellCastPacket(PlayerEntity player, BlockPos pos, Spell spell, List<Sign> signs) {
        this.signs.addAll(signs);
        this.pos = pos;
        this.spell = spell;
        this.uuid = player.getUniqueID();
    }

    public SpellCastPacket(UUID uuid, BlockPos pos, ResourceLocation location, List<Sign> signs) {
        this.signs.addAll(signs);
        this.pos = pos;
        this.spell = Spells.find(location);
        this.uuid = uuid;
    }

    public static void encode(SpellCastPacket object, PacketBuffer buffer) {
        buffer.writeString(object.spell.getRegistryName().toString());
        buffer.writeInt(object.signs.size());
        for (int i = 0; i < object.signs.size(); i ++) buffer.writeString(object.signs.get(i).getRegistryName().toString());
        buffer.writeUniqueId(object.uuid);
        buffer.writeBlockPos(object.pos);
    }

    public static SpellCastPacket decode(PacketBuffer buffer) {
        ResourceLocation spell = new ResourceLocation(buffer.readString());
        int n = buffer.readInt();
        List<Sign> signs = new ArrayList<>();
        for (int i = 0; i < n; i ++) signs.add(Signs.find(new ResourceLocation(buffer.readString())));
        return new SpellCastPacket(buffer.readUniqueId(), buffer.readBlockPos(), spell, signs);
    }

    public static void consume(SpellCastPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            assert ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT;

            World world = Eidolon.proxy.getWorld();
            if (world != null) {
                PlayerEntity player = world.getPlayerByUuid(packet.uuid);
                if (player != null) {
                    List<Sign> signs = packet.signs;
                    packet.spell.cast(world, packet.pos, player, signs);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
