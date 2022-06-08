package elucent.eidolon.network;

import elucent.eidolon.Eidolon;
import elucent.eidolon.spell.Sign;
import elucent.eidolon.spell.Signs;
import elucent.eidolon.spell.Spell;
import elucent.eidolon.spell.Spells;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class SpellCastPacket {
    List<Sign> signs = new ArrayList<>();
    Spell spell;
    BlockPos pos;
    UUID uuid;

    public SpellCastPacket(Player player, BlockPos pos, Spell spell, List<Sign> signs) {
        this.signs.addAll(signs);
        this.pos = pos;
        this.spell = spell;
        this.uuid = player.getUUID();
    }

    public SpellCastPacket(UUID uuid, BlockPos pos, ResourceLocation location, List<Sign> signs) {
        this.signs.addAll(signs);
        this.pos = pos;
        this.spell = Spells.find(location);
        this.uuid = uuid;
    }

    public static void encode(SpellCastPacket object, FriendlyByteBuf buffer) {
        buffer.writeUtf(object.spell.getRegistryName().toString());
        buffer.writeInt(object.signs.size());
        for (int i = 0; i < object.signs.size(); i ++) buffer.writeUtf(object.signs.get(i).getRegistryName().toString());
        buffer.writeUUID(object.uuid);
        buffer.writeBlockPos(object.pos);
    }

    public static SpellCastPacket decode(FriendlyByteBuf buffer) {
        ResourceLocation spell = new ResourceLocation(buffer.readUtf());
        int n = buffer.readInt();
        List<Sign> signs = new ArrayList<>();
        for (int i = 0; i < n; i ++) signs.add(Signs.find(new ResourceLocation(buffer.readUtf())));
        return new SpellCastPacket(buffer.readUUID(), buffer.readBlockPos(), spell, signs);
    }

    public static void consume(SpellCastPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            assert ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT;

            Level world = Eidolon.proxy.getWorld();
            if (world != null) {
                Player player = world.getPlayerByUUID(packet.uuid);
                if (player != null) {
                    List<Sign> signs = packet.signs;
                    packet.spell.cast(world, packet.pos, player, signs);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
