package elucent.eidolon.network;

import elucent.eidolon.Eidolon;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class TESyncPacket {
    BlockPos pos;
    CompoundTag tag;

    public TESyncPacket(BlockPos pos, CompoundTag tag) {
        this.pos = pos;
        this.tag = tag;
    }

    public static void encode(TESyncPacket object, FriendlyByteBuf buffer) {
        buffer.writeBlockPos(object.pos);
        buffer.writeNbt(object.tag);
    }

    public static TESyncPacket decode(FriendlyByteBuf buffer) {
        return new TESyncPacket(buffer.readBlockPos(), buffer.readNbt());
    }

    public static void consume(TESyncPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Level world;
            if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT)
                world = Eidolon.proxy.getWorld();
            else {
                if (ctx.get().getSender() == null) return;
                world = ctx.get().getSender().level;
            }

            world.getBlockEntity(packet.pos).load(world.getBlockState(packet.pos), packet.tag);
            world.getBlockEntity(packet.pos).setChanged();
        });
        ctx.get().setPacketHandled(true);
    }
}
