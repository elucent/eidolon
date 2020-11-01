package elucent.eidolon.network;

import elucent.eidolon.Eidolon;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class TESyncPacket {
    BlockPos pos;
    CompoundNBT tag;

    public TESyncPacket(BlockPos pos, CompoundNBT tag) {
        this.pos = pos;
        this.tag = tag;
    }

    public static void encode(TESyncPacket object, PacketBuffer buffer) {
        buffer.writeBlockPos(object.pos);
        buffer.writeCompoundTag(object.tag);
    }

    public static TESyncPacket decode(PacketBuffer buffer) {
        return new TESyncPacket(buffer.readBlockPos(), buffer.readCompoundTag());
    }

    public static void consume(TESyncPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            World world;
            if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT)
                world = Eidolon.proxy.getWorld();
            else {
                if (ctx.get().getSender() == null) return;
                world = ctx.get().getSender().world;
            }

            world.getTileEntity(packet.pos).read(world.getBlockState(packet.pos), packet.tag);
            world.getTileEntity(packet.pos).markDirty();
        });
        ctx.get().setPacketHandled(true);
    }
}
