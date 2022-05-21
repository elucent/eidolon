package elucent.eidolon.network;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import elucent.eidolon.capability.IKnowledge;
import elucent.eidolon.capability.IPlayerData;
import elucent.eidolon.entity.ChantCasterEntity;
import elucent.eidolon.item.IWingsItem;
import elucent.eidolon.spell.Rune;
import elucent.eidolon.spell.Runes;
import elucent.eidolon.spell.Sign;
import elucent.eidolon.spell.Signs;
import elucent.eidolon.util.KnowledgeUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraft.world.level.Level;

public class WingsFlapPacket {
    UUID uuid;

    public WingsFlapPacket(Player player) {
        this.uuid = player.getUUID();
    }

    public WingsFlapPacket(UUID uuid) {
        this.uuid = uuid;
    }

    public static void encode(WingsFlapPacket object, FriendlyByteBuf buffer) {
        buffer.writeUUID(object.uuid);
    }

    public static WingsFlapPacket decode(FriendlyByteBuf buffer) {
        return new WingsFlapPacket(buffer.readUUID());
    }

    public static void consume(WingsFlapPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            assert ctx.get().getDirection() == NetworkDirection.PLAY_TO_SERVER;

            Level world = ctx.get().getSender().level;
            if (world != null) {
                Player player = world.getPlayerByUUID(packet.uuid);
                if (player != null) {
                	player.getCapability(IPlayerData.INSTANCE).ifPresent((d) -> {
                		d.tryFlapWings(player);
                	});
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
