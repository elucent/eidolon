package elucent.eidolon.network;

import java.util.UUID;
import java.util.function.Supplier;

import elucent.eidolon.Eidolon;
import elucent.eidolon.capability.IKnowledge;
import elucent.eidolon.capability.ISoul;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

public class SoulUpdatePacket {
	boolean isPlayer;
    UUID uuid;
    int id;
    CompoundTag tag;

    public SoulUpdatePacket(int id, CompoundTag tag) {
    	this.isPlayer = false;
        this.id = id;
        this.tag = tag;
    }

    public SoulUpdatePacket(UUID uuid, CompoundTag tag) {
    	this.isPlayer = true;
        this.uuid = uuid;
        this.tag = tag;
    }

    public SoulUpdatePacket(LivingEntity entity) {
    	this.isPlayer = entity instanceof Player;
        if (isPlayer) this.uuid = entity.getUUID();
        else this.id = entity.getId();
        entity.getCapability(ISoul.INSTANCE, null).ifPresent((k) -> {
            this.tag = ((INBTSerializable<CompoundTag>)k).serializeNBT();
        });
    }

    public SoulUpdatePacket(Player entity) {
    	this.isPlayer = true;
        this.uuid = entity.getUUID();
        entity.getCapability(ISoul.INSTANCE, null).ifPresent((k) -> {
            this.tag = ((INBTSerializable<CompoundTag>)k).serializeNBT();
        });
    }

    public static void encode(SoulUpdatePacket object, FriendlyByteBuf buffer) {
    	buffer.writeBoolean(object.isPlayer);
    	if (object.isPlayer) buffer.writeUUID(object.uuid);
    	else buffer.writeInt(object.id);
        buffer.writeNbt(object.tag);
    }

    public static SoulUpdatePacket decode(FriendlyByteBuf buffer) {
    	if (buffer.readBoolean()) {
    		return new SoulUpdatePacket(buffer.readUUID(), buffer.readNbt());
    	}
    	else return new SoulUpdatePacket(buffer.readInt(), buffer.readNbt());
    }

    public static void consume(SoulUpdatePacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            assert ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT;

            Level world = Eidolon.proxy.getWorld();
            LivingEntity e = packet.isPlayer ? world.getPlayerByUUID(packet.uuid) : (LivingEntity)world.getEntity(packet.id);
            if (e != null) {
                e.getCapability(ISoul.INSTANCE, null).ifPresent((k) -> {
                	((INBTSerializable<CompoundTag>)k).deserializeNBT(packet.tag);
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
