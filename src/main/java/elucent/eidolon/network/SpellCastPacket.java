package elucent.eidolon.network;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import elucent.eidolon.Eidolon;
import elucent.eidolon.spell.Sign;
import elucent.eidolon.spell.SignSequence;
import elucent.eidolon.spell.Signs;
import elucent.eidolon.spell.Spell;
import elucent.eidolon.spell.Spells;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

public class SpellCastPacket {
    SignSequence seq;
    Spell spell;
    BlockPos pos;
    UUID uuid;

    public SpellCastPacket(Player player, BlockPos pos, Spell spell, SignSequence seq) {
        this.seq = seq;
        this.pos = pos;
        this.spell = spell;
        this.uuid = player.getUUID();
    }

    public SpellCastPacket(UUID uuid, BlockPos pos, ResourceLocation location, SignSequence seq) {
        this.seq = seq;
        this.pos = pos;
        this.spell = Spells.find(location);
        this.uuid = uuid;
    }

    public static void encode(SpellCastPacket object, FriendlyByteBuf buffer) {
        buffer.writeUtf(object.spell.getRegistryName().toString());
        buffer.writeNbt(object.seq.serializeNbt());
        buffer.writeUUID(object.uuid);
        buffer.writeBlockPos(object.pos);
    }

    public static SpellCastPacket decode(FriendlyByteBuf buffer) {
        ResourceLocation spell = new ResourceLocation(buffer.readUtf());
        SignSequence seq = SignSequence.deserializeNbt(buffer.readNbt());
        return new SpellCastPacket(buffer.readUUID(), buffer.readBlockPos(), spell, seq);
    }

    public static void consume(SpellCastPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            assert ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT;

            Level world = Eidolon.proxy.getWorld();
            if (world != null) {
                Player player = world.getPlayerByUUID(packet.uuid);
                if (player != null) {
                    SignSequence signs = packet.seq;
                    packet.spell.cast(world, packet.pos, player, signs);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
