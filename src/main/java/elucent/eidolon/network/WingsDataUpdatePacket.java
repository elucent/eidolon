package elucent.eidolon.network;

import java.util.UUID;
import java.util.function.Supplier;

import elucent.eidolon.Eidolon;
import elucent.eidolon.capability.IKnowledge;
import elucent.eidolon.capability.IPlayerData;
import elucent.eidolon.capability.ISoul;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

public class WingsDataUpdatePacket {
    UUID uuid;
    long lastFlapTime;
    int dashTicks;
    boolean isFlying;

    public WingsDataUpdatePacket(Player player) {
    	this.uuid = player.getUUID();
    	player.getCapability(IPlayerData.INSTANCE).ifPresent((d) -> {
    		lastFlapTime = d.getLastFlapTime(player);
    		dashTicks = d.getDashTicks(player);
    		isFlying = d.isFlying(player);
    	});
    }

    public WingsDataUpdatePacket(UUID uuid, long lastFlapTime, int dashTicks, boolean isFlying) {
    	this.uuid = uuid;
    	this.lastFlapTime = lastFlapTime;
    	this.dashTicks = dashTicks;
    	this.isFlying = isFlying;
    }

    public static void encode(WingsDataUpdatePacket object, FriendlyByteBuf buffer) {
    	buffer.writeUUID(object.uuid);
    	buffer.writeLong(object.lastFlapTime);
    	buffer.writeInt(object.dashTicks);
    	buffer.writeBoolean(object.isFlying);
    }

    public static WingsDataUpdatePacket decode(FriendlyByteBuf buffer) {
    	return new WingsDataUpdatePacket(buffer.readUUID(), buffer.readLong(), buffer.readInt(), buffer.readBoolean());
    }

    public static void consume(WingsDataUpdatePacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            assert ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT;

            Level world = Eidolon.proxy.getWorld();
            Player p = world.getPlayerByUUID(packet.uuid);
            if (p != null && p != Minecraft.getInstance().player) {
                p.getCapability(IPlayerData.INSTANCE, null).ifPresent((d) -> {
                	if (packet.isFlying && !d.isFlying(p)) d.startFlying(p);
                	else if (!packet.isFlying && d.isFlying(p)) d.stopFlying(p);
                	d.setLastFlapTime(packet.lastFlapTime);
                	d.setDashTicks(packet.dashTicks);
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
