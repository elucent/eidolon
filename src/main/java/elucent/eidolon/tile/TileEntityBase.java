package elucent.eidolon.tile;

import elucent.eidolon.network.Networking;
import elucent.eidolon.network.TESyncPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.Connection;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.network.PacketDistributor;

public class TileEntityBase extends BlockEntity {
    public TileEntityBase(BlockEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public void onDestroyed(BlockState state, BlockPos pos) {
        invalidateCaps();
    }

    public InteractionResult onActivated(BlockState state, BlockPos pos, Player player, InteractionHand hand) {
        return InteractionResult.PASS;
    }

    public void sync() {
        setChanged();
        if (level.isClientSide)
            Networking.INSTANCE.sendToServer(new TESyncPacket(worldPosition, save(new CompoundTag())));
        else
            Networking.INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> level.getChunkAt(worldPosition)), new TESyncPacket(worldPosition, save(new CompoundTag())));
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.save(new CompoundTag());
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return new ClientboundBlockEntityDataPacket(this.worldPosition, 3, this.getUpdateTag());
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
        handleUpdateTag(level.getBlockState(pkt.getPos()), pkt.getTag());
    }
}
