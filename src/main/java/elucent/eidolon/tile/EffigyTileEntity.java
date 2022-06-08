package elucent.eidolon.tile;

import elucent.eidolon.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

public class EffigyTileEntity extends TileEntityBase {
    long previous = -1;

    public EffigyTileEntity() {
        super(Registry.EFFIGY_TILE_ENTITY);
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
    public void load(BlockState state, CompoundTag tag) {
        super.load(state, tag);
        previous = tag.getLong("previous");
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag = super.save(tag);
        tag.putLong("previous", previous);
        return tag;
    }
}
