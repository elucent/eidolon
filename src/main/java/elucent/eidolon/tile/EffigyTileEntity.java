package elucent.eidolon.tile;

import elucent.eidolon.Registry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

public class EffigyTileEntity extends TileEntityBase {
    long previous = -1;

    public EffigyTileEntity(BlockPos pos, BlockState state) {
        super(Registry.EFFIGY_TILE_ENTITY.get(), pos, state);
    }

    public boolean ready() {
        return true; // world.getGameTime() - previous >= 24000;
    }

    public void pray() {
        if (!level.isClientSide) {
            previous = level.getGameTime();
            sync();
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        previous = tag.getLong("previous");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putLong("previous", previous);
    }
}
