package elucent.eidolon.tile;

import elucent.eidolon.Registry;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;

public class EffigyTileEntity extends TileEntityBase {
    long previous = -1;

    public EffigyTileEntity() {
        super(Registry.EFFIGY_TILE_ENTITY);
    }

    public boolean ready() {
        return true; // world.getGameTime() - previous >= 24000;
    }

    public void pray() {
        if (!world.isRemote) {
            previous = world.getGameTime();
            sync();
        }
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        super.read(state, tag);
        previous = tag.getLong("previous");
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag = super.write(tag);
        tag.putLong("previous", previous);
        return tag;
    }
}
